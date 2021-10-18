package com.github.milomarten.pngtuber.service;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VoiceSpeakService {
    @Autowired
    private GatewayDiscordClient client;

    public Flux<Boolean> watchSpeaking(Snowflake user, Snowflake channel) {
        return Flux.concat(Mono.just(false), Mono.never());
    }
}
