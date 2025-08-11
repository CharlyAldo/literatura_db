package com.literalura.literatura_console.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.literalura.literatura_console.dto.GutendexBookDTO;
import com.literalura.literatura_console.dto.GutendexResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class GutendexClient {
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Optional<GutendexBookDTO> buscarPorTitulo(String titulo) throws IOException, InterruptedException {
        String q = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        String url = "https://gutendex.com/books/?search=" + q;
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) return Optional.empty();
        GutendexResponse gr = mapper.readValue(resp.body(), GutendexResponse.class);
        if (gr.getResults() == null || gr.getResults().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(gr.getResults().get(0));
    }
}

