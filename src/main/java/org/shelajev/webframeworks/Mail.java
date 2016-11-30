package org.shelajev.webframeworks;

import java.io.Serializable;

public class Mail implements Serializable {

    public String Id="Mail"+ System.currentTimeMillis();

    public String body;
    public String subject;
    public String from;
    public String to;
    public String file="mail.html";

    public Long dtSend=null;

    public Mail() {
    }

    public Long getDtSend() {
        return dtSend;
    }

    public void setDtSend(Long dtSend) {
        this.dtSend = dtSend;
    }

    public Mail(String body, String subject, String from, String to) {
        this.body = body;
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.setId(this.getTo()+ System.currentTimeMillis());
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getId() {
        return Id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setId(String id) {
        Id = id;
    }
}
