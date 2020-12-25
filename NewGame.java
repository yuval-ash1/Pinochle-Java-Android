package edu.ramapo.yashken1.pinochle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class NewGame extends AppCompatActivity implements Serializable {
    Main local_main =  new Main();
    Game local_game;
    Human p1;
    Computer p2;
    Round local_round = new Round();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        String printing = "";
        local_main = (Main) getIntent().getSerializableExtra("object");
        local_main.initialize_game();
        p1 = local_main.get_human_object();
        p2 = local_main.get_computer_object();
        local_game = local_main.get_game_object();
        local_round = local_game.get_round_object();
        int num = local_game.get_r_num();

        //handling who starts
        if(local_game.get_r_num() == 1){
            printing = "Since this is the first round,";

            Intent i = new Intent(getApplicationContext(), coinToss.class);

            i.putExtra("main_obj", local_main);

            i.putExtra("reason", printing);
            startActivity(i);
        }
        else if(p1.get_game_score() == p2.get_game_score()){
            printing = "Since there is a tie,";

            Intent i = new Intent(getApplicationContext(), coinToss.class);

            i.putExtra("main_obj", local_main);
            i.putExtra("reason", printing);
            startActivity(i);
        }
        else{
            Intent i = new Intent(getApplicationContext(), RoundDisplay.class);
            i.putExtra("main_obj", local_main);
            startActivity(i);

        }
    }
}

