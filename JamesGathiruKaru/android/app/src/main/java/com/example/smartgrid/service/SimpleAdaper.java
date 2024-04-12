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

import java.util.ArrayList;

public class SimpleAdaper extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> myreadings;
    public SimpleAdaper(Context context, ArrayList<String> myreadings){
        super(context, R.layout.my_simple_layout, myreadings);
        this.context = context;
        this.myreadings = myreadings;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_simple_layout, null);

            TextView textView = convertView.findViewById(R.id.readings);
            textView.setText(myreadings.get(position));
        }
        return convertView;
    }
}
