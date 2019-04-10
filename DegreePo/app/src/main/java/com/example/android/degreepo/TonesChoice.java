package com.example.android.degreepo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TonesChoice extends Activity {

    private Button tone1;
    private Button tone2;
    private Button tone3;
    private Button tone4;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tone_choice);

        findById();
        setOnclickListener();

    }

    private void setOnclickListener() {

        tone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO(8): Turn to the tone's page. The other three are too.
            }
        });
        tone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Turn to the tone's page.
            }
        });
        tone3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Turn to the tone's page.
            }
        });
        tone4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Turn to the tone's page.
            }
        });

    }

    // Initial the buttons
    public void findById(){

        tone1 = findViewById(R.id.data_butt_tone1);
        tone2 = findViewById(R.id.data_butt_tone2);
        tone3 = findViewById(R.id.data_butt_tone3);
        tone4 = findViewById(R.id.data_butt_tone4);

    }

}
