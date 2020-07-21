package eu.miaplatform.customplugin.springboot.models;

public class Message {
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
