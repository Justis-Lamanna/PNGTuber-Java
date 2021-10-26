package com.github.milomarten.pngtuber.repository;

import com.github.milomarten.pngtuber.model.PngTuber;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PngTuberRepository extends R2dbcRepository<PngTuber, String> {
    Mono<PngTuber> findBySnowflake(String snowflake);
    Mono<PngTuber> findBySnowflakeAndVariant(String snowflake, String variant);
}
