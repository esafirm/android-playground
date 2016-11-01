package com.esafirm.androidplayground.rxjava2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esafirm.androidplayground.common.MenuFactory;
import com.esafirm.androidplayground.common.OnNavigatePage;
import com.esafirm.androidplayground.utils.ActivityStater;

import java.util.Arrays;

public class RxJavaSampleAct extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                MenuFactory.create(this, Arrays.asList(
                        "Observable vs Flowable",
                        "Interop with v1"
                ), new OnNavigatePage() {
                    @Override
                    public void navigate(int index) {
                        navigateToIndex(index);
                    }
                })
        );
    }

    private void navigateToIndex(int index) {
        switch (index) {
            case 0:
                ActivityStater.start(this, ObservableFlowableAct.class);
                break;
            case 1:
                ActivityStater.start(this, RxJavaInteropAct.class);
                break;
        }
    }

    /* --------------------------------------------------- */
    /* > Stater */
    /* --------------------------------------------------- */

    public static void start(Context context) {
        context.startActivity(
                new Intent(context, RxJavaSampleAct.class));
    }
}
