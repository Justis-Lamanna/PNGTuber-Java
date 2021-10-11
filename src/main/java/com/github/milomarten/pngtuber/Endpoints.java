package com.github.milomarten.pngtuber;

import com.github.milomarten.pngtuber.model.PngTuber;
import com.github.milomarten.pngtuber.service.DiscordService;
import com.github.milomarten.pngtuber.service.PngTuberService;
import discord4j.common.util.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
}
