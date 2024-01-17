package com.example.recordingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.recordingapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private ImageView imgSetting;
    private RecyclerView recyclerView;
    private ImageView imgNewRecord;
    private RecordRecyclerAdapter recyclerAdapter;
    private List<Record> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //find id
        searchView = findViewById(R.id.searchView);
        imgSetting = findViewById(R.id.img_setting);
        recyclerView = findViewById(R.id.rcl_itemRecord);
        imgNewRecord = findViewById(R.id.img_newRecord);
        //init object
        list = new ArrayList<>();
        list.add(new Record("1","vzfs",7913,new Date("17/01/2023 20:31")));
        list.add(new Record("2","vzfs",7913,new Date("17/01/2023 20:31")));
        recyclerAdapter = new RecordRecyclerAdapter(list,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(recyclerAdapter);
        //set events
        createNewRecord();
    }
    private void createNewRecord(){
        imgNewRecord.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewRecordActivity.class);
            startActivity(intent);
        });
    }
}