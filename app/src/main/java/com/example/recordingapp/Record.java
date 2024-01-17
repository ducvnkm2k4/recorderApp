package com.example.recordingapp;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {
    private int id;
    private String nameRecord;
    private String path;
    private int duration;
    private Date dateSave;
    private boolean isVisible = false;

    public Record(String nameRecord, String path, int duration, Date dateSave) {
        this.nameRecord = nameRecord;
        this.path = path;
        this.duration = duration;
        this.dateSave = dateSave;
    }

    public String getNameRecord() {
        return nameRecord;
    }

    public void setNameRecord(String nameRecord) {
        this.nameRecord = nameRecord;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }
    public boolean isVisible() {
        return this.isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
    @SuppressLint("DefaultLocale")
    public String formatDuration(int duration){
        int hour=duration/3600;
        int minute=(duration-hour*3600)/60;
        int second=(duration-hour*3600-minute*60);
        return String.format("%02d:%02d:%02d",hour,minute,second);
    }
    public String formatDate(Date date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        return String.valueOf(dateFormat.format(date));
    }
}
