package eu.miaplatform.customplugin.springboot.models;

import java.io.Serializable;

public class Message implements Serializable {
    private String who;
    private String mymsg;

    public Message(String mymsg, String who) {
        this.who = who;
        this.mymsg = mymsg;
    }

    public Message(String mymsg) {
        this.mymsg = mymsg;
    }

    public String getWho() {
        return who;
    }

    public String getMymsg() {
        return mymsg;
    }
}
