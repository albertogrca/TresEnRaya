package com.gualo.blackjack.jsonMessages;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;

/**
 * Created by Gualo on 11/4/15.
 */
public class BlackJackBoardMessage extends JSONMessage{

    @JSONable
    private String cartas;
    ////Aqui faltaria la lista de cartas
    @JSONable
    private String player1;
    @JSONable
    private String player2;
    @JSONable
    private String userWithTurn;

    public BlackJackBoardMessage(String board) throws JSONException {
        super(false);
        StringTokenizer st=new StringTokenizer(board, "#");
        ///las cartas que llevemos
        this.cartas=st.nextToken();
        this.player1=st.nextToken();
        if (st.hasMoreTokens()) {
            this.player2=st.nextToken();
            userWithTurn=st.nextToken();
        }
    }

    public BlackJackBoardMessage(JSONObject jso) throws JSONException {
        super(false);
        ///las cartas que llevemos
        this.cartas=jso.getString("cartas");
        this.player1=jso.getString("player1");
        if (jso.optString("player2").length()>0) {
            this.player2=jso.getString("player2");
            this.userWithTurn=jso.getString("userWithTurn");
        }
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getUserWithTurn() {
        return userWithTurn;
    }
    public String getCartas() {
        return cartas;
    }
}
