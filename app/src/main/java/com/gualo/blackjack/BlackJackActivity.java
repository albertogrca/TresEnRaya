package com.gualo.blackjack;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import edu.uclm.esi.common.jsonMessages.OKMessage;

public class BlackJackActivity extends ActionBarActivity {
    private BlackJack match;
    private TextView tvPlayer;
    private TextView tvMessage;
    private TextView tvOpponent;
    private TextView tvCartas;
    private TextView tvPuntos;
    private Button btnsPedirCarta;
    private Button btnsPlantarse;
    private Button btnApostar;
    private EditText inputFichas;
    private int puntos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Store.get().setCurrentContext(this);
        setContentView(R.layout.activity_black_jack_);
        this.tvPlayer=(TextView) this.findViewById(R.id.textViewBlackJackPlayer);
        this.tvMessage=(TextView) this.findViewById(R.id.textViewMessage);
        this.tvOpponent=(TextView) this.findViewById(R.id.textViewOpponent);
        this.tvCartas=(TextView) this.findViewById(R.id.textViewCarta);
        this.tvPuntos=(TextView) this.findViewById(R.id.textViewPuntos);
        this.inputFichas=(EditText) this.findViewById(R.id.inputFichas);
        //this.btns=new Button[2];
        //((TextView) this.findViewById(R.id.textViewBlackJackPlayer)).setText(tvPlayer.getText());
        Store store=Store.get();
        this.tvPlayer.setText(store.getUser().getEmail());
        final String user=(String)this.tvPlayer.getText();
        //final String cartas

        btnApostar = (Button) findViewById(R.id.btnApostar);
        this.btnsPedirCarta = (Button) this.findViewById(R.id.btnPedirCarta);
        btnsPedirCarta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (match.getOpponent()==null) {
                    Dialogs.showOneButtonDialog(BlackJackActivity.this, "Boton apretado", "Wait for the opponent", "OK");
                    //Dialogs.showOneButtonDialog(BlackJackActivity.this, "Attention", "Wait for the opponent", "OK");
                } else if (!match.getUserWithTurn().equals(Store.get().getUser().getEmail())) {
                    Dialogs.showOneButtonDialog(BlackJackActivity.this, "Attention", "It's not your turn", "OK");
                } else {
                    if (puntos <= 21) {
                        BlackJackMovement mov;
                        ///metemos el user y las cartas junto al movimiento
                        mov = new BlackJackMovement(tvPlayer.getText().toString(),tvCartas.getText().toString(), "" + puntos + "", "c");
                        match.put(mov);
                    }
                    else {
                        Dialogs.showOneButtonDialog(BlackJackActivity.this, "Puntuación límite", "Has excedido los 21 puntos, lo sentimos.", "OK :(");
                        btnsPedirCarta.setEnabled(false);
                        btnsPlantarse.setEnabled(false);
                        tvMessage.setText("Has jugado tu turno.");
                        BlackJackMovement mov;
                        mov = new BlackJackMovement(tvPlayer.getText().toString(), tvCartas.getText().toString(), "" + puntos + "", "p");
                        match.put(mov);
                    }
                }
            }
        });
        this.btnsPlantarse = (Button) this.findViewById(R.id.btnPlantarse);
        btnsPlantarse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (match.getOpponent()==null) {
                    Dialogs.showOneButtonDialog(BlackJackActivity.this, "Attention", "Wait for the opponent", "OK");
                    //Dialogs.showOneButtonDialog(BlackJackActivity.this, "Attention", "Wait for the opponent", "OK");
                } else if (!match.getUserWithTurn().equals(Store.get().getUser().getEmail())) {
                    Dialogs.showOneButtonDialog(BlackJackActivity.this, "Attention", "It's not your turn", "OK");
                } else {
                    btnsPedirCarta.setEnabled(false);
                    btnsPlantarse.setEnabled(false);
                    tvMessage.setText("Has jugado tu turno. ");
                    BlackJackMovement mov;
                    mov = new BlackJackMovement(tvPlayer.getText().toString(),tvCartas.getText().toString(),"" + puntos + "","p");
                    match.put(mov);

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
            JSONMessage jsm= Proxy.get().postJSONOrderWithResponse("GetBoardBJ.action", jspIdUser, jspIdGame, jspIdMatch);
            if (jsm.getType().equals(BlackJackBoardMessage.class.getSimpleName())) {
                loadBoard(jsm);
               //Dialogs.showOneButtonDialog(BlackJackActivity.this, "Esta hecho el if", "It's not your turn", "OK");
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
        this.tvOpponent.setText(this.match.getOpponent().toString());
        this.tvCartas.setText((CharSequence) this.match.getCartas());
        this.puntos = calcular_puntos(this.match.getCartas());
        tvPuntos.setText(puntos + " puntos.");
    }

    public int calcular_puntos(String cartas){
        int puntos=0;
        if (cartas.length() > 0) {
            String[] cartasAux = cartas.split("-");
            for (int i = 0; i < cartasAux.length; i++) {
                if (cartasAux[i].length() == 2) {
                    puntos += Character.getNumericValue(cartasAux[i].charAt(1));
                } else {
                    int aux = 10;
                    puntos += aux;
                }
            }
        }
        return puntos;
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

    public void apostar(View v) {
        btnsPedirCarta.setEnabled(true);
        btnsPlantarse.setEnabled(true);
        btnApostar.setEnabled(false);
        BlackJackMovement mov;
        String apuestaAux=inputFichas.getText().toString();
        mov = new BlackJackMovement(tvPlayer.getText().toString(),"a", apuestaAux);
        match.put(mov);
    }
}
