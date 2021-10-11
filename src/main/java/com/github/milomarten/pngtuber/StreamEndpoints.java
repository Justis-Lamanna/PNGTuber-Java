package com.github.milomarten.pngtuber;

import com.github.milomarten.pngtuber.service.DiscordService;
import com.github.milomarten.pngtuber.service.VoiceConnectService;
import com.github.milomarten.pngtuber.service.VoiceSpeakService;
import discord4j.common.util.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/streams")
public class StreamEndpoints {
    @Autowired
    private VoiceConnectService voiceConnectService;

    @Autowired
    private VoiceSpeakService voiceSpeakService;

    @Value("${channel.default}")
    private Snowflake defaultChannel;

    private final AtomicLong connectIdCount = new AtomicLong();
    private final AtomicLong speakIdCount = new AtomicLong();

    @GetMapping("voice/{user}/connect")
    public Flux<ServerSentEvent<Boolean>> voiceConnectState(@PathVariable Snowflake user) {
        return voiceConnectService.watchConnection(user, defaultChannel)
                .map(state -> ServerSentEvent.<Boolean>builder()
                        .id("connect-" + connectIdCount.getAndIncrement())
                        .data(state)
                        .event("connect")
                        .comment(user.asString())
                        .build());
    }

    @GetMapping("voice/{user}/speak")
    public Flux<ServerSentEvent<Boolean>> voiceState(@PathVariable Snowflake user) {
        return voiceSpeakService.watchSpeaking(user, defaultChannel)
                .map(state -> ServerSentEvent.<Boolean>builder()
                        .id("speak-" + speakIdCount.getAndIncrement())
                        .data(state)
                        .event("speak")
                        .comment(user.asString())
                        .build());
    }
}
