package com.github.milomarten.pngtuber.model;

import org.springframework.http.codec.multipart.FilePart;

public class UnknownFileType extends RuntimeException {
    public UnknownFileType(FilePart file) {
        super("Unknown File Type for file " + file.filename());
    }
}
