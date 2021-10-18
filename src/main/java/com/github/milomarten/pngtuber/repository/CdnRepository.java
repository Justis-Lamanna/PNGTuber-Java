package com.github.milomarten.pngtuber.repository;

import com.github.milomarten.pngtuber.model.CdnImage;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CdnRepository extends R2dbcRepository<CdnImage, Long> {
}
