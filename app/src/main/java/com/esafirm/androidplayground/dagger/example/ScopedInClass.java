package com.esafirm.androidplayground.dagger.example;


import com.esafirm.androidplayground.dagger.ActivityScope;

import javax.inject.Inject;

@ActivityScope
public class ScopedInClass {

    @Inject
    public ScopedInClass() {
    }
}
