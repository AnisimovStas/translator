package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Word {
private  Integer id;
private  String originalText;
private  String translatedText;
    @JsonProperty("isShown")
private  Boolean isShown;

    public Word() {
    }

    public Word( String originalText, String translatedText, Boolean isShown) {
        this.originalText = originalText;
        this.translatedText = translatedText;
        this.isShown = isShown;
    }

    public String getOriginalText() {
        return originalText;
    }

    @Override
    public String toString() {
        return "Word{" +
            "id=" + id +
            ", originalText='" + originalText + '\'' +
            ", translatedText='" + translatedText + '\'' +
            ", isShown=" + isShown +
            '}';
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public Boolean isShown() {
        return isShown;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public void setShown(Boolean shown) {
        isShown = shown;
    }
}
