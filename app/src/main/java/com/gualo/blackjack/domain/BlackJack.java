package com.gualo.blackjack.domain;

import android.widget.Toast;

import com.gualo.blackjack.BlackJackActivity;
import com.gualo.blackjack.jsonMessages.BlackJackBoardMessage;
import com.gualo.blackjack.jsonMessages.BlackJackMovement;

import org.json.JSONException;

import java.util.ArrayList;

import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.http.Proxy;
import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONParameter;
import edu.uclm.esi.common.jsonMessages.OKMessage;

/**
 * Created by Gualo on 11/4/15.
 */
public class BlackJack {
    public static int BLACKJACK=2;
    private BlackJackActivity ctx;
    private String opponent;
    private String userWithTurn;
    private String cartas;

    public BlackJack(BlackJackActivity ctx) {
        this.ctx=ctx;
        this.cartas="";
    }
    public void put(BlackJackMovement mov) {
        Store store=Store.get();
        JSONParameter jspIdUser=new JSONParameter("idUser", ""+ store.getUser().getId());
        JSONParameter jspIdGame=new JSONParameter("idGame", ""+store.getIdGame());
        JSONParameter jspIdMatch=new JSONParameter("idMatch", ""+store.getIdMatch());
        try {
            JSONMessage jsm= Proxy.get().postJSONOrderWithResponse("SendMovement.action", mov, jspIdUser, jspIdGame, jspIdMatch);
            if (jsm.getType().equals(OKMessage.class.getSimpleName())) {
                Toast.makeText(this.ctx, "Succesfully sent", Toast.LENGTH_LONG).show();

            } else {
                ErrorMessage em=(ErrorMessage) jsm;
                Toast.makeText(this.ctx, em.getText(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this.ctx, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String getCartas() {
        return cartas;
    }

    public void setCartas(String cartas) {
        this.cartas = cartas;
    }

    public void load(BlackJackBoardMessage board) throws JSONException {
        ///aqui falta recibir el tablero
        this.cartas=board.getCartas();

        if (board.getPlayer2()!=null) {
            if (Store.get().getUser().getEmail().equals(board.getPlayer1()))
                this.opponent=board.getPlayer2();
            else
                this.opponent=board.getPlayer1();
            this.userWithTurn=board.getUserWithTurn();
        }
    }

    public String getOpponent() {
        return opponent;
    }

    public String getUserWithTurn() {
        return userWithTurn;
    }
}
