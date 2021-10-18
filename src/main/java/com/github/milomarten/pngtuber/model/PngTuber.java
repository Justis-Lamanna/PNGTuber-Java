package com.github.milomarten.pngtuber.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import reactor.util.function.Tuple2;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
@NoArgsConstructor
public class PngTuber implements Persistable<String> {
    @Id
    private String id;

    private String notConnectedUrl;
    private String idleUrl;
    private String speakingUrl;

    private PngTuber(String id) {
        this.id = id;
    }

    public PngTuber(String id, String notConnectedUrl, String idleUrl, String speakingUrl) {
        this.id = id;
        this.notConnectedUrl = notConnectedUrl;
        this.idleUrl = idleUrl;
        this.speakingUrl = speakingUrl;
    }

    private void set(Tuple2<PngTuberPart, String> part) {
        switch (part.getT1()) {
            case IDLE: this.idleUrl = part.getT2(); break;
            case OFFLINE: this.notConnectedUrl = part.getT2(); break;
            case SPEAKING: this.speakingUrl = part.getT2(); break;
        }
    }

    public PngTuber merge(PngTuber other) {
        PngTuber nuu = new PngTuber(this.getId());
        nuu.notConnectedUrl = Optional.of(notConnectedUrl).orElse(other.notConnectedUrl);
        nuu.idleUrl = Optional.of(idleUrl).orElse(other.idleUrl);
        nuu.speakingUrl = Optional.of(speakingUrl).orElse(other.speakingUrl);
        return nuu;
    }

    ///// Database Helper
    @Transient
    @JsonIgnore
    private boolean _new;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return _new;
    }

    public PngTuber setNew(boolean _new) {
        this._new = _new;
        return this;
    }

    /// Construction Helper
    @RequiredArgsConstructor
    public static class Collector implements java.util.stream.Collector<Tuple2<PngTuberPart, String>, PngTuber, PngTuber> {
        private final String id;

        @Override
        public Supplier<PngTuber> supplier() {
            return () -> new PngTuber(id);
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
