package com.example.smartgrid.service;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartgrid.R;
import com.example.smartgrid.Status;
import com.example.smartgrid.dto.ComponentStatus;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<ComponentStatus> {
    ArrayList<ComponentStatus> components = new ArrayList<>();
    ArrayList<String> componentNames = new ArrayList<>();
    Context context;

    public CustomArrayAdapter(Context context, ArrayList<ComponentStatus> components){
        super(context, R.layout.my_layout, components);
        this.context = context;
        this.components = components;

        for(ComponentStatus componentStatus: components){
            componentNames.add(componentStatus.getComponentName());
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.my_layout, null);

            TextView textView = convertView.findViewById(R.id.textView4);
            Button button = convertView.findViewById(R.id.button3);

            textView.setText(componentNames.get(position));
            String buttonText = "";
            int color = 2;

            if(components.get(position).isStatus()){
                buttonText = "Turn off";
                color = context.getResources().getColor(R.color.orange);
            }else{
                buttonText = "Turn on";
                color = context.getResources().getColor(R.color.offorange);
            }

            button.setText(buttonText);
            button.setBackgroundColor(color);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RemoteAccess remoteAccess = new RemoteAccess(context);
                    String url = context.getResources().getString(R.string.url);
                    remoteAccess.sendComponentStatus(componentNames.get(position),url+"/addOrUpdateComponentStatus");
                }
            });
        }
        return convertView;
    }
}
