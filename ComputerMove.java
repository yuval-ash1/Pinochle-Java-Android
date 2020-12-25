package edu.ramapo.yashken1.pinochle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

public class ComputerMove extends AppCompatActivity implements Serializable {

    Main local_main =  new Main();
    Game local_game;// = new Game();
    Human p1;// = new Human("");
    Computer p2;// = new Computer("");
    Round local_round;// = new Round();
    Turn local_turn;
    Card lead, chase;
    Card tempLead;
    String strategy, nextPlayer;
    boolean help, meld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_move);

        TextView  t1 = (TextView) findViewById(R.id.textView15);
        t1.setAlpha(0.0f); //setting invisible
        t1 = (TextView) findViewById(R.id.best_card);
        t1.setAlpha(0.0f); //setting invisible
        t1 = (TextView) findViewById(R.id.textView17);
        t1.setAlpha(0.0f); //setting invisible
        t1 = (TextView) findViewById(R.id.humanwon);
        t1.setAlpha(0.0f); //setting invisible
        TextView cmeld = (TextView) findViewById(R.id.compmeldname);
        cmeld.setAlpha(0.0f);
        //t1 = (TextView) findViewById(R.id.printinglead);
        //t1.setAlpha(0.0f); //setting invisible
        //t1 = (TextView) findViewById(R.id.printingchase);
        //t1.setAlpha(0.0f); //setting invisible
        View cont = findViewById(R.id.gotonext);
        cont.setVisibility(cont.INVISIBLE);
        cont.setEnabled(false);

        View b1 = findViewById(R.id.yeshelp);
        b1.setVisibility(b1.INVISIBLE);
        b1.setEnabled(false);
        b1 = findViewById(R.id.nohelp);
        b1.setVisibility(b1.INVISIBLE);
        b1.setEnabled(false);
        b1 = findViewById(R.id.yesmeld);
        b1.setVisibility(b1.INVISIBLE);
        b1.setEnabled(false);
        b1 = findViewById(R.id.nomeld);
        b1.setVisibility(b1.INVISIBLE);
        b1.setEnabled(false);

        local_main = (Main) getIntent().getSerializableExtra("main_obj");
        lead = (Card) getIntent().getSerializableExtra("lead_card");
        local_game = local_main.get_game_object();
        p1 = local_main.get_human_object();
        p2 = local_main.get_computer_object();
        local_round = local_game.get_round_object();
        local_turn = local_round.get_turn_object();
        nextPlayer = local_round.playerList[local_round.get_custom_int().get_value()].get_name();   //getting the next player's name

        if(lead.get_type() == 'e'){
            String tempCard;
            tempCard = p2.strategy_lead(local_round.get_trump());
            tempLead = local_turn.first_player_playing();
            local_turn.setLead(tempCard);
        }
        else{
            p2.strategy_chase(local_round.get_trump(), lead.get_type(), lead.get_suit());
            chase = local_turn.second_player_playing();


        }
        strategy = p2.getStrategy();

        TextView txt = (TextView) findViewById(R.id.textView12);
        txt.setText(strategy);

    }

    /**
     This function handles the user button click, determined with turn winner, and moves to the
        next activity accordingly.
     @param v- a View holding the ir of the user's button choice.
     */
    public void findingWinner(View v){
        if(lead.get_type() == 'e'){
            Intent i = new Intent(getApplicationContext(), UserDisplay.class);

            i.putExtra("main_obj", local_main);
            i.putExtra("lead_card", tempLead);
            startActivity(i);
        }
        else {
            Boolean winner = local_turn.get_winner();
            if ((nextPlayer.equals("Human") && winner == true) || (nextPlayer.equals("Computer") && winner == false)) {
                //This means human won
                TextView t = (TextView)findViewById(R.id.textView12);
                t.setAlpha(0.0f);
                View b = findViewById(R.id.contToWinner);
                b.setVisibility(b.INVISIBLE);
                b.setEnabled(false);

                TextView t2 = (TextView) findViewById(R.id.humanwon);
                t2.setText("Human won!");
                t2.setAlpha(1.0f);
                t2 = (TextView) findViewById(R.id.textView15);
                t2.setAlpha(1.0f);
                View b2 = findViewById(R.id.yeshelp);
                b2.setVisibility(b2.VISIBLE);
                b2.setEnabled(true);
                b2 = findViewById(R.id.nohelp);
                b2.setVisibility(b2.VISIBLE);
                b2.setEnabled(true);
            } else {
                //This means computer won
                TextView t = (TextView)findViewById(R.id.textView12);
                t.setAlpha(0.0f);
                View b = findViewById(R.id.contToWinner);
                b.setVisibility(b.INVISIBLE);
                b.setEnabled(false);

                String computer_meld = p2.best_meld(p2.get_hand(), local_round.get_trump());
                TextView winning = (TextView) findViewById(R.id.humanwon);
                if (computer_meld.equals("No possible melds"))
                    computer_meld = "no melds";
                else{
                    local_turn.set_want_to_meld(1);
                    local_turn.performing_meld(computer_meld, p2.get_hand());
                }
                winning.setText("Computer won and performed: " + computer_meld);

                winning.setAlpha(1.0f);
                View next = findViewById(R.id.gotonext);
                next.setEnabled(true);
                next.setVisibility(next.VISIBLE);
            }
        }
        local_round.round_dealing_turn_cards();
    }
    /**
     This function handles the user button click, and interprets whether the user wants help with
        meld or no. It then handles the screen accordingly.
     @param v- a View holding the ir of the user's button choice.
     */
    public void helpOrNo(View v){
        if(v.getId() == R.id.yeshelp)
            help = true;
        else
            help = false;

        TextView t2;
        if (help) {
            t2 = (TextView) findViewById(R.id.best_card);
            t2.setText(local_turn.getting_best_meld());
            t2.setAlpha(1.0f);
        }
        t2 = (TextView) findViewById(R.id.textView17);
        t2.setAlpha(1.0f);
        View b2 = findViewById(R.id.yesmeld);
        b2.setVisibility(b2.VISIBLE);
        b2.setEnabled(true);
        b2 = findViewById(R.id.nomeld);
        b2.setVisibility(b2.VISIBLE);
        b2.setEnabled(true);
    }
    /**
     This function handles the user button click, and interprets whether the user wants to perform a meld
     or no. It then handles the screen accordingly.
     @param v- a View holding the ir of the user's button choice.
     */
    public void meldOrNo(View v){
        if(v.getId() == R.id.yesmeld)
            meld = true;
        else
            meld = false;

        if (meld) {
            //First read in meld vector.
            //local_turn.set_want_to_meld(1);

            Intent i = new Intent(getApplicationContext(), PerformingMeld.class);
            i.putExtra("main_obj", local_main);
            startActivity(i);

        }
        else {
            local_turn.set_want_to_meld(2);
            Intent i = new Intent(getApplicationContext(), RoundDisplay.class);

            i.putExtra("main_obj", local_main);
            startActivity(i);
        }
    }
    /**
     This function handles the user button click, and continues the the appropriate next activity.
     @param v- a View holding the ir of the user's button choice.
     */
    public void continuetonext(View v){
        Intent i = new Intent(getApplicationContext(), RoundDisplay.class);

        i.putExtra("main_obj", local_main);
        startActivity(i);
    }
}