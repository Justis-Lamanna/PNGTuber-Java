package com.github.milomarten.pngtuber.service;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.channel.VoiceChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VoiceConnectService {
    @Autowired
    private GatewayDiscordClient client;

    public Flux<Boolean> watchConnection(Snowflake user, Snowflake channel) {
        return Flux.concat(isConnected(user, channel), watchSubsequentConnections(user, channel));
    }

    protected Mono<Boolean> isConnected(Snowflake user, Snowflake channel) {
        return client.getChannelById(channel)
                .cast(VoiceChannel.class)
                .flatMapMany(VoiceChannel::getVoiceStates)
                .filter(vc -> vc.getUserId().equals(user))
                .next()
                .map(vs -> true)
                .defaultIfEmpty(false);
    }

    protected Flux<Boolean> watchSubsequentConnections(Snowflake user, Snowflake channel) {
        return client.on(VoiceStateUpdateEvent.class)
                .filter(event -> event.getCurrent().getUserId().equals(user))
                .mapNotNull(event -> {
                    if((event.isLeaveEvent() || event.isMoveEvent()) && leftChannel(event, channel)) {
                        return false;
                    } else if((event.isJoinEvent() || event.isMoveEvent()) && joinedChannel(event, channel)) {
                        return true;
                    } else {
                        return null;
                    }
                })
                .distinctUntilChanged();
    }

    protected static boolean leftChannel(VoiceStateUpdateEvent event, Snowflake channel) {
        return event.getOld()
                .flatMap(VoiceState::getChannelId)
                .map(s -> s.equals(channel))
                .orElse(false);
    }

    protected static boolean joinedChannel(VoiceStateUpdateEvent event, Snowflake channel) {
        return event.getCurrent().getChannelId()
                .map(s -> s.equals(channel))
                .orElse(false);
    }
}
