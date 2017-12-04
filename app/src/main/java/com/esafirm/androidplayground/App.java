package com.esafirm.androidplayground;

import android.app.Application;
import android.content.Context;

import com.esafirm.androidplayground.dagger.AppComponent;
import com.facebook.stetho.Stetho;

public class App extends Application {

    private static AppComponent component;

    public App() {
        component = AppComponent.Initializer.make(this);
    }

    public static AppComponent component() {
        return component;
    }

    public static Context appContext(){
        return component.appContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
