package com.esafirm.androidplayground.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esafirm.androidplayground.common.MenuFactory;
import com.esafirm.androidplayground.common.OnNavigatePage;
import com.esafirm.androidplayground.dagger.example.DaggerSampleAct;
import com.esafirm.androidplayground.rxjava2.RxJava2SampleAct;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                MenuFactory.create(this,
                        Arrays.asList(
                                "Dagger Example",
                                "RxJava2"
                        ),
                        new OnNavigatePage() {
                            @Override
                            public void navigate(int index) {
                                navigateToPage(index);
                            }
                        })
        );
    }

    private void navigateToPage(int index) {
        switch (index) {
            case 0:
                DaggerSampleAct.start(this);
                break;
            case 1:
                RxJava2SampleAct.start(this);
                break;
        }
    }
}
