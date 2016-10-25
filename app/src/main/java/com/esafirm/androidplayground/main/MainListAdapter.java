package com.esafirm.androidplayground.main;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class MainListAdapter extends ArrayAdapter<String> {
    public MainListAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    public void setData(List<String> data){
        clear();
        addAll(data);
        notifyDataSetChanged();
    }
}
