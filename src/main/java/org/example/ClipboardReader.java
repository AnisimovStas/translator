package org.example;

import java.awt.*;
import java.awt.datatransfer.*;

public class ClipboardReader {
    public static void main(String[] args) {
        try {
            // Получение буфера обмена
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            // Получение содержимого буфера обмена
            Transferable contents = clipboard.getContents(null);

            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String) contents.getTransferData(DataFlavor.stringFlavor);
                System.out.println("Выделенный текст из буфера обмена: " + text);
            } else {
                System.out.println("Буфер обмена не содержит текстовых данных.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
