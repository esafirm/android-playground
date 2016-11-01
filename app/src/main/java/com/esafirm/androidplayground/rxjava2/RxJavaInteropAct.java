package com.esafirm.androidplayground.rxjava2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.esafirm.androidplayground.utils.Logger;

import java.util.Arrays;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.schedulers.Schedulers;

public class RxJavaInteropAct extends AppCompatActivity {

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.clear();
        setContentView(Logger.getLogView(this));

        // RxJava 1 Observable
        rx.Observable<Integer> observable = Observable.from(Arrays.asList(1, 2, 3, 4, 5))
                .subscribeOn(Schedulers.computation());

        // to RxJava 2 Observable
        io.reactivex.Observable<Integer> observable2 = RxJavaInterop.toV2Observable(observable);

        // to RxJava 2 Flowable
        io.reactivex.Flowable<Integer> flowable = RxJavaInterop.toV2Flowable(observable);

        // Subscription now disposable
        // CompositeSubscription now CompositeDisposable
        compositeDisposable = new CompositeDisposable();

        // forEach now return Disposable
        compositeDisposable.add(
                observable2.forEach(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Logger.log("ObservableV2:" + integer);
                    }
                })
        );

        compositeDisposable.add(
                flowable.forEach(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Logger.log("FlowableV2:" + integer);
                    }
                })
        );
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
