package ru.telegram.bot;

import java.util.Map;

public class TestRequestStrategy implements RequestStrategy {
    private Map<String, String> params;
    private String url;
    private String fileFieldName;
    private String fileName;

    public Map<String, String> getParams() {
        return params;
    }

    public String getUrl() {
        return url;
    }

    public String getFileFieldName() {
        return fileFieldName;
    }

    public String getFileName() {
        return fileName;
    }
    
    
    @Override
    public void sendMessage(String url,
            Map<String, String> params,
            String fileFieldName, String fileName) {

        this.url = url;
        this.params = params;
        this.fileFieldName = fileFieldName;
        this.fileName = fileName;
    }
}
