package com.github.milomarten.pngtuber;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.awt.desktop.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class BrowserStartupConfig {
    @Bean
    @ConditionalOnProperty("startup.uri")
    public ApplicationListener<ApplicationReadyEvent> openOnBrowser(@Value("${startup.uri}") String uri) {
        return event -> {
            System.out.println("Launching Browser...");
            browse(uri);
        };
    }

    private static void browse(String url) {
        if(Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        } else {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
