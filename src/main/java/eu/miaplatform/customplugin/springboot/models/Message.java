package eu.miaplatform.customplugin.springboot.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Message implements Serializable {
    private String who;
    private String mymsg;

    public Message(String who, String mymsg) {
        this.who = who;
        this.mymsg = mymsg;
    }

    public Message(String mymsg) {
        this.mymsg = mymsg;
    }
}
