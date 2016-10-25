package com.esafirm.androidplayground;

import android.app.Application;

import com.esafirm.androidplayground.dagger.AppComponent;

public class App extends Application {

    private static AppComponent component;

    public App() {
        component = AppComponent.Initializer.make(this);
    }

    public static AppComponent component() {
        return component;
    }
}
