package com.github.milomarten.pngtuber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CdnImage {
    @Id
    private Long id;
    private String mimeType;
    private byte[] image;
}
