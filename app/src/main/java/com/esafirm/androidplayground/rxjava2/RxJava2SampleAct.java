package com.esafirm.androidplayground.rxjava2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.esafirm.androidplayground.common.BaseAct;
import com.esafirm.androidplayground.common.MenuFactory;
import com.esafirm.androidplayground.common.OnNavigatePage;
import com.esafirm.androidplayground.utils.ActivityStater;

import java.util.Arrays;

public class RxJava2SampleAct extends BaseAct {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                MenuFactory.create(this, Arrays.asList(
                        "Observable vs Flowable",
                        "Interop with v1",
                        "Null item on Stream",
                        "Schedulers",
                        "RetryWhen",
                        "Single | Kotlin Infix Operator"
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
                ActivityStater.start(this, RxJava2ObservableFlowableAct.class);
                break;
            case 1:
                ActivityStater.start(this, RxJava2InteropAct.class);
                break;
            case 2:
                ActivityStater.start(this, RxJava2NullItemAct.class);
                break;
            case 3:
                ActivityStater.start(this, RxJava2SchedulerSampleAct.class);
                break;
            case 4:
                ActivityStater.start(this, RxJava2RetryWhen.class);
                break;
        }
    }

    /* --------------------------------------------------- */
    /* > Stater */
    /* --------------------------------------------------- */

    public static void start(Context context) {
        context.startActivity(
                new Intent(context, RxJava2SampleAct.class));
    }
}
