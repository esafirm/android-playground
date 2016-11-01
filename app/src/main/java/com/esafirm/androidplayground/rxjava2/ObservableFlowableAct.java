package com.esafirm.androidplayground.rxjava2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.esafirm.androidplayground.utils.Logger.clear;
import static com.esafirm.androidplayground.utils.Logger.getLogView;
import static com.esafirm.androidplayground.utils.Logger.log;

public class ObservableFlowableAct extends AppCompatActivity {

    private Disposable disposable;
    private Disposable secondDisposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clear();
        setContentView(getLogView(this));

        disposable = Observable.interval(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .forEach(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        log("Observable:" + aLong);
                    }
                });

        secondDisposable = Flowable.interval(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureLatest() // Flowable can be backpressured
                .forEach(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        log("Flowable:" + aLong);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        if (secondDisposable != null) {
            secondDisposable.dispose();
        }
        super.onDestroy();
    }
}
