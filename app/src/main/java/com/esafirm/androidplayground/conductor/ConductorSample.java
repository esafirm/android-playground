package com.esafirm.androidplayground.conductor;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.Controller;

import java.io.Serializable;

public class ConductorSample extends Controller implements Serializable {

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        TextView textView = new TextView(container.getContext());
        textView.setText("Hello World");
        textView.setTextSize(48);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}
