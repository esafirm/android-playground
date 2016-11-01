package com.esafirm.androidplayground.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class MenuFactory {
    public static View create(Context context, List<String> data, final OnNavigatePage onNavigatePage) {
        ListView listView = new ListView(context);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        MenuListAdapter menuListAdapter = new MenuListAdapter(context);
        menuListAdapter.setData(data);
        listView.setAdapter(menuListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (onNavigatePage == null) {
                    throw new NullPointerException("OnNavigatePage must be not null");
                }
                onNavigatePage.navigate(i);
            }
        });

        return listView;
    }
}
