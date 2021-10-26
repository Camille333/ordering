package com.example.ordering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

public class MyAdapter extends ArrayAdapter {
    int recourceId;
    public MyAdapter(@NonNull Context context, int resource, @NonNull List<HashMap<String,String>> objects) {
        super(context, resource, objects);
        recourceId = resource;
    }

    @NonNull
    @Override
    //重写getView方法
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HashMap<String,String> hashMap = (HashMap<String, String>) getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(recourceId, parent, false);
        TextView store_name = view.findViewById(R.id.item_name);
        TextView address = view.findViewById(R.id.item_detail);

        /*
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(recourceId, parent, false);
        }
        Item item = (Item) getItem(position);
        TextView name = itemView.findViewById(R.id.item_name);
        TextView country_rate = itemView.findViewById(R.id.item_detail);
        */

        store_name.setText(hashMap.get("store_name"));
        address.setText(hashMap.get("address"));

        return view;
    }

}