package com.esafirm.androidplayground.rxjava2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.esafirm.androidplayground.common.BaseAct;
import com.esafirm.androidplayground.utils.Logger;

import io.reactivex.functions.Consumer;

public class RxJava2NullItemAct extends BaseAct {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = new Button(this);
        button.setText("Start Null Observable");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doStartObservable();
            }
        });

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(button);
        linearLayout.addView(Logger.getLogView(this));

        setContentView(linearLayout);
    }

    private void doStartObservable() {
        io.reactivex.Observable.just(null)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Logger.log("onNext on Observable.just(null)");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.log("onError on Observable.just(null)" + throwable);
                    }
                });
    }
}
