package ru.telegram.bot;

import jenkins.model.Jenkins;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * API for telegram notifications
 */
public class TelegramApi {

    private static final Logger LOGGER = Logger.getLogger(TelegramApi.class.getName());

    /**
     * Jenkins instance used to get bot id, token and group_id
     */
    private final Jenkins j;

    /**
     * Default constructor
     */
    public TelegramApi(){
        j = Jenkins.getInstance();
    }
    
    /**
     * Sends the message to the group in behalf of the bot
     * @param message
     * @return - String representation of the response (json)
     * @throws IOException 
     */
    public String sendMessage(final String message) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.telegram.org/bot");
        sb.append(j.getTelegramBotID());
        sb.append(":");
        sb.append(j.getTelegramBotToken());
        sb.append("/sendMessage");

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("chat_id", j.getTelegramGroupID());
        params.put("text", message);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        
        HttpsURLConnection urlConn = null;
        URL url;
        url = new URL(sb.toString());
        LOGGER.info("TelegramAPI > message will be sent using url: " + url);
        
        urlConn = (HttpsURLConnection) url.openConnection();
        urlConn.setRequestMethod("POST");
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        urlConn.setDoOutput(true);
        urlConn.getOutputStream().write(postDataBytes);
        urlConn.connect();
        
        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
