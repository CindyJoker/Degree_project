package com.example.android.degreepo;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.degreepo.CustomAPI.NestedRadioGroup;

import java.util.Random;


public class Listening extends AppCompatActivity {

    private NestedRadioGroup lradioGroup;

    private ImageButton imageButton;
    private MediaPlayer lmediaPlayer;

    private TextView listenResult;

    private int[] tones = {R.raw.s1, R.raw.s2, R.raw.s3, R.raw.s4};
    private int num = 4;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.signikanegative_regular);
        listenResult = findViewById(R.id.listen_result);
        listenResult.setTypeface(typeface);

        setButtons();
        setClickEvent();
    }

    public void setButtons(){
        lradioGroup = findViewById(R.id.listen_group);
        imageButton = findViewById(R.id.listen_play);
    }

    public void setClickEvent(){

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO(15): Random play a record
                randomPlay();
            }
        });

        // TODO(16): Check the choice
        lradioGroup.setOnCheckedChangeListener(new radioGroupListener());

    }

    int count;
    private void randomPlay(){

        // Pick a file from raw directory
        Random r = new Random();
        count = r.nextInt(num);
        play();

    }

    private void play(){
        try{
            lmediaPlayer = new MediaPlayer();
            lmediaPlayer.reset();
            lmediaPlayer = MediaPlayer.create(getApplicationContext(), tones[count]);
            lmediaPlayer.start();
            if (lmediaPlayer.isPlaying()) {
                imageButton.setEnabled(false);
                Toast.makeText(this, "Is playing", Toast.LENGTH_SHORT).show();
            }
            //lmediaPlayer.release();
            imageButton.setEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // Radio button listener
    class radioButtonListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            System.out.println("Checked " + b);
        }
    }

    // Check correct or wrong
    class radioGroupListener implements NestedRadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(NestedRadioGroup group, int checkedId){

            switch (count+1){
                case 1:
                    if (checkedId == R.id.rbutt1)
                        listenResult.setText("Excellent! You can try the next");
                    else
                        listenResult.setText("Sorry~ Please try again");
                    break;
                case 2:
                    if (checkedId == R.id.rbutt2)
                        listenResult.setText("Excellent! You can try the next");
                    else
                        listenResult.setText("Sorry~ Please try again");
                    break;
                case 3:
                    if (checkedId == R.id.rbutt3)
                        listenResult.setText("Excellent! You can try the next");
                    else
                        listenResult.setText("Sorry~ Please try again");
                    break;
                case 4:
                    if (checkedId == R.id.rbutt4)
                        listenResult.setText("Excellent! You can try the next");
                    else
                        listenResult.setText("Sorry~ Please try again");
                    break;
            }
        }
    }

}
