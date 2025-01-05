package org.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;

public class Translator implements NativeKeyListener, Runnable {

    private LinkedList<String> latestPressedKeys = new LinkedList<>();
    private static final List<String> COMMAND = List.of("shift","ctrl","c");
private final TranslatorService translatorService = new TranslatorService();


    public void nativeKeyPressed(NativeKeyEvent e) {
        String pressedKey = NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
        if(latestPressedKeys.size() == COMMAND.size()) {
            latestPressedKeys.removeFirst();
        }
            latestPressedKeys.add(pressedKey);

        if(latestPressedKeys.containsAll(COMMAND)) {
           String clipboardText = getClipboardText();
           if(clipboardText != null) {
               translatorService.translate(clipboardText);
               latestPressedKeys.clear();
           }}   }



    private String getClipboardText() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            String text = null;
            try {
                text = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        }
        return null;
    }

    @Override
    public void run() {
        try {
            GlobalScreen.registerNativeHook();
            System.out.println("Ready to accept words to translate!");
        } catch (NativeHookException e) {
            e.printStackTrace();
            return;
        }
        GlobalScreen.addNativeKeyListener(this);
    }
}
