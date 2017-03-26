package ru.telegram.bot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Builds POST request to the specified address
 * with the specified parameters. Use methods addField()
 * and addFile() to create the request, then method response()
 * to return the server's response.
 * 
 * Content-Type: multipart/form-data
 * 
 * About implementation.
 * By POST specification each field should be separated by the
 * following scheme (<- -> indicates variables):
 * <-boundary, should start with two dashes->
 * Content-Disposition: form-data; name="<-field's name->"
 * <-empty string->
 * <-field's value->
 * 
 * The end of data should be marked with boundary plus two
 * dashes and a new string.
 */
public class PostRequestBuilder implements RequestBuilder {
    
    /** URL connection */
    private HttpsURLConnection conn;
    
    /** Output stream for parameters */
    private DataOutputStream request;
    
    /** A separator between fields, 
     * should start with two dashes */
    private final String BOUNDARY =  "--***";
    
    /** The symbol of a new line */
    private final String CRLF = "\r\n";
    
    
    /**
     * Creates PostRequestBuilder
     * @param requestUrl    - base url for the request
     * @throws IOException
     */
    public PostRequestBuilder(final String requestUrl) 
            throws IOException {
        
        URL url = new URL(requestUrl);
        conn = (HttpsURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty(
            "Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

        request =  new DataOutputStream(conn.getOutputStream());
    }
    
    
    @Override
    public void addField(final String name, final String value)
            throws IOException {
        
        request.writeBytes(BOUNDARY + CRLF);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                name + "\"" + CRLF);
        request.writeBytes("Content-Type: text/plain; charset=UTF-8" + CRLF);
        request.writeBytes(CRLF);
        request.writeBytes(value + CRLF);
        request.flush();
    }

    
    @Override
    public void addFile(final String fieldName, final String fileName,
            final String fileContent) throws IOException {

        request.writeBytes(BOUNDARY + CRLF);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                fieldName + "\";filename=\"" + fileName + "\"" + CRLF);
        request.writeBytes(CRLF);
        byte[] bytes = fileContent.getBytes("UTF-8");
        request.write(bytes);
        //request.flush();
    }

    
    @Override
    public String response() throws IOException {
        String response = "";

        request.writeBytes(CRLF);
        request.writeBytes(BOUNDARY + "--" + CRLF);
        request.flush();
        request.close();
        
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

        return response;
    }
}
