package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final String BASE_FILE_URL = "src/main/resources";

    private List<String> words = new ArrayList<>();

    public  void initializeServer(int port) {
        Javalin app = Javalin.create().start(port);

        app.get("/*", ctx -> {
            File file = new File(BASE_FILE_URL +"/frontend"+ ctx.path());
            if (file.exists()) {
                try {
                    byte[] fileBytes = Files.readAllBytes(file.toPath()); // Читаем содержимое файла в байты
                    ctx.result(fileBytes);  // Отдаем байтовый массив
                    if(file.getName().endsWith(".css")) {
                        ctx.contentType("text/css"); // Указываем тип содержимого, если это CSS файл
                    }
                    if(file.getName().endsWith(".js")) {
                        ctx.contentType("text/javascript"); // Указываем тип содержимого, если это JS файл
                    }
                    if(file.getName().endsWith(".html")) {
                        ctx.contentType("text/html"); // Указываем тип содержимого, если это HTML файл
                    }
                    if(file.getName().endsWith(".json")) {
                        ctx.contentType("application/json");
                    }
//                    ctx.contentType("text/html"); // Указываем тип содержимого, если это HTML файл
                } catch (IOException e) {
                    ctx.status(500).result("Error reading file");
                }
            } else {
                ctx.status(404).result("File not found");
            }
        });

        app.post("/translate/new", ctx -> {
            String requestBody = ctx.body();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // Преобразуем JSON строку в JsonNode
                JsonNode jsonNode = objectMapper.readTree(requestBody);

                // Извлекаем значение из поля "translatedText"
                String translatedText = jsonNode.get("translatedText").asText();
                words.add(translatedText);
                ctx.result(translatedText);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(400).result("Invalid JSON format");
            }
        });

        app.post("/words", ctx -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                //1. запрос в базу данных для получения непоказанных слов
//2. сделать эти слова покказанными
                //3. вернуть слова в формате json
                String json = objectMapper.writeValueAsString(words);
                ctx.result(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

}
