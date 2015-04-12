package com.gualo.blackjack.jsonMessages;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;

/**
 * Created by Gualo on 11/4/15.
 */
public class BlackJackWaitingMessage extends JSONMessage{
    @JSONable
    private String text;

    public BlackJackWaitingMessage(String text) {
        super(false);
        this.text=text;
    }

    public String getText() {
        return this.text;
    }
}
