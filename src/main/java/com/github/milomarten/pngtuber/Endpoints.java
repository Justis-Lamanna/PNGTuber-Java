package com.github.milomarten.pngtuber;

import com.github.milomarten.pngtuber.model.PngTuber;
import com.github.milomarten.pngtuber.model.PngTuberPart;
import com.github.milomarten.pngtuber.model.UnknownFileType;
import com.github.milomarten.pngtuber.service.CdnService;
import com.github.milomarten.pngtuber.service.DiscordService;
import com.github.milomarten.pngtuber.service.PngTuberService;
import discord4j.common.util.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class Endpoints {
    @Autowired
    private DiscordService discordService;

    @Autowired
    private PngTuberService pngTuberService;

    @Autowired
    private CdnService cdnService;

    @Value("${channel.default}")
    private Snowflake defaultChannel;

    @PostMapping(value = "pngtuber/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> upload(@RequestPart("id") String id,
                             @RequestPart(value = "idle", required = false) FilePart idle,
                             @RequestPart(value = "offline", required = false) FilePart offline,
                             @RequestPart(value = "speaking", required = false) FilePart speaking,
                             @RequestPart(value = "idleUrl", required = false) String idleUrl,
                             @RequestPart(value = "offlineUrl", required = false) String offlineUrl,
                             @RequestPart(value = "speakingUrl", required = false) String speakingUrl) {
        Mono<Tuple2<PngTuberPart, String>> idlePart = uploadOrGetUrl(PngTuberPart.IDLE, idle, idleUrl);
        Mono<Tuple2<PngTuberPart, String>> speakingPart = uploadOrGetUrl(PngTuberPart.SPEAKING, speaking, speakingUrl);
        Mono<Tuple2<PngTuberPart, String>> offlinePart = uploadOrGetUrl(PngTuberPart.OFFLINE, offline, offlineUrl);

        return Flux.merge(idlePart, speakingPart, offlinePart)
                .collect(new PngTuber.Collector(Long.parseLong(id)))
                .flatMap(pngTuberService::savePngTuber)
                .map(PngTuber::toString);
    }

    @GetMapping("pngtuber/{user}/name")
    public Mono<String> username(@PathVariable Snowflake user) {
        return discordService.getUsername(user, defaultChannel);
    }

    @GetMapping("pngtuber/{user}/urls")
    public Mono<PngTuber> pngTuber(@PathVariable Snowflake user) {
        return pngTuberService.getPngTuber(user);
    }

    @GetMapping("cdn/{id}")
    public Mono<ResponseEntity<byte[]>> getImage(@PathVariable long id) {
        return cdnService.get(id)
                .map(img -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(img.getMimeType()));
                    return new ResponseEntity<>(img.getImage(), headers, HttpStatus.OK);
                })
                .switchIfEmpty(Mono.fromRunnable(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    private Mono<Tuple2<PngTuberPart, String>> uploadOrGetUrl(PngTuberPart part, FilePart upload, String url) {
        if(isImage(upload)) {
            return Mono.justOrEmpty(upload)
                    .flatMap(fp -> cdnService.upload(fp))
                    .map(fileId -> "/api/cdn/" + fileId)
                    .or(Mono.justOrEmpty(url))
                    .map(finalUrl -> Tuples.of(part, finalUrl));
        } else {
            return Mono.error(new UnknownFileType(upload));
        }
    }

    private boolean isImage(FilePart filePart) {
        MediaType type = filePart.headers().getContentType();
        if(type != null) {
            return type.getType().equalsIgnoreCase("image");
        }
        return false;
    }
}
