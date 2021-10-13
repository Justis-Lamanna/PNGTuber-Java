package com.github.milomarten.pngtuber;

import com.github.milomarten.pngtuber.model.PngTuber;
import com.github.milomarten.pngtuber.service.DiscordService;
import com.github.milomarten.pngtuber.service.PngTuberService;
import discord4j.common.util.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class Endpoints {
    @Autowired
    private DiscordService discordService;

    @Autowired
    private PngTuberService pngTuberService;

    @Value("${channel.default}")
    private Snowflake defaultChannel;

    @GetMapping("pngtuber/{user}/name")
    public Mono<String> username(@PathVariable Snowflake user) {
        return discordService.getUsername(user, defaultChannel);
    }

    @GetMapping("pngtuber/{user}/urls")
    public Mono<PngTuber> pngTuber(@PathVariable Snowflake user) {
        return pngTuberService.getPngTuber(user);
    }

    @PostMapping(value = "pngtuber/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> test(@RequestPart("id") String id,
                             @RequestPart(value = "idle", required = false) FilePart idle,
                             @RequestPart(value = "offline", required = false) FilePart offline,
                             @RequestPart(value = "speaking", required = false) FilePart speaking,
                             @RequestPart(value = "idleUrl", required = false) String idleUrl,
                             @RequestPart(value = "offlineUrl", required = false) String offlineUrl,
                             @RequestPart(value = "speakingUrl", required = false) String speakingUrl) {
        return Mono.just("Thanks");
    }
}
