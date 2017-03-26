package ru.telegram.bot;

import jenkins.model.Jenkins;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * API for telegram notifications
 */
public class TelegramApi {

    private static final Logger LOGGER = 
            Logger.getLogger(TelegramApi.class.getName());

    /**
     * The strategy for handling requests
     */
    private final RequestStrategy requestStrategy;
    
    /**
     * Jenkins instance used to get bot id, token and group_id
     */
    private final Jenkins j;

    /**
     * Constructor
     */
    public TelegramApi(final RequestStrategy requestStrategy) {
        this.requestStrategy = requestStrategy;
        j = Jenkins.getInstance();
    }
    
    /**
     * Sends the message to the group in behalf of the bot
     * @param message
     * @return - void
     * @throws IOException 
     */
    public void sendMessage(final String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.telegram.org/bot");
        sb.append(j.getTelegramBotID());
        sb.append(":");
        sb.append(j.getTelegramBotToken());
        sb.append("/sendMessage");

        Map<String, String> params = new LinkedHashMap<>();
        params.put("chat_id", j.getTelegramGroupID());
        params.put("text", message);

        String url = sb.toString();
        LOGGER.info("TelegramAPI > message will be sent using url: " + url);
        
        requestStrategy.sendMessage(url, params, null, null);
    }
}
