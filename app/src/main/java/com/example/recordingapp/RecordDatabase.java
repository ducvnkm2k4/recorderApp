package com.example.recordingapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordDatabase extends SQLiteOpenHelper {
    private static final String NAME_DATABASE="RecordDatabase";
    private static final String NAME_TABLE="tblRecord";
    private static final String COLUMN_ID="iId";
    private static final String COLUMN_NAME_RECORD="sNameRecord";
    private static final String COLUMN_PATH="sPath";
    private static final String COLUMN_DURATION_RECORD="iDuration";
    private static final String COLUMN_DATE_SAVE="dDateSave";
    public RecordDatabase(@Nullable Context context) {
        super(context, NAME_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE IF NOT EXISTS "+NAME_TABLE
            + "(" +
                COLUMN_ID + " INT PRIMARY KEY,"+
                COLUMN_NAME_RECORD +" STRING,"+
                COLUMN_PATH + " STRING,"+
                COLUMN_DURATION_RECORD +" INT,"+
                COLUMN_DATE_SAVE +" DATETIME "+
            ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    public boolean insertData(Record record){
        try {
            SQLiteDatabase database = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID,record.getId());
            values.put(COLUMN_NAME_RECORD,record.getNameRecord());
            values.put(COLUMN_PATH,record.getPath());
            values.put(COLUMN_DURATION_RECORD,record.getDuration());
            values.put(COLUMN_DATE_SAVE,Record.formatDate(record.getDateSave()));
            database.insert(NAME_TABLE,null,values);
            database.close();
            return true;
        }catch (Exception e){
            Log.e("errorInsert",e.getMessage());
            return false;
        }
    }
    public void insertListData(ArrayList<Record> list){
        for(Record record:list) insertData(record);
    }
    @SuppressLint("Range")
    public List<Record> getData(){
        List<Record> recordList = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] listColumn={COLUMN_ID,COLUMN_NAME_RECORD,COLUMN_PATH,COLUMN_DURATION_RECORD,COLUMN_DATE_SAVE};
            Cursor cursor = db.query(NAME_TABLE,listColumn,null,null,null,null,null);
            if(cursor!=null){
                while (cursor.moveToNext()){
                     int id=cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                     String nameRecord = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_RECORD));
                     String path=cursor.getString(cursor.getColumnIndex(COLUMN_PATH));
                     int duration=cursor.getInt(cursor.getColumnIndex(COLUMN_DURATION_RECORD));
                     Date date = new Date(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_SAVE)));
                     recordList.add(0,new Record(id,nameRecord,path,duration,date));
                }
                cursor.close();
            }
            db.close();
        }catch (Exception e){
            Log.e("error select data",e.getMessage());
        }
        return recordList;
    }
    public void deleteRecord(Record record){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            db.delete(NAME_TABLE,COLUMN_ID+"=?",new String[]{String.valueOf(record.getId())});
            db.close();
        }catch (Exception e){
            Log.e("error delete record database",e.getMessage());
        }
    }
    public void updateNameRecord(Record record){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_RECORD,record.getNameRecord());
            db.update(NAME_TABLE,values,COLUMN_ID+"=?",new String[]{String.valueOf(record.getId())});
            db.close();
        }catch (Exception e){
            Log.e("error update name record",e.getMessage());
        }
    }
}
