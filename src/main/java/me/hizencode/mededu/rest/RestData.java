package me.hizencode.mededu.rest;

import me.hizencode.mededu.rest.photos.PhotoData;
import me.hizencode.mededu.rest.text.TextData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class RestData {

    private final WebClient webTextClient;

    private final WebClient webPictureClient;

    public RestData(WebClient.Builder webClientBuilder, @Value("${pexels.key}") String key) {
        this.webTextClient = webClientBuilder.baseUrl("http://asdfast.beobit.net").build();
        this.webPictureClient = webClientBuilder.baseUrl("https://api.pexels.com")
                .defaultHeader("Authorization", key)
                .build();
    }

    public Mono<PhotoData> getImages(int perPage, int page) {
        return this.webPictureClient.get().uri("/v1/search?query=medical&per_page={perPage}&page={page}", perPage, page)
                .retrieve().bodyToMono(PhotoData.class);
    }

    public Mono<TextData> getTextWithWords(int length) {
        return this.webTextClient.get().uri("/api/?type=word&length={length}", length)
                .retrieve().bodyToMono(TextData.class);
    }

    public Mono<TextData> getTextWithParagraphs(int length) {
        return this.webTextClient.get().uri("/api/?type=paragraph&length={length}", length)
                .retrieve().bodyToMono(TextData.class);
    }

    public int randomLength(int min, int max) {
        return (int) (min + Math.random() * (max - min));
    }

    public String getTextWords(int min, int max) {
        Optional<TextData> textDataOptional = getTextWithWords(randomLength(min, max)).blockOptional();

        return textDataOptional.map(TextData::getText).orElseThrow(IllegalStateException::new);
    }

    public String getTextParagraphs(int min, int max) {
        Optional<TextData> textDataOptional = getTextWithParagraphs(randomLength(min, max)).blockOptional();

        return textDataOptional.map(TextData::getText).orElseThrow(IllegalStateException::new);
    }
}
