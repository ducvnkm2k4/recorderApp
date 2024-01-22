package com.example.recordingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordRecyclerAdapter extends RecyclerView.Adapter<RecordRecyclerAdapter.RecordRecyclerViewHolder> {
    private final Context context;
    private final List<Record> recordList;
    private int selectedPosition = -1;
    private final RecordDatabase database;

    public RecordRecyclerAdapter(Context context, List<Record> recordList, RecordDatabase database) {
        this.context = context;
        this.recordList = recordList;
        this.database = database;
    }

    @NonNull
    @Override
    public RecordRecyclerAdapter.RecordRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_record_rcl_view,parent,false);
        return new RecordRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordRecyclerAdapter.RecordRecyclerViewHolder holder, int position) {
        holder.setData(recordList.get(position));
        holder.setVisibilityLayout(position);
        holder.toggleButton.setChecked(position == selectedPosition);
        holder.setImg_renameRecordListener(recordList.get(position));
        holder.setImg_garbageBinListener(position);
        holder.setTv_speedListener();
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class RecordRecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_nameRecord;
        private final TextView tv_duration;
        private final TextView tv_dateSave;
        private final SeekBar seekBar;
        private final TextView tv_speed;
        private final ImageView img_garbageBin;
        private final ImageView img_renameRecord;
        private final RelativeLayout layout;
        private final ToggleButton toggleButton;
        public RecordRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nameRecord=itemView.findViewById(R.id.tv_nameRecord);
            tv_duration = itemView.findViewById(R.id.tv_durationOfRecord);
            tv_dateSave = itemView.findViewById(R.id.tv_dateSaveRecord);
            seekBar  = itemView.findViewById(R.id.seekBar_durationRecord);
            tv_speed = itemView.findViewById(R.id.tv_speedRecord);
            img_garbageBin = itemView.findViewById(R.id.img_garbageBin);
            img_renameRecord = itemView.findViewById(R.id.img_renameRecord);
            layout=itemView.findViewById(R.id.layout_item);
            toggleButton = itemView.findViewById(R.id.toggleButton);
        }
        public void setData(Record record){
            tv_nameRecord.setText(record.getNameRecord());
            tv_duration.setText(Record.formatDuration(record.getDuration()));
            tv_dateSave.setText(Record.formatDate(record.getDateSave()));
        }
        @SuppressLint("NotifyDataSetChanged")
        public void setVisibilityLayout(int position) {
            toggleButton.setOnCheckedChangeListener(null);

            toggleButton.setChecked(position == selectedPosition);

            toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                } else {
                    selectedPosition = -1;
                    notifyDataSetChanged();
                }
            });

            layout.setVisibility(selectedPosition == position ? View.VISIBLE : View.GONE);
        }
        public void setImg_renameRecordListener(Record record){
            img_renameRecord.setOnClickListener(v -> {
                showDialogRename(new EditText(context),record);
            });
        }
        private void showDialogRename(@NonNull EditText editText, Record record){
            editText.setSingleLine(true);
            editText.setOnKeyListener((v, keyCode, event) -> keyCode== KeyEvent.KEYCODE_ENTER);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Đặt lại tên bản ghi");
            builder.setView(editText);
            builder.setPositiveButton("Xác nhận",(dialog, which) -> {
                String newName= editText.getText().toString();
                String oldName = tv_nameRecord.getText().toString().trim();
                if(newName.isEmpty()) {
                    tv_nameRecord.setText(oldName);
                    record.setNameRecord(oldName);
                }
                else {
                    tv_nameRecord.setText(newName);
                    record.setNameRecord(newName);
                }
                database.updateNameRecord(record);
            });
            builder.setNegativeButton("Hủy",(dialog, which) -> dialog.cancel());
            builder.setOnCancelListener(DialogInterface::cancel);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        public void setImg_garbageBinListener(int position){
            img_garbageBin.setOnClickListener(v -> showDialogDeleteRecord(position));
        }
        private void showDialogDeleteRecord(int position){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("thông báo");
            builder.setMessage("bạn có chắc chắn muốn xóa bản ghi âm này");
            builder.setPositiveButton("xác nhận",(dialog, which) -> {
                database.deleteRecord(recordList.get(position));
                recordList.remove(position);
                notifyItemRemoved(position);
            });
            builder.setNegativeButton("Hủy",(dialog, which) -> dialog.cancel());
            builder.setOnCancelListener(DialogInterface::cancel);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        public void setTv_speedListener(){
            tv_speed.setOnClickListener(v -> {
                double speed = Double.parseDouble(tv_speed.getText().toString());
                if(speed==2.0)
                    speed=0.5;
                else speed+=0.5;
                tv_speed.setText(String.valueOf(speed));
            });
        }
    }
}
