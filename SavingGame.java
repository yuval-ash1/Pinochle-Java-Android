package edu.ramapo.yashken1.pinochle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

public class SavingGame extends AppCompatActivity implements Serializable {

    Main local_main =  new Main();
    Game local_game;// = new Game();
    Human p1;// = new Human("");
    Computer p2;// = new Computer("");
    Round local_round;// = new Round();
    Turn local_turn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_game);

        local_main = (Main) getIntent().getSerializableExtra("main_obj");
        local_game = local_main.get_game_object();
        local_round = local_game.get_round_object();
        local_turn = local_round.get_turn_object();
    }

    /**
     This function is responsible for setting the user's choice to 1, which then saves the game and
        quits.
     @param v- a View holding the user's file name input.
     */
    public void performTask(View v){
        EditText text = (EditText)findViewById(R.id.enterFileName);
        String value = text.getText().toString();

            local_turn.setFileName(value);
            //local_turn.set_player_choice(1);
            local_turn.first_player_playing();
            Intent i = new Intent(getApplicationContext(), GoodbyePage.class);
            startActivity(i);

    }
}