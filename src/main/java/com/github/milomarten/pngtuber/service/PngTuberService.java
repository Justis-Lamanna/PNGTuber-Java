package com.github.milomarten.pngtuber.service;

import com.github.milomarten.pngtuber.model.PngTuber;
import com.github.milomarten.pngtuber.repository.PngTuberRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PngTuberService {
    @Autowired
    private PngTuberRepository pngTuberRepository;

    @Autowired
    private GatewayDiscordClient client;

    public Mono<PngTuber> getPngTuber(Snowflake user) {
        return pngTuberRepository.findById(user.asLong())
                .switchIfEmpty(getDefaultPngTuber(user));
    }

    private Mono<PngTuber> getDefaultPngTuber(Snowflake user) {
        return client.getUserById(user)
                .map(User::getAvatarUrl)
                .map(s -> new PngTuber(user.asLong(), null, s, s));
    }
}
