package edu.ramapo.yashken1.pinochle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;

public class LoadedGame extends AppCompatActivity implements Serializable {

    Main local_main =  new Main();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_game);
        local_main = (Main) getIntent().getSerializableExtra("object");



    }
    /**
     This function handles the user button click, and continues the the appropriate next activity.
     @param v- a View holding the text that the user inputted (fileName).
     */
    public void fileNameEntered(View v){
        EditText text = (EditText)findViewById(R.id.editTextTextPersonName);
        String value = text.getText().toString();
        local_main.set_fileName(value);
        local_main.initialize_game();
        Intent i = new Intent(getApplicationContext(), RoundDisplay.class);
        i.putExtra("main_obj", local_main);
        startActivity(i);

    }
}

