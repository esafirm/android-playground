package com.esafirm.androidplayground.utils;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import rx.*;
import rx.functions.*;
import rx.subjects.*;

@SuppressWarnings("unchecked")
public class Logger {

    private static SerializedSubject subject = new SerializedSubject<>(PublishSubject.create());
    private static final StringBuilder loggerBuilder = new StringBuilder();

    public static void clear() {
        loggerBuilder.setLength(0);
    }

    public static void divider() {
        log("-----------");
    }

    public static void log(Object o) {
        String text = o != null ? o.toString() : "Object null";
        System.out.println(text);
        loggerBuilder.append(text).append("\n");
        subject.onNext(loggerBuilder.toString());
    }

    public static String getLogText() {
        return loggerBuilder.toString();
    }

    private static Observable<String> getObs() {
        return subject.startWith(getLogText()).onBackpressureLatest();
    }

    public static ViewGroup getLogView(Context context) {
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
        final TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextColor(Color.BLACK);
        textView.setPadding(padding, padding / 2, padding, padding / 2);

        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(textView);

        getObs().observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        textView.setText(null);
                        textView.setText(s);
                    }
                });

        return scrollView;
    }
}
