package com.esafirm.androidplayground.common;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.esafirm.androidplayground.utils.Logger;

public class BaseController extends Controller {

    public BaseController() {
        Logger.clear();
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return null;
    }
}
