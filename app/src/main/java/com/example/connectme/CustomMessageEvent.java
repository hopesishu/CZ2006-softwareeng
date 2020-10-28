package com.example.connectme;

/**
 * Message to be pass through event bus
 */
public class CustomMessageEvent {
    private String customMessage;

    public CustomMessageEvent(String customMessage) {
        this.customMessage = customMessage;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    private void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }
}
