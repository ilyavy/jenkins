package ru.telegram.bot;

import java.util.Map;

public interface RequestStrategy {
    
    /**
     * Sends the request
     * @param url - the link (the address) for the request
     * @param params - the parameters to pass to the request
     * @param fileFieldKeyName - specify null, if no files should
     * be transfered. Else, it should be the name of the key with
     * the file in the params map
     * @param fileName - the name of the file, with which it should
     * be transfered
     */
    public void sendMessage(
            String url, Map<String, String> params,
            String fileFieldKeyName, String fileName);
}
