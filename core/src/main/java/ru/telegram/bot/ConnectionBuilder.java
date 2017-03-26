package ru.telegram.bot;

import java.io.IOException;
import javax.net.ssl.HttpsURLConnection;

/**
 * RequestBuilder interface. Method response() should implement
 * connection to a server by the specified in constructor
 * request url and return the server's response.
 */
public interface ConnectionBuilder {
    
    public void addField(String name, String value)
            throws IOException;
    
    public void addFile(String fieldName, String fileName, 
            String fileContent) throws IOException;
    
    public HttpsURLConnection getConnection() throws IOException;
}
