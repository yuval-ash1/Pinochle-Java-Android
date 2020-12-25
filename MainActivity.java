package edu.ramapo.yashken1.pinochle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable{

   // private static final String tag = MainActivity.class.getName();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     This function handles the user button click, and continues the the appropriate next activity
        (new game/ loaded game).
     @param v- a View holding the id of the user's button choice.
     */
    public void new_or_load(View v){
        Main m = new Main();
        if (v.getId() == R.id.startGame) {
            m.set_user_input(1);
            //m.initialize_game();
            //getIntent().putExtra("object", m);
            Intent i = new Intent(getApplicationContext(), NewGame.class);
            i.putExtra("object", m);
            startActivity(i);
        }
        if(v.getId() == R.id.loadGame) {
            m.set_user_input(2);
            //m.initialize_game();
            Intent i = new Intent(getApplicationContext(), LoadedGame.class);
            i.putExtra("object", m);
            startActivity(i);
        }
    }

}