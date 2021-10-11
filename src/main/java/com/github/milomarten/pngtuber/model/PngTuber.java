package com.github.milomarten.pngtuber.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class PngTuber {
    @Id
    private final long id;

    private final String notConnectedUrl;
    private final String idleUrl;
    private final String speakingUrl;
}
