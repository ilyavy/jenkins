package ru.telegram.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * API for telegram notifications
 */
public class TelegramApi {
    private final String botId;
    private final String token;
    private final String groupId;
    
    
    public String getBotId() {
        return botId;
    }

    public String getToken() {
        return token;
    }

    public String getGroupId() {
        return groupId;
    }
    
    /**
     * Constructor
     * @param botId - bot's id
     * @param token - bot's token
     * @param groupId - id of the group, which has the
     * specified bot as a member and where notifications
     * will be sent
     */
    public TelegramApi(final String botId,
            final String token, final String groupId) {
        this.botId = botId;
        this.token = token;
        this.groupId = groupId;
    }
    
    /**
     * Sends the message to the group in behalf of the bot
     * @param message
     * @return - String representation of the response (json)
     * @throws IOException 
     */
    public String sendMessage(String message) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.telegram.org/bot");
        sb.append(botId);
        sb.append(":");
        sb.append(token);
        sb.append("/sendMessage?");
        sb.append("chat_id=");
        sb.append(groupId);
        sb.append("&text=");
        sb.append(URLEncoder.encode(message, "UTF-8"));
        
        HttpsURLConnection urlConn = null;
        URL url;
        url = new URL(sb.toString());
        System.out.println(
                "TelegramAPI > message will be sent using url: " + url);
        
        urlConn = (HttpsURLConnection) url.openConnection();
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
