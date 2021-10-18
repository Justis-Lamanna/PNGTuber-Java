package com.github.milomarten.pngtuber.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import reactor.util.function.Tuple2;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PngTuber {
    @Id
    private long id;

    private String notConnectedUrl;
    private String idleUrl;
    private String speakingUrl;

    @RequiredArgsConstructor
    private static class SoftBuilder {
        private final long id;

        private String notConnectedUrl;
        private String idleUrl;
        private String speakingUrl;

        public SoftBuilder set(Tuple2<PngTuberPart, String> part) {
            switch (part.getT1()) {
                case IDLE: this.idleUrl = part.getT2(); break;
                case SPEAKING: this.speakingUrl = part.getT2(); break;
                case OFFLINE: this.notConnectedUrl = part.getT2(); break;
            }
            return this;
        }

        public SoftBuilder merge(SoftBuilder other) {
            if(other.notConnectedUrl != null) {
                this.notConnectedUrl = other.notConnectedUrl;
            }
            if(other.idleUrl != null) {
                this.idleUrl = other.idleUrl;
            }
            if(other.speakingUrl != null) {
                this.speakingUrl = other.speakingUrl;
            }
            return this;
        }

        public PngTuber build() {
            return new PngTuber(id, notConnectedUrl, idleUrl, speakingUrl);
        }
    }

    @RequiredArgsConstructor
    public static class Collector implements java.util.stream.Collector<Tuple2<PngTuberPart, String>, SoftBuilder, PngTuber> {
        private final long id;

        @Override
        public Supplier<SoftBuilder> supplier() {
            return () -> new SoftBuilder(id);
        }

        @Override
        public BiConsumer<SoftBuilder, Tuple2<PngTuberPart, String>> accumulator() {
            return SoftBuilder::set;
        }

        @Override
        public BinaryOperator<SoftBuilder> combiner() {
            return SoftBuilder::merge;
        }

        @Override
        public Function<SoftBuilder, PngTuber> finisher() {
            return SoftBuilder::build;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.CONCURRENT, Characteristics.UNORDERED);
        }
    }
}
