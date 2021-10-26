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
        return pngTuberRepository.findBySnowflake(user.asString())
                .switchIfEmpty(getDefaultPngTuber(user));
    }

    public Mono<PngTuber> getPngTuber(Snowflake user, String variant) {
        return pngTuberRepository.findBySnowflakeAndVariant(user.asString(), variant)
                .switchIfEmpty(getDefaultPngTuber(user));
    }

    public Mono<PngTuber> savePngTuber(PngTuber pngTuber) {
        if(pngTuber.getVariant() == null) {
            return pngTuberRepository.findBySnowflake(pngTuber.getSnowflake())
                    .map(old -> {
                        pngTuber.setId(old.getId());
                        return pngTuber;
                    })
                    .defaultIfEmpty(pngTuber)
                    .flatMap(p -> pngTuberRepository.save(p));
        } else {
            return pngTuberRepository.findBySnowflakeAndVariant(pngTuber.getSnowflake(), pngTuber.getVariant())
                    .map(old -> {
                        pngTuber.setId(old.getId());
                        return pngTuber;
                    })
                    .defaultIfEmpty(pngTuber)
                    .flatMap(p -> pngTuberRepository.save(p));
        }
    }

    private Mono<PngTuber> getDefaultPngTuber(Snowflake user) {
        return client.getUserById(user)
                .map(User::getAvatarUrl)
                .map(s -> new PngTuber(null, user.asString(), null, null, s, s));
    }
}
