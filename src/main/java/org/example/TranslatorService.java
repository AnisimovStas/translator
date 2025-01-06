package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Word;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static org.example.Main.PORT;

public class TranslatorService {

    public static final String SOURCE_LANGUAGE = "en";
    public static final String TARGET_LANGUAGE = "ru";
    private static final String TRANSLATE_URL =  "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s";


    public void translate(String text) {
        String preparedText = text.replaceAll(" ", "%20");
        String translatedText = translateRequest(preparedText);
        Word translatedWord = new Word(text, translatedText, false);
        notifyClient(translatedWord);
    }

    private String translateRequest(String text) {
        HttpClient client = HttpClient.newHttpClient();

        try {
            // Подготавливаем запрос
            String translateUrl = String.format(TRANSLATE_URL, SOURCE_LANGUAGE, TARGET_LANGUAGE, text);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(translateUrl))
                .GET()
                .build();

            // Отправляем запрос и получаем ответ
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String translatedText = response.body().split("\"")[1];
            return translatedText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void notifyClient(Word word) {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> data = new HashMap<>();
        data.put("translatedText", word.getTranslatedText());
        data.put("originalText", word.getOriginalText());
        data.put("isShown", String.valueOf(word.isShown()));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(data);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:"+PORT+"/translate/new"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

