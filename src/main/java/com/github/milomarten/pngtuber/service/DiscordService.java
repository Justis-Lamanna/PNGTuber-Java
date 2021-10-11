package com.github.milomarten.pngtuber.service;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.PartialMember;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.rest.http.client.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DiscordService {
    @Autowired
    private GatewayDiscordClient client;

    public Mono<String> getUsername(Snowflake user, Snowflake channel) {
        return client.getChannelById(channel)
                .cast(VoiceChannel.class)
                .flatMap(vc -> vc.getGuild())
                .flatMap(g -> g.getMemberById(user))
                .map(PartialMember::getDisplayName)
                .onErrorResume(ClientException.class, ce -> client.getUserById(user)
                        .map(User::getUsername));
    }
}
