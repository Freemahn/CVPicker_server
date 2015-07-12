package com.freemahn;

import java.util.ArrayList;

/**
 * Created by freeemahn on 12.07.15.
 */
public class User {
    String id;
    Integer result;
    ArrayList<Attachment> attachments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public User() {

    }

    public User(String id, int result, ArrayList<Attachment> attachments) {
        this.id = id;
        this.result = result;
        this.attachments = attachments;
    }


}
