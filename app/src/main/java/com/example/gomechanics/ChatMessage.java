package com.example.gomechanics;

public class ChatMessage {

    public static final int TYPE_USER = 1;
    public static final int TYPE_BOT = 2;

    private String message;
    private int type;

    public ChatMessage(String message, int type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }
}