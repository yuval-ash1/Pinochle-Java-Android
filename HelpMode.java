package edu.ramapo.yashken1.pinochle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

public class HelpMode extends AppCompatActivity implements Serializable {

    Main local_main =  new Main();
    Game local_game;// = new Game();
    Human p1;// = new Human("");
    Computer p2;// = new Computer("");
    Round local_round;// = new Round();
    Turn local_turn;
    Card lead = new Card();
    String strategy = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_mode);

        local_main = local_main = (Main) getIntent().getSerializableExtra("main_obj");
        lead = (Card) getIntent().getSerializableExtra("lead_card");
        local_turn = (Turn) getIntent().getSerializableExtra("turn_object");
        local_game = local_main.get_game_object();
        local_round = local_game.get_round_object();
        p1 = local_main.get_human_object();

        if(lead.get_type() == 'e'){
            strategy = p1.strategy_lead(local_round.get_trump());
        }
        else{
            p1.strategy_chase(local_round.get_trump(), lead.get_type(), lead.get_suit());
        }
        strategy = p1.getStrategy();

        TextView txt = (TextView) findViewById(R.id.reccomendation);
        txt.setText(strategy);
    }

    /**
     This function handles the user button click, and continues the the TurnDisplay activity after
        the user got the help.
     @param v- a View holding the ir of the user's button choice.
     */
    public void gotoplay(View v){
        Intent i = new Intent(getApplicationContext(), TurnDisplay.class);

        i.putExtra("main_obj", local_main);
        i.putExtra("turn_object", local_turn);
        i.putExtra("lead_card", lead);
        startActivity(i);
    }
}

