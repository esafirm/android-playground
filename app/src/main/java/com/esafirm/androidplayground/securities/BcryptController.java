package com.esafirm.androidplayground.securities;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.esafirm.androidplayground.utils.Logger;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptController extends Controller {
    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        Logger.log(BCrypt.hashpw("test", BCrypt.gensalt(5)));
        return Logger.getLogView(container.getContext());
    }
}
