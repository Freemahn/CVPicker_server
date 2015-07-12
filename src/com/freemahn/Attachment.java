package com.freemahn;

/**
 * Created by freeemahn on 12.07.15.
 */
public class Attachment {
    String url;
    String key;

    public Attachment() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Attachment(String url, String key) {
        this.url = url;
        this.key = key;

    }
}
