package com.gualo.blackjack;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gualo.blackjack.jsonMessages.RankingJSONMessage;
import com.maco.tresenraya.R;

import java.util.StringTokenizer;

import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.http.Proxy;
import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONParameter;

public class BlackJackRanking extends ActionBarActivity {
    private LinearLayout rankingList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_jack_ranking2);

        String ranking=verRanking();

        this.rankingList=(LinearLayout) this.findViewById(R.id.ll);

        StringTokenizer st = new StringTokenizer(ranking,"#");
        while (st.hasMoreTokens()) {
            String aux=st.nextToken();
            StringTokenizer st2 = new StringTokenizer(aux,"-");
            String player=st2.nextToken();
            String fichas=st2.nextToken();

            final TextView tvPlayer=new TextView(this);
            tvPlayer.setText(player+"\t\t\t");
            LinearLayout ll=new LinearLayout(this);
            ll.addView(tvPlayer);

            final TextView tvFichas=new TextView(this);
            tvFichas.setText(fichas);
            ll.addView(tvFichas);

            this.rankingList.addView(ll);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_black_jack_ranking2, menu);
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


    public String verRanking(){
        Store store=Store.get();
        String rank="";
        String[] aux = null;
        JSONParameter jspEmailUser=new JSONParameter("emailUser", ""+store.getUser().getEmail());
        try {
            JSONMessage jsm= Proxy.get().postJSONOrderWithResponse("GetBJRanking.action", jspEmailUser);
            if (jsm.getType().equals(RankingJSONMessage.class.getSimpleName())) {
                rank=jsm.toString();
                aux = rank.split("text");


            } else {
                ErrorMessage em=(ErrorMessage) jsm;
                Toast.makeText(this, em.getText(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return aux[1].substring(3,aux[1].length()-2);
    }
}
