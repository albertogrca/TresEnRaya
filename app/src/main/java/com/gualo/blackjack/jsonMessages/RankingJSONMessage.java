package com.gualo.blackjack.jsonMessages;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;
/**
 * Created by Gualo on 4/5/15.
 */
public class RankingJSONMessage extends JSONMessage{
    @JSONable
    private String text;

    public RankingJSONMessage(String text) {
        super(false);
        this.text=text;
    }
    public String getText() {
        return this.text;
    }
}
