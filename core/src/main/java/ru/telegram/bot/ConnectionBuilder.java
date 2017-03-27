package ru.telegram.bot;

import java.io.IOException;
import javax.net.ssl.HttpsURLConnection;

/**
 * RequestBuilder interface. Method response() should implement
 * connection to a server by the specified in constructor
 * request url and return the server's response.
 */
public interface ConnectionBuilder {
    
    /**
     * Adds field to the connection
     * @param name  -   the name of the field
     * @param value -   the value of the field
     * @throws IOException
     */
    public void addField(String name, String value)
            throws IOException;
    
    /**
     * Adds file to the connection
     * @param fieldName -   the name of the field
     * @param fileName  -   the name of the file
     * @param fileContent   - the string, which represents
     * the content of the file  
     * @throws IOException
     */
    public void addFile(String fieldName, String fileName, 
            String fileContent) throws IOException;
    
    /**
     * Returns the created connection
     * @return
     * @throws IOException
     */
    public HttpsURLConnection getConnection() throws IOException;
}
