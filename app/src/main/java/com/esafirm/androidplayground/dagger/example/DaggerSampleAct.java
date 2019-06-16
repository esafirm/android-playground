package com.esafirm.androidplayground.dagger.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.esafirm.androidplayground.App;
import com.esafirm.androidplayground.dagger.DaggerActivityComponent;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.esafirm.androidplayground.utils.Logger.clear;
import static com.esafirm.androidplayground.utils.Logger.getLogView;
import static com.esafirm.androidplayground.utils.Logger.log;

public class DaggerSampleAct extends AppCompatActivity {

    @Inject NonScopedClass classInAppComponent;
    @Inject NonScopedClass classInAppComponent2;

    @Inject ScopedClass scopedClass;
    @Inject ScopedClass scopedClass2;

    @Inject Provider<ScopedClass> singletonClassProvider;

    @Inject ScopedInClass scopedInContructorClass;
    @Inject ScopedInClass scopedInContructorClass2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerActivityComponent.builder()
                .appComponent(App.Companion.component())
                .build()
                .inject(this);

        clear();
        log(">> Non scoped injected object \n");
        log(classInAppComponent); // Wi
        log(classInAppComponent2);
        log(App.Companion.component().classInAppComponent());

        log("\n");
        log(">> Scoped injected object \n");
        log(scopedClass);
        log(scopedClass2);
        log(App.Companion.component().singletonClass());

        log("\n");
        log(">> Scoped with Provider\n");
        log(singletonClassProvider.get()); // Will inject the same thing
        log(singletonClassProvider.get());

        log("\n");
        log(">> Scoped in constructor\n");
        log(scopedInContructorClass);
        log(scopedInContructorClass2);

        setContentView(getLogView(this));
    }

    /* --------------------------------------------------- */
    /* > Stater */
    /* --------------------------------------------------- */

    public static void start(Context context) {
        context.startActivity(new Intent(context, DaggerSampleAct.class));
    }
}
