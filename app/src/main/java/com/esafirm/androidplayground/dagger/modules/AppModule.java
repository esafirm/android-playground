package com.esafirm.androidplayground.dagger.modules;

import android.content.Context;

import com.esafirm.androidplayground.dagger.example.NonScopedClass;
import com.esafirm.androidplayground.dagger.example.ScopedClass;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext() {
        return context;
    }

    @Provides
    NonScopedClass someClassInAppComponent() {
        return new NonScopedClass();
    }

    @Singleton
    @Provides
    ScopedClass singletonClass() {
        return new ScopedClass();
    }
}

