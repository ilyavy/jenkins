package ru.telegram.bot;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class PostRequestStrategy implements RequestStrategy {
    
    @Override
    public void sendMessage(
            final String url, final Map<String, String> params,
            final String fileFieldName, final String fileName) {
        
        try {
            RequestBuilder requestBuilder = new PostRequestBuilder(url);
            for (Entry<String, String> entry : params.entrySet()) {
                if (!entry.getKey().equals(fileFieldName)) {
                    requestBuilder.addField(
                            entry.getKey(), entry.getValue());
                } else {
                    requestBuilder.addFile(
                            entry.getKey(), fileName, entry.getValue());
                }
            }
            String response = requestBuilder.response();
            System.out.println(response);
            
        } catch(IOException e) {
            e.printStackTrace();
        }
       
    }

}
