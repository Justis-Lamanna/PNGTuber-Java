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

@Configuration
public class Config {
    @Bean
    public GatewayDiscordClient client(@Value("${token}") String token) {
        return GatewayBootstrap.create(DiscordClient.create(token))
                .setEnabledIntents(IntentSet.nonPrivileged().and(IntentSet.of(Intent.GUILD_PRESENCES)))
                .login()
                .block();
    }

    @Bean
    public ConnectionFactoryInitializer database(@Value("${db.url}") String url) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(ConnectionFactories.get(url));

//        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
//        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
//        initializer.setDatabasePopulator(populator);

        return initializer;
    }
}
