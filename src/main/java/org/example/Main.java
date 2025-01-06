package org.example;

import org.example.repository.WordRepository;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static final   int PORT = 8085;

    public static void main(String[] args) throws IOException {
        WordRepository.init();
Translator translator = new Translator();
new Thread(translator).start();
Server server = new Server();
server.initializeServer(PORT);
        openChrome();
    }



    public static void   openChrome() {
            String url = "http://localhost:8085/index.html";

            // Попробуем использовать Desktop API
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                    System.err.println("Failed to open URL: " + e.getMessage());
                }
            } else {
                // Альтернативный вариант через Runtime (если Desktop не поддерживается)
                try {
                    String os = System.getProperty("os.name").toLowerCase();
                    if (os.contains("win")) {
                        Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome " + url});
                    } else if (os.contains("mac")) {
                        Runtime.getRuntime().exec(new String[]{"open", "-a", "Google Chrome", url});
                    } else if (os.contains("nix") || os.contains("nux")) {
                        Runtime.getRuntime().exec(new String[]{"google-chrome", url});
                    }
                } catch (IOException e) {
                    System.err.println("Failed to open URL with Runtime: " + e.getMessage());
                }
            }
        }


}