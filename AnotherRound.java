package edu.ramapo.yashken1.pinochle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

public class AnotherRound extends AppCompatActivity implements Serializable {

    Main local_main =  new Main();
    Game local_game;// = new Game();
    Human p1;// = new Human("");
    Computer p2;// = new Computer("");
    Round local_round;// = new Round();
    Card lead = new Card();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_round);
        local_main = (Main) getIntent().getSerializableExtra("main_obj");
        local_game = local_main.get_game_object();
        p1 = local_main.get_human_object();
        p2 = local_main.get_computer_object();
        p1.add_to_game_score(p1.get_round_score());
        p2.add_to_game_score(p2.get_round_score());
        local_round = local_game.get_round_object();


        if (p1.get_game_score() > p2.get_game_score()) {
            TextView txt = (TextView)findViewById(R.id.humanConclusion);
            txt.setText("Human's score: " + p1.get_game_score());
            txt.findViewById(R.id.compConclusion);
            txt.setText("Computer's score: " + p2.get_game_score());
            txt.findViewById(R.id.textView23);
            txt.setText("Human won this round");
        }
        else if (p1.get_game_score() > p2.get_game_score()) {
            TextView txt = (TextView)findViewById(R.id.humanConclusion);
            txt.setText("Human's score: " + p1.get_game_score());
            txt.findViewById(R.id.compConclusion);
            txt.setText("Computer's score: " + p2.get_game_score());
            txt.findViewById(R.id.textView23);
            txt.setText("Computer won this round");

        }
        else {
            TextView txt = (TextView)findViewById(R.id.humanConclusion);
            txt.setText("Human's score: " + p1.get_game_score());
            txt.findViewById(R.id.compConclusion);
            txt.setText("Computer's score: " + p2.get_game_score());
            txt.findViewById(R.id.textView23);
            txt.setText("It's a tie!");
        }

        p1.clear_for_new_round();
        p2.clear_for_new_round();
    }

    /**
     This function handles the user button click.
     @param v- a View holding the ir of the user's button choice.
     */
    public void decision(View v){
        if(v.getId() == R.id.yesRound){
            local_game = new Game(p1,p2);
            Intent i = new Intent(getApplicationContext(), RoundDisplay.class);

            i.putExtra("main_obj", local_main);
            startActivity(i);
        }
        else{
            finishAffinity();
            System.exit(0);
        }

    }
}

