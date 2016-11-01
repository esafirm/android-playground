package com.esafirm.androidplayground.common;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class MenuListAdapter extends ArrayAdapter<String> {

    public MenuListAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    public void setData(List<String> data) {
        clear();
        addAll(data);
        notifyDataSetChanged();
    }
}
