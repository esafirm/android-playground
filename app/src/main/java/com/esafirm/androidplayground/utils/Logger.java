package com.esafirm.androidplayground.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class Logger {

    private static final StringBuilder loggerBuilder = new StringBuilder();

    public static void clear() {
        loggerBuilder.setLength(0);
    }

    public static void log(Object o) {
        String text = o != null ? o.toString() : "Object null";
        System.out.println(text);
        loggerBuilder.append(text).append("\n");
    }

    public static String getLogText() {
        return loggerBuilder.toString();
    }

    public static ViewGroup getLogView(Context context) {
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(getLogText());
        textView.setTextColor(Color.BLACK);
        textView.setPadding(padding, padding / 2, padding, padding / 2);

        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(textView);

        return scrollView;
    }
}
