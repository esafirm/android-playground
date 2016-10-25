package com.esafirm.androidplayground.dagger;

import com.esafirm.androidplayground.App;
import com.esafirm.androidplayground.dagger.example.NonScopedClass;
import com.esafirm.androidplayground.dagger.example.ScopedClass;
import com.esafirm.androidplayground.dagger.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    NonScopedClass classInAppComponent();
    ScopedClass singletonClass();

    class Initializer {
        public static AppComponent make(App app) {
            return DaggerAppComponent.builder()
                    .appModule(new AppModule(app))
                    .build();
        }
    }
}
