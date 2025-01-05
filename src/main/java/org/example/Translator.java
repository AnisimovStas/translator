package org.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Translator implements NativeKeyListener {

    private LinkedList<String> latestPressedKeys = new LinkedList<>();
    private static final List<String> COMMAND = List.of("shift","ctrl","c");

    public void nativeKeyPressed(NativeKeyEvent e) {
        String pressedKey = NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
        if(latestPressedKeys.size() == COMMAND.size()) {
            latestPressedKeys.removeFirst();
        }
            latestPressedKeys.add(pressedKey);

        if(latestPressedKeys.containsAll(COMMAND)) {
           String clipboardText = getClipboardText();
           if(clipboardText != null) {
               System.out.println(clipboardText);
               latestPressedKeys.clear();
           }
        }
    }


    private String getClipboardText() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // Получение содержимого буфера обмена
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

    public void start() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
            return;
        }
        GlobalScreen.addNativeKeyListener(this);
    }


}
