package com.example.recordingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordRecyclerAdapter extends RecyclerView.Adapter<RecordRecyclerAdapter.RecordRecyclerViewHolder> {
    private List<Record> recordList;
    private Context context;

    public RecordRecyclerAdapter(List<Record> recordList, Context context) {
        this.recordList = recordList;
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class RecordRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_nameRecord;
        private TextView tv_duration;
        private TextView tv_dateSave;
        private SeekBar seekBar;
        private TextView tv_speech;
        private ImageView img_garbageBin;
        private ImageView img_renameRecord;
        private RelativeLayout layout;
        private ToggleButton toggleButton;
        public RecordRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nameRecord=itemView.findViewById(R.id.tv_nameRecord);
            tv_duration = itemView.findViewById(R.id.tv_durationOfRecord);
            tv_dateSave = itemView.findViewById(R.id.tv_dateSaveRecord);
            seekBar  = itemView.findViewById(R.id.seekBar_durationRecord);
            tv_speech = itemView.findViewById(R.id.tv_speechRecord);
            img_garbageBin = itemView.findViewById(R.id.img_garbageBin);
            img_renameRecord = itemView.findViewById(R.id.img_renameRecord);
            layout=itemView.findViewById(R.id.layout_item);
            toggleButton = itemView.findViewById(R.id.toggleButton);
        }
        public void setData(Record record){
            tv_nameRecord.setText(record.getNameRecord());
            tv_duration.setText(record.formatDuration(record.getDuration()));
            tv_dateSave.setText(record.formatDate(record.getDateSave()));
            toggleButton.setChecked(record.isVisible());
        }
        @SuppressLint("NotifyDataSetChanged")
        public void setVisibilityLayout(int position){
            toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Record currentRecord = recordList.get(getAdapterPosition());
                if (isChecked) {
                    currentRecord.setVisible(true);
                    for (int i = 0; i < recordList.size(); i++) {
                        if (i != position) {
                            recordList.get(i).setVisible(false);
                        }
                    }
                } else {
                    currentRecord.setVisible(false);
                }
                notifyDataSetChanged();
            });
            layout.setVisibility(recordList.get(position).isVisible() ? View.VISIBLE : View.GONE);
        }
    }
}
