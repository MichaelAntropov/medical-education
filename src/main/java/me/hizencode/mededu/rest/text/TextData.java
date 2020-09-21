package me.hizencode.mededu.rest.text;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TextData {

    private String text;

    public TextData() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TextData{" +
                "text='" + text + '\'' +
                '}';
    }
}
