package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    public void initializeServer(int port) {

        try (
            ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
        private static void handleClient(Socket clientSocket) throws IOException {
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true);

            // Читаем запрос
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                return;
            }
            System.out.println("Request: " + requestLine);

            // Парсим метод и путь
            String[] parts = requestLine.split(" ");
            if (parts.length < 2) {
                sendResponse(writer, 400, "Bad Request");
                return;
            }

            String method = parts[0];
            String path = parts[1];

            // Обработка запросов
            if (method.equals("GET")) {
                handleGet(path, writer);
            } else if (method.equals("POST")) {
                handlePost(reader, writer);
            } else {
                sendResponse(writer, 405, "Method Not Allowed");
            }

            clientSocket.close();
        }

        private static void handleGet(String path, PrintWriter writer) {
            String response = "You requested GET " + path;
            sendResponse(writer, 200, response);
        }

        private static void handlePost(BufferedReader reader, PrintWriter writer) throws IOException {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                body.append(line).append("\n");
            }

            String response = "You sent POST with body:\n" + body;
            sendResponse(writer, 200, response);
        }

        private static void sendResponse(PrintWriter writer, int statusCode, String response) {
            writer.println("HTTP/1.1 " + statusCode + " OK");
            writer.println("Content-Type: text/plain; charset=UTF-8");
            writer.println("Content-Length: " + response.getBytes(StandardCharsets.UTF_8).length);
            writer.println();
            writer.print(response);
        }


}
