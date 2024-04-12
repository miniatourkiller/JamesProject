package com.example.smartgrid.service;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartgrid.R;
import com.example.smartgrid.dto.HistoryDto;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CustomArrayListAdapter2 extends ArrayAdapter<HistoryDto> {
    private Context context;
    private ArrayList<HistoryDto> historyDtos;

   public CustomArrayListAdapter2(Context context, ArrayList<HistoryDto> historyDtos){
        super(context, R.layout.my_history_layout, historyDtos);
        this.context = context;
        this.historyDtos = historyDtos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.my_history_layout, null);

            TextView timestamp = convertView.findViewById(R.id.textView7);
            TextView readings = convertView.findViewById(R.id.textView8);
            HistoryDto historyDto = historyDtos.get(position);

            timestamp.setText(historyDto.getTimeStamp());
            readings.setText(historyDto.getReadings());
        }

        return convertView;
    }
}
