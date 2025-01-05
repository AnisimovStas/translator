package org.example;

import java.io.IOException;

public class Main {
    public static final   int PORT = 8085;

    public static void main(String[] args) throws IOException {
Translator translator = new Translator();
new Thread(translator).start();
Server server = new Server();
server.initializeServer(PORT);

    }
}