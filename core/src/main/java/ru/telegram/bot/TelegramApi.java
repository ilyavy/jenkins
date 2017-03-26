package ru.telegram.bot;

import jenkins.model.Jenkins;

import java.util.HashMap;
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
    public void sendMessage(final String message) throws Exception {
        String fileFieldKeyName = null;
        String fileName = null;
        Map<String, String> apiSpec = new HashMap<>();
        
        if (message.getBytes("UTF-8").length < 1000) {
            apiSpec.put("methodName", "sendMessage");
            apiSpec.put("paramName", "text");
        } else {
            apiSpec.put("methodName", "sendDocument");
            apiSpec.put("paramName", "document");
            fileName = "report.txt";
            fileFieldKeyName = "document";жж
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.telegram.org/bot");
        sb.append(j.getTelegramBotID());
        sb.append(":");
        sb.append(j.getTelegramBotToken());
        sb.append("/" + apiSpec.get("methodName"));

        Map<String, String> params = new LinkedHashMap<>();
        params.put("chat_id", j.getTelegramGroupID());
        params.put(apiSpec.get("paramName"), message);

        String url = sb.toString();
        LOGGER.info("TelegramAPI > message will be sent using url: " + url);
        
        requestStrategy.sendMessage(url, params, fileFieldKeyName, fileName);
    }
}
