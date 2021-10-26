package com.github.milomarten.pngtuber.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import reactor.util.function.Tuple2;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PngTuber {
    @Id
    private String id;

    private String snowflake;
    private String variant;
    private String notConnectedUrl;
    private String idleUrl;
    private String speakingUrl;

    public PngTuber(String snowflake, String variant) {
        this.snowflake = snowflake;
        this.variant = variant;
    }

    private void set(Tuple2<PngTuberPart, String> part) {
        switch (part.getT1()) {
            case IDLE -> this.idleUrl = part.getT2();
            case OFFLINE -> this.notConnectedUrl = part.getT2();
            case SPEAKING -> this.speakingUrl = part.getT2();
        }
    }

    public PngTuber merge(PngTuber other) {
        PngTuber nuu = new PngTuber(getSnowflake(), getVariant());
        nuu.notConnectedUrl = Optional.of(notConnectedUrl).orElse(other.notConnectedUrl);
        nuu.idleUrl = Optional.of(idleUrl).orElse(other.idleUrl);
        nuu.speakingUrl = Optional.of(speakingUrl).orElse(other.speakingUrl);
        return nuu;
    }

    /// Construction Helper
    @RequiredArgsConstructor
    public static class Collector implements java.util.stream.Collector<Tuple2<PngTuberPart, String>, PngTuber, PngTuber> {
        private final String snowflake;
        private final String variant;

        @Override
        public Supplier<PngTuber> supplier() {
            return () -> new PngTuber(snowflake, variant);
        }

        @Override
        public BiConsumer<PngTuber, Tuple2<PngTuberPart, String>> accumulator() {
            return PngTuber::set;
        }

        @Override
        public BinaryOperator<PngTuber> combiner() {
            return PngTuber::merge;
        }

        @Override
        public Function<PngTuber, PngTuber> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.CONCURRENT, Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
        }
    }
}
