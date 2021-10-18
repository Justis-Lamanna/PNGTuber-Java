package com.github.milomarten.pngtuber.service;

import com.github.milomarten.pngtuber.model.CdnImage;
import com.github.milomarten.pngtuber.model.UnknownFileType;
import com.github.milomarten.pngtuber.repository.CdnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CdnService {
    @Autowired
    private CdnRepository repository;

    public Mono<Long> upload(FilePart file) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        return DataBufferUtils.write(file.content(), os)
                .then(Mono.justOrEmpty(getType(file)))
                .switchIfEmpty(Mono.error(new UnknownFileType(file)))
                .map(mime -> new CdnImage(null, mime, os.toByteArray()))
                .flatMap(repository::save)
                .map(CdnImage::getId);
    }

    public Mono<CdnImage> get(long id) {
        return repository.findById(id);
    }

    private Optional<String> getType(FilePart file) {
        return Optional.ofNullable(file.headers().getContentType())
                .map(MediaType::toString);
    }
}
