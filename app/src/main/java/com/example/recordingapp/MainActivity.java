package com.example.recordingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private ImageView imgSetting;
    private RecyclerView recyclerView;
    private ImageView imgNewRecord;
    private RecordRecyclerAdapter recyclerAdapter;
    private List<Record> list;
    private RecordDatabase database;
    private static final int REQUEST_CODE_NEW_RECORD=99;
    private static final int RESULT_CODE_NEW_RECORD=99;
    private static final int REQUEST_PERMISSIONS_CODE=1;
    private ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_CODE_NEW_RECORD){
                        Record record = (Record) result.getData().getSerializableExtra("resultRecordObject");
                        list.add(0,record);
                        recyclerAdapter.notifyItemInserted(0);
                        database.insertData(record);
                    }
                }
            });
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
        database = new RecordDatabase(this);
        list = database.getData();
        recyclerAdapter = new RecordRecyclerAdapter(list,MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(recyclerAdapter);
        //set events
        createNewRecord();
        requestPermissions();
    }
    private int findMinIdRecord() {
        List<Record> listRecord = new ArrayList<>(list);
        listRecord.sort((o1, o2) -> o1.getId() - o2.getId());
        for (int i = 0; i < listRecord.size(); i++) {
            if(i==listRecord.size()-1) return listRecord.get(i).getId()+1;
            if (listRecord.get(i).getId() + 1 != listRecord.get(i + 1).getId())
                return i + 1;
        }
        return 1;
    }
    private void createNewRecord(){
        imgNewRecord.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecordActivity.class);
            int idNewRecord=findMinIdRecord();
            Record record =new Record(idNewRecord,Record.formatDefaultNameRecord(idNewRecord),null,0,new Date());
            intent.putExtra("recordObject",record);
            resultLauncher.launch(intent);
        });
    }
    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        boolean allPermissionsGranted = true;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            boolean allPermissionsGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted)
                showDialogIfUserDenyPermission();
        }
    }
    private void showDialogIfUserDenyPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("thông báo");
        builder.setMessage(R.string.MessageDenyPermission);
        builder.setPositiveButton("xác nhận",(dialog, which) -> dialog.cancel());
        builder.setOnCancelListener(DialogInterface::cancel);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}