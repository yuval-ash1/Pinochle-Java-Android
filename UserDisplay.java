package edu.ramapo.yashken1.pinochle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.Serializable;

public class UserDisplay extends AppCompatActivity implements Serializable {

    Main local_main =  new Main();
    Game local_game;
    Human p1;
    Computer p2;
    Round local_round;
    Turn local_turn;
    String nextPlayer;
    Card lead;
    Int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_display);
        local_main = (Main) getIntent().getSerializableExtra("main_obj");
        lead = (Card) getIntent().getSerializableExtra("lead_card");
        local_game = local_main.get_game_object();
        p1 = local_main.get_human_object();
        p2 = local_main.get_computer_object();
        local_round = local_game.get_round_object();
        //local_round.initialize_round();
        local_turn = local_round.get_turn_object();
        index = local_round.getNPI();
        //getting the next player's name:
        nextPlayer = local_round.playerList[local_round.get_custom_int().get_value()].get_name();
        if(lead.get_suit() == 'e' && index.get_value() == 1 || lead.get_suit() != 'e' && index.get_value() == 0){
            View v1 = findViewById(R.id.option4);
            v1.setVisibility(View.INVISIBLE);
            v1.setEnabled(false);
        }
    }

    /**
     This function interprets the user's button click selection and handles it appropriately.
     @param v- a view holding the button's id.
     */
    public void selection(View v){
        if(v.getId() == R.id.option1){
            local_turn.set_player_choice(1);
            Intent i = new Intent(getApplicationContext(), SavingGame.class);
            i.putExtra("main_obj", local_main);
            startActivity(i);
        }
        else if(v.getId() == R.id.option2){
            if(lead.get_suit() == 'e' && index.get_value() == 1 || p1.get_hand().size() == 11 ){
                local_turn.set_player_choice(2);
                Intent i = new Intent(getApplicationContext(), ComputerMove.class);

                i.putExtra("main_obj", local_main);
                i.putExtra("turn_obj", local_turn);
                i.putExtra("lead_card", lead);
                startActivity(i);
            }
            else{
                local_turn.set_player_choice(2);
                Intent i = new Intent(getApplicationContext(), TurnDisplay.class);

                i.putExtra("main_obj", local_main);
                i.putExtra("turn_obj", local_turn);
                i.putExtra("lead_card", lead);
                startActivity(i);
            }
        }
        else if(v.getId() == R.id.option3){
            finishAffinity();
            System.exit(0);
        }
        else{ //option 4
            local_turn.set_player_choice(4);
            Intent i = new Intent(getApplicationContext(), HelpMode.class);

            i.putExtra("main_obj", local_main);
            i.putExtra("turn_obj", local_turn);
            i.putExtra("lead_card", lead);
            startActivity(i);
        }
    }
}