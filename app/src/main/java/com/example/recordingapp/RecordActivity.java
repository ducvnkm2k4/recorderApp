package com.example.recordingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class RecordActivity extends AppCompatActivity {
    private ImageView img_back;
    private ImageView img_save;
    private TextView tv_titleNameNewRecord;
    private TextView tv_durationRecord;
    private ToggleButton toggleButton;
    private Record record;
    private Handler handler;
    private Runnable runnable;
    private boolean isRecording = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //find id
        img_back = findViewById(R.id.img_back);
        tv_titleNameNewRecord = findViewById(R.id.tv_titleNameNewRecord);
        tv_durationRecord = findViewById(R.id.tv_durationRecord);
        toggleButton = findViewById(R.id.btn_startAndStopNewRecord);
        img_save = findViewById(R.id.img_saveNewRecord);
        createView();
        setEventClickImgBack();
        setEventClickImgSaveNewRecord();
    }
    private void createView(){
        getObjectFromIntent();
        setTitleActivity();
    }
    private void getObjectFromIntent(){
        Intent intent = getIntent();
        record = (Record) intent.getSerializableExtra("recordObject");
    }
    private void setTitleActivity(){
        tv_titleNameNewRecord.setText(record.getNameRecord());
    }
    private void setEventClickImgBack(){
        img_back.setOnClickListener(v -> showDialog());
    }
    private void saveRecordAndSendMainActivity(){
        Intent intent = new Intent();
        intent.putExtra("resultRecordObject",record);
        setResult(99,intent);
        finish();
    }
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Lưu bản ghi");
        builder.setPositiveButton("Lưu",((dialog, which) -> {
            saveRecordAndSendMainActivity();
        }));
        builder.setNegativeButton("Hủy",((dialog, which) -> dialog.cancel()));
        builder.setNeutralButton("Loại bỏ",((dialog, which) -> finish()));
        builder.setOnCancelListener(dialog -> {dialog.cancel();});
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void setEventClickImgSaveNewRecord(){
        img_save.setOnClickListener(v -> saveRecordAndSendMainActivity());
    }
}