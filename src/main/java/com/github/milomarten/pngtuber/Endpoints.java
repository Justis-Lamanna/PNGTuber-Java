package com.github.milomarten.pngtuber;

import com.github.milomarten.pngtuber.service.DiscordService;
import com.github.milomarten.pngtuber.service.VoiceConnectService;
import com.github.milomarten.pngtuber.service.VoiceSpeakService;
import discord4j.common.util.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class Endpoints {
    @Autowired
    private DiscordService discordService;

    @Value("${channel.default}")
    private Snowflake defaultChannel;

    @GetMapping("discord/{user}/name")
    public Mono<String> username(@PathVariable Snowflake user) {
        return discordService.getUsername(user, defaultChannel);
    }
}
