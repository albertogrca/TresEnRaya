package com.gualo.blackjack.jsonMessages;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;

/**
 * Created by Gualo on 11/4/15.
 */
public class BlackJackMovement extends JSONMessage {
    @JSONable
    private String tipo;

    public BlackJackMovement( String tipo) {
        super(true);
        this.tipo = tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    /*@JSONable
    private String palo;
    @JSONable
    private int valor;
    public BlackJackMovement(String palo, int valor) {
        super(true);
        this.palo=palo;
        this.valor=valor;
    }

    public String getPalo() {
        return palo;
    }

    public int getValor() {
        return valor;
    }*/

}
