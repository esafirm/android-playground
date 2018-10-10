package com.esafirm.androidplayground;

import android.app.Application;
import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;

import com.esafirm.androidplayground.dagger.AppComponent;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

public class App extends Application {

    private static AppComponent component;

    public App() {
        component = AppComponent.Initializer.make(this);
    }

    public static AppComponent component() {
        return component;
    }

    public static Context appContext() {
        return component.appContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        Stetho.initializeWithDefaults(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
