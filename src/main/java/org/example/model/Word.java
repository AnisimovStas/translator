package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Word {
private  String originalText;
private  String translatedText;
    @JsonProperty("isShown")
private  Boolean isShown;

    public Word() {
    }

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

    public Boolean isShown() {
        return isShown;
    }


}
