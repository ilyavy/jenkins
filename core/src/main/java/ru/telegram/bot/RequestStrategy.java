package ru.telegram.bot;

import java.util.Map;

public interface RequestStrategy {
    
    public void sendMessage(
            String url, Map<String, String> params,
            String fileFieldName, String fileName);
}
