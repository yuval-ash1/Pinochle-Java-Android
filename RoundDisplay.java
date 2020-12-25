package edu.ramapo.yashken1.pinochle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class RoundDisplay extends AppCompatActivity implements Serializable {

    Main local_main =  new Main();
    Game local_game;// = new Game();
    Human p1;// = new Human("");
    Computer p2;// = new Computer("");
    Round local_round;// = new Round();
    Card lead = new Card();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_display);
        int i;
        local_main = (Main) getIntent().getSerializableExtra("main_obj");
        local_game = local_main.get_game_object();
        p1 = local_main.get_human_object();
        p2 = local_main.get_computer_object();
        local_round = local_game.get_round_object();
        local_round.initialize_round();

        TextView myReason = findViewById(R.id.welcomer);
        myReason.setText("Welcome to round number " + local_game.round_number + "!");


        String trumpstr = "";
        Card temptrump = local_round.get_deck_object().get_trump();
        trumpstr += temptrump.get_type();
        trumpstr += temptrump.get_suit();
        trumpstr = trumpstr.toUpperCase();
        trumpstr = "Trump: " + trumpstr;
        TextView trumptext = findViewById(R.id.trumpdisplay);
        trumptext.setText(trumpstr);

        String deckstr =  "";
        List<Card> tempdeck = local_round.get_deck_object().get_deck();
        for(i = 0; i < tempdeck.size(); i++){
            deckstr = deckstr + tempdeck.get(i).get_type() + tempdeck.get(i).get_suit();
            if (i != tempdeck.size()-1)
                deckstr += ", ";
            if (i==15)
                deckstr += "\n\t\t  ";
        }
        deckstr = deckstr.toUpperCase();
        deckstr = "Deck: " + deckstr;
        TextView decktext = findViewById(R.id.deckdisplay);
        decktext.setText(deckstr);


        String chand =  "";
        List<Card> tempchand = p2.get_hand();
        for(i = 0; i < tempchand.size(); i++){
            chand = chand + tempchand.get(i).get_type() + tempchand.get(i).get_suit();
            if (i != tempchand.size()-1)
                chand += ", ";
            if (i==15)
                chand += "\n\t\t  ";
        }
        chand = chand.toUpperCase();
        chand = "Hand: " + chand;
        TextView chandtxt = findViewById(R.id.computerhand);
        chandtxt.setText(chand);

        String ccapture =  "";
        List<Card> tempccapture = p2.get_capture_pile();
        for(i = 0; i < tempccapture.size(); i++){
            ccapture = ccapture + tempccapture.get(i).get_type() + tempccapture.get(i).get_suit();
            if (i != tempccapture.size()-1)
                ccapture += ", ";
            if (i==15)
                ccapture += "\n\t\t  ";
        }
        ccapture = ccapture.toUpperCase();
        ccapture = "Capture Pile: " + ccapture;
        TextView ccapturetxt = findViewById(R.id.computercapture);
        ccapturetxt.setText(ccapture);

        String cmelds =  "";
        List<String> tempcmelds = p2.get_previous_melds_cards();
        for(i = 0; i < tempcmelds.size(); i++){
            cmelds = cmelds + tempcmelds.get(i);
            if (i != tempcmelds.size()-1)
                cmelds += ", ";
            if (i==15)
                cmelds += "\n\t\t  ";
        }
        cmelds = cmelds.toUpperCase();
        cmelds = "Melds: " + cmelds;
        TextView cmeldstxt = findViewById(R.id.computermelds);
        cmeldstxt.setText(cmelds);

        String str = String.valueOf(p2.get_round_score());
        String scorestr = "Score: " + str;
        TextView cscoretxt = findViewById(R.id.computerdisplayscore);
        cscoretxt.setText(scorestr);


        String hhand =  "";
        List<Card> temphhand = p1.get_hand();
        for(i = 0; i < temphhand.size(); i++){
            hhand = hhand + temphhand.get(i).get_type() + temphhand.get(i).get_suit();
            if (i != temphhand.size()-1)
                hhand += ", ";
            if (i==15)
                hhand += "\n\t\t  ";
        }
        hhand = hhand.toUpperCase();
        hhand = "Hand: " + hhand;
        TextView hhandtxt = findViewById(R.id.humanhand);
        hhandtxt.setText(hhand);

        String hcapture =  "";
        List<Card> temphcapture = p1.get_capture_pile();
        for(i = 0; i < temphcapture.size(); i++){
            hcapture = hcapture + temphcapture.get(i).get_type() + temphcapture.get(i).get_suit();
            if (i != temphcapture.size()-1)
                hcapture += ", ";
            if (i==15)
                hcapture += "\n\t\t  ";
        }
        hcapture = hcapture.toUpperCase();
        hcapture = "Capture Pile: " + hcapture;
        TextView hcapturetxt = findViewById(R.id.humancapture);
        hcapturetxt.setText(hcapture);

        String hmelds =  "";
        List<String> temphmelds = p1.get_previous_melds_cards();
        for(i = 0; i < temphmelds.size(); i++){
            hmelds = hmelds + temphmelds.get(i);
            if (i != temphmelds.size()-1)
                hmelds += ", ";
            if (i==15)
                hmelds += "\n\t\t  ";
        }
        hmelds = hmelds.toUpperCase();
        hmelds = "Melds: " + hmelds;
        TextView hmeldstxt = findViewById(R.id.humanmelds);
        hmeldstxt.setText(hmelds);


        TextView hscoretxt = findViewById(R.id.humandisplayscore);
        hscoretxt.setText("Score: " + p1.get_round_score());

    }

    /**
     These function continues to the next appropriate activity on a buttons click
        (after viewing the round details).
     @param v- a View holding the button's id.
     */
    public void continuing(View v){
        if((!(p1.get_hand().isEmpty()) && !(p2.get_hand().isEmpty()))) {
            Intent i = new Intent(getApplicationContext(), UserDisplay.class);

            i.putExtra("main_obj", local_main);
            i.putExtra("lead_card", lead);
            startActivity(i);
        }
        else{
            Intent i = new Intent(getApplicationContext(), AnotherRound.class);

            i.putExtra("main_obj", local_main);
            startActivity(i);
        }
    }
}