package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static org.example.Main.PORT;

public class TranslatorService {

    public void translate(String text) {

        String translatedText = text;
        notifyClient(translatedText);
    }

    private void notifyClient(String translatedText) {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> data = new HashMap<>();
        data.put("translatedText", translatedText);

        try {
            // Преобразуем Map в JSON строку
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(data);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:"+PORT+"/translate/new"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

