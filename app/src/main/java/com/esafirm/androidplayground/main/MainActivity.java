package com.esafirm.androidplayground.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.esafirm.androidplayground.R;
import com.esafirm.androidplayground.dagger.example.DaggerSampleAct;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainListAdapter adapter = new MainListAdapter(this);
        adapter.setData(Arrays.asList(
                "Dagger Example"
        ));

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        DaggerSampleAct.start(context);
                        break;
                }
            }
        });
    }
}
