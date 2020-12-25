package edu.ramapo.yashken1.pinochle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;

public class coinToss extends AppCompatActivity implements Serializable {
    Main local_main =  new Main();
    Game local_game;// = new Game();
    Human p1;// = new Human("");
    Computer p2;// = new Computer("");
    Round local_round;// = new Round();
    //int coin_selection;
    String reasoning = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_toss);
        local_main = (Main) getIntent().getSerializableExtra("main_obj");
        local_game = local_main.get_game_object();
        p1 = local_main.get_human_object();
        p2 = local_main.get_computer_object();
        local_round =local_game.get_round_object();
        reasoning = (String) getIntent().getSerializableExtra("reason");
        TextView text = (TextView) findViewById(R.id.coindeclaration);
        text.setAlpha(0.0f);
        View b = findViewById(R.id.nextact);
        b.setVisibility(b.INVISIBLE);
        b.setEnabled(false);
        TextView myReason = findViewById(R.id.reason);
        myReason.setText(reasoning);
    }

    public void sending_coin(View v){
        int actual_coin;
        String coinIs;
        View buttons = findViewById(R.id.heads);
        buttons.setEnabled(false);
        buttons = findViewById(R.id.tails);
        buttons.setEnabled(false);
        if (v.getId() == R.id.heads) {
            local_round.set_coin(0);
        }
        if(v.getId() == R.id.tails) {
            local_round.set_coin(1);
        }
        local_round.setting_beginner();
        actual_coin = local_round.get_actual_coin();
        if(actual_coin == 0)
            coinIs = "Heads";
        else
            coinIs = "Tails";
        TextView text = (TextView) findViewById(R.id.coindeclaration);
        text.setText("Coin shows: " + coinIs);
        text.setAlpha(1.0f);
        View b = findViewById(R.id.nextact);
        b.setVisibility(b.VISIBLE);
        b.setEnabled(true);
    }

    public void nextActivity(View v){
        Intent i = new Intent(getApplicationContext(), RoundDisplay.class);

        i.putExtra("main_obj", local_main);
        i.putExtra("reason", reasoning);
        startActivity(i);
    }
}