package com.example.android.degreepo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SchemePage extends AppCompatActivity {

    private Button butt_listen;
    private Button butt_recognize;
    private Button butt_practice;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_scheme);

        initButton();
        setOnclick();
    }

    // Initial buttons
    private void initButton(){
        butt_listen = findViewById(R.id.butt_listen);
        butt_recognize = findViewById(R.id.butt_recognize);
        butt_practice = findViewById(R.id.butt_practice);
    }

    // Set onclick event
    private void setOnclick(){
        butt_listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO(12): Turn to listening page
                Intent listen = new Intent(SchemePage.this, Listening.class);
                startActivity(listen);
            }
        });

        butt_recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO(13): Turn to recognizing page
                Intent recog = new Intent(SchemePage.this, Recognizing.class);
                startActivity(recog);
            }
        });

        butt_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO(14): Turn to recording page
                Intent record = new Intent(SchemePage.this, Recording.class);
                startActivity(record);
            }
        });
    }

}
