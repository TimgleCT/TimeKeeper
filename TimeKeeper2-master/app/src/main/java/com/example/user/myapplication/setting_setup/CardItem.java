package com.example.user.myapplication.setting_setup;


public class CardItem {
    String text, title, type;

    public CardItem(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
