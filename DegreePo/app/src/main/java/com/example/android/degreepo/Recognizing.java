package com.example.android.degreepo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.mathworks.toolbox.javabuilder.MWCharArray;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
*/
import com.mathworks.toolbox.javabuilder.MWCharArray;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWComplexity;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import DrawLineChart.LineChart;
import FinalPro.finalPro;

//import finalpro.pro;

public class Recognizing extends AppCompatActivity {

    private ImageView toneImage;
    private ImageButton img_butt_start, img_butt_next;
    private Button listenBack, recogDetail;
    private MediaRecorder recogMediaRecorder;
    private boolean isRecording = false;
    private int flag = 0;
    private TextView yourScore, highestScore, recog_yourScore, recog_highestScore, recordingHint;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizing);

        // Set font
        Typeface typeface = ResourcesCompat.getFont(this, R.font.signikanegative_regular);

        initTextView(typeface);

        recordingHint = findViewById(R.id.recog_hint);

        initButtons();
        setOnclickListener();
        changeImage();

    }

    // Init buttons
    public void initButtons(){
        img_butt_start = findViewById(R.id.recog_play);
        img_butt_start.setEnabled(true);
        img_butt_next = findViewById(R.id.recog_next);
        listenBack = findViewById(R.id.butt_play_back);
        recogDetail = findViewById(R.id.recog_detail);
    }

    // Init text view
    public void initTextView(Typeface typeface){
        recog_yourScore = findViewById(R.id.recog_your_score);
        recog_highestScore = findViewById(R.id.recog_highest_score);
        yourScore = findViewById(R.id.recog_score);
        highestScore = findViewById(R.id.highest_score);

        recog_yourScore.setTypeface(typeface);
        recog_highestScore.setTypeface(typeface);
        yourScore.setTypeface(typeface);
        highestScore.setTypeface(typeface);
    }

    // Set onClickListener
    @SuppressLint("ClickableViewAccessibility")
    public void setOnclickListener(){

        // Click to start record, stop record and replay
        img_butt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag){
                    case 0:
                        flag = 1;
                        startRecording();
                        recordingHint.setText(R.string.click_hint_stop);
                        break;
                    case 1:
                        flag = 0;
                        stopRecording();
                        try {
                            showScore();
                        } catch (MWException e) {
                            e.printStackTrace();
                        }
                        recordingHint.setText(R.string.click_hint_start);
                        break;
                }
            }
        });

        img_butt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });

        listenBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO(18): Play at this activity

                // Just play at practicing page at this time
                playBack();
            }
        });

        recogDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO(21): Look the diagram of recording
                // Turn to new activity
                Intent recog_detail = new Intent();
            }
        });

    }

    @Override
    public void onDestroy(){

        if (isRecording){
            recogMediaRecorder.stop();
            recogMediaRecorder.release();
            recogMediaRecorder = null;
        }

        super.onDestroy();

    }

    // Recording
    public void startRecording(){
        try{
            setFileNameAndPath();

            recogMediaRecorder = new MediaRecorder();
            // Recording use microphone
            recogMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // Set the format of recordings
            recogMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // Set the encoder format for recordings
            recogMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // Set the absolute path for recording files
            if (file.exists()){
                file.delete();
            }
            recogMediaRecorder.setOutputFile(file.getAbsolutePath());

            // Starting Record
            isRecording = true;
            recogMediaRecorder.prepare();
            recogMediaRecorder.start();
            img_butt_start.setActivated(true);
            Toast.makeText(Recognizing.this, "Start recording",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Stop
    public void stopRecording(){
        recogMediaRecorder.stop();
        recogMediaRecorder.release();
        recogMediaRecorder = null;
        isRecording = false;
        img_butt_start.setActivated(false);
        Toast.makeText(Recognizing.this, "Recording end", Toast.LENGTH_SHORT).show();
    }

    // Play back
    public void playBack(){
        Intent practice = new Intent(Recognizing.this, Recording.class);
        startActivity(practice);
    }

    // TODO(19): Change image randomly
    public void changeImage(){

        toneImage = findViewById(R.id.random_image);
        int images[] = {
                R.drawable.ma1, R.drawable.ma2,
                R.drawable.ma3, R.drawable.ma4,
                R.drawable.bai1, R.drawable.bai2,
                R.drawable.bai3, R.drawable.bai4
        };

        Random random = new Random();
        int index = random.nextInt(8);

        toneImage.setImageResource(images[index]);
    }

    // Set file name and path
    private File file;

    private void setFileNameAndPath() {
        String recogFileName = getFileName() + ".mp3";
        String recogFilePath;
        if (hasSDCard()){
            recogFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SoundRecorder/" + recogFileName;
        }else
            recogFilePath = Environment.getDataDirectory().getAbsolutePath() + "/SoundRecorder/" + recogFileName;
        file = new File(recogFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    // Make sure there is a SD card
    private boolean hasSDCard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // Get file name
    private String getFileName(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date(System.currentTimeMillis());
        String fileName = simpleDateFormat.format(date);
        fileName = "Your record "+ fileName;
        return fileName;
    }

    Double score;
    // Show your scores
    private void showScore() throws MWException {
        // TODO(20): Get drawable resource name
        finalPro f = new finalPro();
        Object[] grade;
        // Get tone
        String imgName = getResources().getResourceName(toneImage.getId());
        int lastDigit = imgName.charAt(imgName.length()-1);

        // Get path of this recording file
        if (file != null) {
            String path = file.getAbsolutePath();
            MWNumericArray tone = new MWNumericArray(lastDigit, MWClassID.INT16);
            MWCharArray p = new MWCharArray(path);
            Object[] o = new Object[2];
            o[0] = p;
            o[1] = tone;
            grade = f.finalpro(1, o);
            yourScore.setText(grade.toString());
            score = Double.parseDouble(grade.toString());
        }
    }

    private Queue<Double> queue = new LinkedList<Double>();
    // Show line chart
    private void showLineChart() throws MWException {
        // Queue<Double> queue = new LinkedList<Double>();
        queue.offer(score);

        int[] dims = { 1, queue.size() };
        LineChart f = new LineChart();
        int j=1;
        MWNumericArray input = MWNumericArray.newInstance(dims, MWClassID.DOUBLE,
                MWComplexity.REAL);
        for(Double q : queue) {
            input.set(j, q);
            j++;
        }
        f.DrawLineChart(input);
        f.waitForFigures();
    }

}
