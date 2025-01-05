package org.example.model;

public class Word {
private final String originalText;
private final String translatedText;
private final Boolean isShown;

    public Word(String originalText, String translatedText, Boolean isShown) {
        this.originalText = originalText;
        this.translatedText = translatedText;
        this.isShown = isShown;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public Boolean getShown() {
        return isShown;
    }


}
