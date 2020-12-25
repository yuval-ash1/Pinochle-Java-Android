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

public class TurnDisplay extends AppCompatActivity implements Serializable {
    String [] human_buttons = {"hb1", "hb2","hb3","hb4","hb5","hb6","hb7","hb8","hb9","hb10","hb11","hb12"};
    String [] computer_buttons = {"cb1", "cb2","cb3","cb4","cb5","cb6","cb7","cb8","cb9","cb10","cb11","cb12"};

    Main local_main =  new Main();
    Game local_game;// = new Game();
    Human p1;// = new Human("");
    Computer p2;// = new Computer("");
    Round local_round;// = new Round();
    Turn local_turn;
    String nextPlayer;
    Card lead = new Card();
    Card chase = new Card();
    String winnerDec;
    String choice;
    boolean help, meld;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_display);

        //setting extras disables and unclickable for now
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
        t1 = (TextView) findViewById(R.id.printinglead);
        t1.setAlpha(0.0f); //setting invisible
        t1 = (TextView) findViewById(R.id.printingchase);
        t1.setAlpha(0.0f); //setting invisible
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

        enableHButtons();
        disableCButtons();

        local_main = local_main = (Main) getIntent().getSerializableExtra("main_obj");
        lead = (Card) getIntent().getSerializableExtra("lead_card");
        if(lead.get_suit() != 'e'){
            TextView  plead = (TextView) findViewById(R.id.printinglead);
            plead.setText("Lead: " + Character.toUpperCase(lead.get_type()) + Character.toUpperCase(lead.get_suit()));
            plead.setAlpha(1.0f); //setting visible
        }
        local_game = local_main.get_game_object();
        p1 = local_main.get_human_object();
        p2 = local_main.get_computer_object();
        local_round = local_game.get_round_object();
        nextPlayer = local_round.playerList[local_round.get_custom_int().get_value()].get_name();   //getting the next player's name
        setting_cards_text();

        local_turn = local_round.get_turn_object();
    }

    /**
     This function is responsible for reading the user's card choice.
     @param v- a view that holds the card type and suit.
     */
    public void human_card_choice(View v){
        Button b = (Button)v;
        choice = b.getText().toString();
        b.setEnabled(false);
        p1.set_card_choice(choice);

        if(lead.get_type() == 'e'){
            lead = local_turn.first_player_playing();

            Intent i = new Intent(getApplicationContext(), UserDisplay.class);

            i.putExtra("main_obj", local_main);
            i.putExtra("lead_card", lead);
            startActivity(i);

        }
        else{
            chase = local_turn.second_player_playing();
            disableHButtons();
            Boolean winner = local_turn.get_winner();
            if((nextPlayer.equals("Human") && winner == true) || (nextPlayer.equals("Computer") && winner == false)) {
                //This means human won
                buttons_invisible();
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
            }
            else{
                //This means computer won
                buttons_invisible();
                String computer_meld = p2.best_meld(p2.get_hand(), local_round.get_trump());
                TextView winning = (TextView) findViewById(R.id.humanwon);
                if(computer_meld.equals("No possible melds"))
                    computer_meld = "no melds";
                winning.setText("Computer won and performed: " + computer_meld);
                local_turn.set_want_to_meld(1);
                p2.perform_a_meld(local_round.get_trump(), p2.get_hand(), "");

                winning.setAlpha(1.0f);
                View next = findViewById(R.id.gotonext);
                next.setEnabled(true);
                next.setVisibility(next.VISIBLE);
            }
        }
        disableHButtons();

        local_round.round_dealing_turn_cards();
        //logic();

    }

    /**
     This function is responsible for reading the user's choice for whether they want help with
        meld or no.
     @param v- a view that holds the user's choice.
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
     This function is responsible for reading the user's choice for whether they want to perform a
     meld or no.
     @param v- a view that holds the user's choice.
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
     These functions enable/disable human/computer buttons when needed.
     */
    void disableHButtons (){
        for (String name : human_buttons) {
            int id = getResources().getIdentifier(name, "id", getPackageName());
            Button b = (Button) findViewById(id);
            b.setEnabled(false);
        }
    }
    void disableCButtons (){
        for (String name : computer_buttons) {
            int id = getResources().getIdentifier(name, "id", getPackageName());
            Button b = (Button) findViewById(id);
            b.setEnabled(false);
        }
    }
    void enableHButtons (){
        for (String name : human_buttons) {
            int id = getResources().getIdentifier(name, "id", getPackageName());
            Button b = (Button) findViewById(id);
            b.setEnabled(true);
        }
    }
    void buttons_invisible (){
        for (String name : human_buttons) {
            int id = getResources().getIdentifier(name, "id", getPackageName());
            Button b = (Button) findViewById(id);
            b.setEnabled(false);
            b.setVisibility(b.INVISIBLE);
        }
        for (String name : computer_buttons) {
            int id = getResources().getIdentifier(name, "id", getPackageName());
            Button b = (Button) findViewById(id);
            b.setEnabled(false);
            b.setVisibility(b.INVISIBLE);
        }
        TextView  t1 = (TextView) findViewById(R.id.textView7);
        t1.setAlpha(0.0f); //setting invisible
        t1 = (TextView) findViewById(R.id.textView9);
        t1.setAlpha(0.0f);
        t1 = (TextView) findViewById(R.id.printinglead);
        t1.setAlpha(0.0f);
        t1 = (TextView) findViewById(R.id.printingchase);
        t1.setAlpha(0.0f);

    }

    /**
     This function sets the text of the buttons to each match a crd from the Player's hand.
     */
    void setting_cards_text(){
        int i;
        TextView hb, cb;
        List<String> hcards = new ArrayList<String>();
        List<String> ccards = new ArrayList<String>();
        if (p1.get_hand().size() == 12 && p2.get_hand().size() == 12){
            String temp="";
            for(i = 0; i < 12; i++){
                temp = Character.toString(p1.get_hand().get(i).get_type()) + Character.toString(p1.get_hand().get(i).get_suit());
                hcards.add(temp);
                temp = Character.toString(p2.get_hand().get(i).get_type()) + Character.toString(p2.get_hand().get(i).get_suit());
                ccards.add(temp);
            }
        }
        else{

            String temp="";
            for(i = 0; i < p1.get_hand().size(); i++) {
                temp = Character.toString(p1.get_hand().get(i).get_type()) + Character.toString(p1.get_hand().get(i).get_suit());
                hcards.add(temp);
            }
            for(i = 0; i < p2.get_hand().size(); i++){
                temp = Character.toString(p2.get_hand().get(i).get_type()) + Character.toString(p2.get_hand().get(i).get_suit());
                ccards.add(temp);
            }
        }
        if(hcards.size() >= 1) {
            hb = (TextView) findViewById(R.id.hb1);
            hb.setText(hcards.get(0));
        }
        else{
            hb = (TextView) findViewById(R.id.hb1);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 1){
            cb = (TextView)findViewById(R.id.cb1);
            cb.setText(ccards.get(0));
        }
        else{
            cb = (TextView) findViewById(R.id.cb1);
            cb.setAlpha(0.0f);
        }
        if(hcards.size() >= 2) {
            hb = (TextView) findViewById(R.id.hb2);
            hb.setText(hcards.get(1));
        }
        else{
            hb = (TextView) findViewById(R.id.hb2);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 2){
            cb = (TextView)findViewById(R.id.cb2);
            cb.setText(ccards.get(1));
        }
        else{
            cb = (TextView) findViewById(R.id.cb2);
            cb.setAlpha(0.0f);
        }
        if(hcards.size() >= 3) {
            hb = (TextView) findViewById(R.id.hb3);
            hb.setText(hcards.get(2));
        }
        else{
            hb = (TextView) findViewById(R.id.hb3);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 3){
            cb = (TextView)findViewById(R.id.cb3);
            cb.setText(ccards.get(2));
        }
        else{
            cb = (TextView) findViewById(R.id.cb3);
            cb.setAlpha(0.0f);
        }
        if(hcards.size() >= 4) {
            hb = (TextView) findViewById(R.id.hb4);
            hb.setText(hcards.get(3));
        }
        else{
            hb = (TextView) findViewById(R.id.hb4);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 4){
            cb = (TextView)findViewById(R.id.cb4);
            cb.setText(ccards.get(3));
        }
        else{
            cb = (TextView) findViewById(R.id.cb4);
            cb.setAlpha(0.0f);
        }
        if(hcards.size() >= 5) {
            hb = (TextView) findViewById(R.id.hb5);
            hb.setText(hcards.get(4));
        }
        else{
            hb = (TextView) findViewById(R.id.hb5);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 5){
            cb = (TextView)findViewById(R.id.cb5);
            cb.setText(ccards.get(4));
        }
        else{
            cb = (TextView) findViewById(R.id.cb5);
            cb.setAlpha(0.0f);
        }
        if(hcards.size() >= 6) {
            hb = (TextView) findViewById(R.id.hb6);
            hb.setText(hcards.get(5));
        }
        else{
            hb = (TextView) findViewById(R.id.hb6);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 6){
            cb = (TextView)findViewById(R.id.cb6);
            cb.setText(ccards.get(5));
        }
        else{
            cb = (TextView) findViewById(R.id.cb6);
            cb.setAlpha(0.0f);
        }
        if(hcards.size() >= 7) {
            hb = (TextView) findViewById(R.id.hb7);
            hb.setText(hcards.get(6));
        }
        else{
            hb = (TextView) findViewById(R.id.hb7);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 7){
            cb = (TextView)findViewById(R.id.cb7);
            cb.setText(ccards.get(6));
        }
        else{
            cb = (TextView) findViewById(R.id.cb7);
            cb.setAlpha(0.0f);
        }
        if(hcards.size() >= 8) {
            hb = (TextView) findViewById(R.id.hb8);
            hb.setText(hcards.get(7));
        }
        else{
            hb = (TextView) findViewById(R.id.hb8);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 8) {
            cb = (TextView) findViewById(R.id.cb8);
            cb.setText(ccards.get(7));
        }
        else{
            cb = (TextView) findViewById(R.id.cb8);
            cb.setAlpha(0.0f);
        }
        if(hcards.size() >= 9) {
            hb = (TextView) findViewById(R.id.hb9);
            hb.setText(hcards.get(8));
        }
        else{
            hb = (TextView) findViewById(R.id.hb9);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 9){
            cb = (TextView)findViewById(R.id.cb9);
            cb.setText(ccards.get(8));
        }
        else{
            cb = (TextView) findViewById(R.id.cb9);
            cb.setAlpha(0.0f);
        }
        if(hcards.size() >= 10) {
            hb = (TextView) findViewById(R.id.hb10);
            hb.setText(hcards.get(9));
        }
        else{
            hb = (TextView) findViewById(R.id.hb10);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 10){
            cb = (TextView)findViewById(R.id.cb10);
            cb.setText(ccards.get(9));
        }
        else{
            cb = (TextView) findViewById(R.id.cb10);
            cb.setAlpha(0.0f);
        }
        if(hcards.size() >= 11) {
            hb = (TextView) findViewById(R.id.hb11);
            hb.setText(hcards.get(10));
        }
        else{
            hb = (TextView) findViewById(R.id.hb11);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 11){
            cb = (TextView)findViewById(R.id.cb11);
            cb.setText(ccards.get(10));
        }
        else{
            cb = (TextView) findViewById(R.id.cb11);
            cb.setAlpha(0.0f);
        }
        if(hcards.size() >= 12) {
            hb = (TextView) findViewById(R.id.hb12);
            hb.setText(hcards.get(11));
        }
        else{
            hb = (TextView) findViewById(R.id.hb12);
            hb.setAlpha(0.0f);
        }
        if(ccards.size() >= 12){
            cb = (TextView)findViewById(R.id.cb12);
            cb.setText(ccards.get(11));
        }
        else{
            cb = (TextView) findViewById(R.id.cb12);
            cb.setAlpha(0.0f);
        }
    }

    /**
     This function continues tp the RoundDisplay activity when the button is clicked.
     @param v- a view holding the button's id.
     */
    public void continuetonext(View v){
        Intent i = new Intent(getApplicationContext(), RoundDisplay.class);

        i.putExtra("main_obj", local_main);
        startActivity(i);
    }
}