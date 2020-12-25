package edu.ramapo.yashken1.pinochle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PerformingMeld extends AppCompatActivity implements Serializable {

    String [] human_buttons = {"hb1", "hb2","hb3","hb4","hb5","hb6","hb7","hb8","hb9","hb10","hb11","hb12"};

    Main local_main =  new Main();
    Game local_game;// = new Game();
    Human p1;// = new Human("");
    Computer p2;// = new Computer("");
    Round local_round;// = new Round();
    Turn local_turn;
    List<Card> meld_vec = new ArrayList<Card>();
    String meld_name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performing_meld);
        local_main = (Main) getIntent().getSerializableExtra("main_obj");

        local_game = local_main.get_game_object();
        p1 = local_main.get_human_object();
        p2 = local_main.get_computer_object();
        local_round = local_game.get_round_object();
        local_turn = local_round.get_turn_object();
        disableHButtons();
        setting_cards_text();
    }

    /**
     This function handles the user button click, and saves the appropriate meld name.
     @param v- a View holding the id of the user's button choice- representing the meld name
        the user is trying to make.
     */
    public void get_meld_name(View v){
        if(v.getId() == R.id.flushm){
            meld_name = "flush_meld";
        }
        else if(v.getId() == R.id.fourAm){
            meld_name = "fourA";
        }
        else if(v.getId() == R.id.fourKm){
            meld_name = "fourK";
        }
        else if(v.getId() == R.id.fourQm){
            meld_name = "fourQ";
        }
        else if(v.getId() == R.id.royalMm){
            meld_name = "royal_marriage";
        }
        else if(v.getId() == R.id.fourJm){
            meld_name = "fourJ";
        }
        else if(v.getId() == R.id.pinochlem){
            meld_name = "pinochle";
        }
        else if(v.getId() == R.id.spadeMm){
            meld_name = "spade_marriage";
        }
        else if(v.getId() == R.id.heartMm){
            meld_name = "heart_marriage";
        }
        else if(v.getId() == R.id.diamondMm){
            meld_name = "diamond_marriage";
        }
        else if(v.getId() == R.id.clubMm){
            meld_name = "club_marriage";
        }
        else if(v.getId() == R.id.dixm){
            meld_name = "dix";
        }
        enableHButtons();
    }
    /**
     This function handles the user button click, and saves the appropriate card to the meld vector.
     @param v- a View holding the id of the user's button choice- representing a card.
     */
    public void add_to_meld_vec(View v){
        Button b = (Button)v;
        String selection = b.getText().toString();
        meld_vec.add(new Card(selection.charAt(0), selection.charAt(1)));
        b.setEnabled(false);
    }

    /**
     This function handles the user button click that "submits" the meld and continues to the next
        activity.
     @param v- a View holding the id of the user's button choice- the submit button.
     */
    public void submit(View v){
        local_turn.set_want_to_meld(1);
        local_turn.performing_meld(meld_name, meld_vec);
        Intent i = new Intent(getApplicationContext(), RoundDisplay.class);

        i.putExtra("main_obj", local_main);
        startActivity(i);

    }
    /**
     These functions enable/disable buttons when needed.
     */
    void disableHButtons (){
        for (String name : human_buttons) {
            int id = getResources().getIdentifier(name, "id", getPackageName());
            Button b = (Button) findViewById(id);
            b.setEnabled(false);
            b.setVisibility(b.INVISIBLE);
        }
        TextView t1 = (TextView) findViewById(R.id.textView10);
        t1.setAlpha(0.0f);
        Button b = (Button) findViewById(R.id.done);
        b.setEnabled(false);
        b.setVisibility(b.INVISIBLE);

    }
    void enableHButtons (){
        for (String name : human_buttons) {
            int id = getResources().getIdentifier(name, "id", getPackageName());
            Button b = (Button) findViewById(id);
            b.setEnabled(true);
            b.setVisibility(b.VISIBLE);
        }
        TextView t1 = (TextView) findViewById(R.id.textView10);
        t1.setAlpha(1.0f);
        Button b = (Button) findViewById(R.id.done);
        b.setEnabled(true);
        b.setVisibility(b.VISIBLE);
    }

    /**
     This function sets the text of the buttons to each match a crd from the Player's hand.
     */
    void setting_cards_text(){
        int i;
        TextView hb;
        List<String> hcards = new ArrayList<String>();
        if (p1.get_hand().size() >= 12){
            String temp="";
            for(i = 0; i < 12; i++){
                temp = Character.toString(p1.get_hand().get(i).get_type()) + Character.toString(p1.get_hand().get(i).get_suit());
                hcards.add(temp);
            }
        }
        else{

            String temp="";
            for(i = 0; i < p1.get_hand().size(); i++){
                temp = Character.toString(p1.get_hand().get(i).get_type()) + Character.toString(p1.get_hand().get(i).get_suit());
                hcards.add(temp);
            }
        }
        if(hcards.size() >= 1){
            hb = (TextView)findViewById(R.id.hb1);
            hb.setText(hcards.get(0));
        }
        if(hcards.size() >= 2){
            hb = (TextView)findViewById(R.id.hb2);
            hb.setText(hcards.get(1));
        }
        if(hcards.size() >= 3){
            hb = (TextView)findViewById(R.id.hb3);
            hb.setText(hcards.get(2));
        }
        if(hcards.size() >= 4){
            hb = (TextView)findViewById(R.id.hb4);
            hb.setText(hcards.get(3));
        }
        if(hcards.size() >= 5){
            hb = (TextView)findViewById(R.id.hb5);
            hb.setText(hcards.get(4));
        }
        if(hcards.size() >= 6){
            hb = (TextView)findViewById(R.id.hb6);
            hb.setText(hcards.get(5));
        }
        if(hcards.size() >= 7){
            hb = (TextView)findViewById(R.id.hb7);
            hb.setText(hcards.get(6));
        }
        if(hcards.size() >= 8){
            hb = (TextView)findViewById(R.id.hb8);
            hb.setText(hcards.get(7));
        }
        if(hcards.size() >= 9){
            hb = (TextView)findViewById(R.id.hb9);
            hb.setText(hcards.get(8));
        }
        if(hcards.size() >= 10){
            hb = (TextView)findViewById(R.id.hb10);
            hb.setText(hcards.get(9));
        }
        if(hcards.size() >= 11){
            hb = (TextView)findViewById(R.id.hb11);
            hb.setText(hcards.get(10));
        }
        if(hcards.size() >= 12){
            hb = (TextView)findViewById(R.id.hb12);
            hb.setText(hcards.get(11));
        }
    }
}



