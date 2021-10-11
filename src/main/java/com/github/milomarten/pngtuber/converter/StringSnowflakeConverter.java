package com.github.milomarten.pngtuber.converter;

import discord4j.common.util.Snowflake;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringSnowflakeConverter implements Converter<String, Snowflake> {
    @Override
    public Snowflake convert(String source) {
        return Snowflake.of(source);
    }
}
