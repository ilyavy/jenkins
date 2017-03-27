package ru.telegram.bot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;


public class PostRequestStrategy implements RequestStrategy {
    
    @Override
    public void sendMessage(
            final String url, final Map<String, String> params,
            final String fileFieldKeyName, final String fileName) {
        
        try {
            ConnectionBuilder requestBuilder = new PostConnectionBuilder(url);
            for (Entry<String, String> entry : params.entrySet()) {
                if (!entry.getKey().equals(fileFieldKeyName)) {
                    requestBuilder.addField(
                            entry.getKey(), entry.getValue());
                } else {
                    requestBuilder.addFile(
                            entry.getKey(), fileName, entry.getValue());
                }
            }
            
            String response = null;
            HttpsURLConnection conn = requestBuilder.getConnection();
            conn.connect();
            
            int status = conn.getResponseCode();
            if (status == HttpsURLConnection.HTTP_OK) {
                InputStream responseStream = new
                        BufferedInputStream(conn.getInputStream());
                BufferedReader responseStreamReader =
                        new BufferedReader(new InputStreamReader(responseStream));

                String line = "";
                StringBuilder sb = new StringBuilder();

                while ((line = responseStreamReader.readLine()) != null) {
                    sb.append(line).append(System.lineSeparator());
                }
                responseStreamReader.close();

                response = sb.toString();
                conn.disconnect();
                
            } else {
                throw new IOException("Server returned error status: " + status);
            }

            System.out.println(response);
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
