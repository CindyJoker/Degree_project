package com.example.android.degreepo;


import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Recording extends Activity implements RecordFilesAdapter.RecordFilesOnClickListener {

    private Button butt_start;
    private Button butt_stop;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean isRecording;

    private RecordFilesAdapter rAdepter;
    private RecyclerView rfileLists;
    private String[] rFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        setAdapterAndLayoutManager();

        InitialButtons();

        setOnClickListener();
        showFiles();

    }

    // Initial the buttons
    private void InitialButtons(){

        butt_start = findViewById(R.id.butt_record);
        butt_stop = findViewById(R.id.butt_stop);

        butt_stop.setEnabled(false);

    }

    // Set adapter and layout manager for recycler view
    private void setAdapterAndLayoutManager(){

        rfileLists = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rfileLists.setLayoutManager(layoutManager);
        rfileLists.setHasFixedSize(true);

        rAdepter = new RecordFilesAdapter(this);
        rfileLists.setAdapter(rAdepter);
    }

    private String[] getFiles() {
        // TODO(9): Get files' names in directory.
        String path;
        if (hasSDCard())
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/SoundRecorder/";
        else
            path = Environment.getDataDirectory().getAbsolutePath()+ "/SoundRecorder/";
        String[] fileNames = null;
        File f_list = new File(path);
        if(!f_list.exists())
            f_list.mkdir();
        else
            fileNames = f_list.list();

        return fileNames;
    }

    // Show files
    public void showFiles(){
        rFileName = getFiles();
        rAdepter.setFileNames(rFileName);
    }


    // Set OnClickListener
    private void setOnClickListener(){

        butt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord();
            }
        });

        butt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
            }
        });

    }

    @Override
    public void onDestroy(){

        if (isRecording){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }

        super.onDestroy();

    }

    private void playRecords(String fileName) {
        // TODO(7): Replay the recordings
        String path;
        if (hasSDCard())
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SoundRecorder/"+fileName;
        else
            path = Environment.getDataDirectory().getAbsolutePath()+"/SoundRecorder/"+fileName;

        // Set data source
        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (mediaPlayer.isPlaying())
            Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
    }

    private void stopRecord() {

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        isRecording = false;
        butt_start.setEnabled(true);
        butt_stop.setEnabled(false);
        Toast.makeText(Recording.this, "Recording end", Toast.LENGTH_SHORT).show();
        showFiles();

    }

    // TODO(6): Set files for recordings
    private File file;

    private void setFileNameAndPath() {
        String mFileName = getCurrentDateAndTime() + ".mp3";
        String mFilePath;
        if (hasSDCard()){
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SoundRecorder/" + mFileName;
        }else
            mFilePath = Environment.getDataDirectory().getAbsolutePath() + "/SoundRecorder/" + mFileName;
        //Toast.makeText(Recording.this, mFilePath, Toast.LENGTH_SHORT).show();
        file = new File(mFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    // Get current date and time
    private String getCurrentDateAndTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String dat = simpleDateFormat.format(date);
        return dat;
    }

    private void startRecord() {

        try{
            setFileNameAndPath();

            mediaRecorder = new MediaRecorder();
            // Recording use microphone
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // Set the format of recordings
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // Set the encoder format for recordings
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // Set the absolute path for recording files
            if (file.exists()){
                file.delete();
            }
            mediaRecorder.setOutputFile(file.getAbsolutePath());

            // Starting Record
            isRecording = true;
            mediaRecorder.prepare();
            mediaRecorder.start();
            butt_start.setEnabled(false);
            butt_stop.setEnabled(true);
            Toast.makeText(Recording.this, "Start recording",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Make sure there is a SD card
    private boolean hasSDCard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // Remove and Rename File
    /*public void fileExecution(final View view, final String fileName){

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_files_item, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.removeFile:
                        rAdepter.removeFiles(fileName);
                        //Toast.makeText(Recording.this, fileName, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.renameFile:
                        // TODO(17): Rename method
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplicationContext(), "Turn off popupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
        showFiles();

    }*/


    @Override
    public void AdapterOnClick(String fileName) {
        //TODO(10): Click, play the recording
        playRecords(fileName);
    }

    /*@Override
    public void AdapterLongOnClick(View view, String fileName) {
        fileExecution(view, fileName);
    }*/

}
