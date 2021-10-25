package com.github.milomarten.pngtuber;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.shard.GatewayBootstrap;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import io.r2dbc.spi.ConnectionFactories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class Config {
    @Bean
    public GatewayDiscordClient client(@Value("${token}") String token) {
        if(token == null) {
            System.out.println("Null Token");
            throw new NullPointerException();
        } else {
            System.out.println("Token part: " + token.substring(0, 5));
        }
        return GatewayBootstrap.create(DiscordClient.create(token))
                .setEnabledIntents(IntentSet.nonPrivileged().and(IntentSet.of(Intent.GUILD_PRESENCES, Intent.GUILD_VOICE_STATES)))
                .login()
                .block();
    }

    @Bean
    public ConnectionFactoryInitializer database(@Value("${db.url}") String url) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(ConnectionFactories.get(url));

        return initializer;
    }
}
