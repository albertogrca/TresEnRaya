package com.gualo.blackjack;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gualo.blackjack.domain.BlackJack;
import com.gualo.blackjack.jsonMessages.BlackJackBoardMessage;
import com.gualo.blackjack.jsonMessages.BlackJackMatchReadyMessage;
import com.gualo.blackjack.jsonMessages.BlackJackWaitingMessage;
import com.maco.tresenraya.R;

import com.gualo.blackjack.jsonMessages.BlackJackMovement;
import com.maco.tresenraya.domain.TresEnRaya;
import com.maco.tresenraya.jsonMessages.TresEnRayaBoardMessage;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.common.androidClient.dialogs.Dialogs;
import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.http.Proxy;
import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONParameter;

public class BlackJackActivity extends ActionBarActivity {
    private BlackJack match;
    private TextView tvPlayer;
    private TextView tvMessage;
    private TextView tvOpponent;
    private Button btnsPedirCarta;
    private Button btnsPlantarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Store.get().setCurrentContext(this);
        setContentView(R.layout.activity_black_jack_);
        this.tvPlayer=(TextView) this.findViewById(R.id.textViewBlackJackPlayer);
        this.tvMessage=(TextView) this.findViewById(R.id.textViewMessage);
        this.tvOpponent=(TextView) this.findViewById(R.id.textViewOpponent);
        //this.btns=new Button[2];
        //((TextView) this.findViewById(R.id.textViewBlackJackPlayer)).setText(tvPlayer.getText());
        Store store=Store.get();
        this.tvPlayer.setText(store.getUser().getEmail());
        this.btnsPedirCarta = (Button) this.findViewById(R.id.btnPedirCarta);
        btnsPedirCarta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (match.getOpponent()==null) {//poner un ==
                    Dialogs.showOneButtonDialog(BlackJackActivity.this, "Boton apretado", "Wait for the opponent", "OK");
                    //Dialogs.showOneButtonDialog(BlackJackActivity.this, "Attention", "Wait for the opponent", "OK");
                } else if (!match.getUserWithTurn().equals(Store.get().getUser().getEmail())) {
                    Dialogs.showOneButtonDialog(BlackJackActivity.this, "Attention", "It's not your turn", "OK");
                } else {
                    JSONObject jso=(JSONObject) v.getTag();
                    BlackJackMovement mov;

                }
            }
        });
        this.btnsPlantarse = (Button) this.findViewById(R.id.btnPlantarse);
        btnsPlantarse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (match.getOpponent()==null) {
                    Dialogs.showOneButtonDialog(BlackJackActivity.this, "Boton plantarse", "Wait for the opponent", "OK");
                    //Dialogs.showOneButtonDialog(BlackJackActivity.this, "Attention", "Wait for the opponent", "OK");
                } else if (!match.getUserWithTurn().equals(Store.get().getUser().getEmail())) {
                    Dialogs.showOneButtonDialog(BlackJackActivity.this, "Attention", "It's not your turn", "OK");
                } else {
                    JSONObject jso=(JSONObject) v.getTag();
                    BlackJackMovement mov;

                }
            }
        });

    this.match=new BlackJack(this);
    loadMatch();
    }
    private void loadMatch() {
        Store store=Store.get();
        this.tvPlayer.setText(store.getUser().getEmail());
        JSONParameter jspIdUser=new JSONParameter("idUser", ""+store.getUser().getId());
        JSONParameter jspIdGame=new JSONParameter("idGame", ""+store.getIdGame());
        JSONParameter jspIdMatch=new JSONParameter("idMatch", ""+store.getIdMatch());
        try {
            JSONMessage jsm= Proxy.get().postJSONOrderWithResponse("GetBoard.action", jspIdUser, jspIdGame, jspIdMatch);
            if (jsm.getType().equals(BlackJackBoardMessage.class.getSimpleName())) {
                loadBoard(jsm);
               // Dialogs.showOneButtonDialog(BlackJackActivity.this, "Esta hecho el if", "It's not your turn", "OK");


            } else {
                ErrorMessage em=(ErrorMessage) jsm;
                Toast.makeText(this, em.getText(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void loadBoard(JSONMessage jsm) throws JSONException {
        BlackJackBoardMessage board=(BlackJackBoardMessage) jsm;
        if (this.match==null)
            this.match=new BlackJack(this);
        this.match.load(board);
    }
    public void loadMessage(BlackJackWaitingMessage bj) {
        this.tvMessage.setText(bj.getText());
    }
    public void loadReadyMessage(BlackJackMatchReadyMessage rm) {
        String player1=rm.getPlayer1();
        String player2=rm.getPlayer2();
        String opponent;
        if (Store.get().getUser().getEmail().equals(player1)) {
            opponent = player2;
            this.tvOpponent.setText(opponent);
        }else
            opponent=player1;
            this.tvOpponent.setText(opponent);
        this.tvOpponent.setText("Playing against " + opponent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_black_jack_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
