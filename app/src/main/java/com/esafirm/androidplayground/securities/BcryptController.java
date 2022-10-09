package com.esafirm.androidplayground.securities;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esafirm.androidplayground.common.BaseController;
import com.esafirm.androidplayground.libs.Logger;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptController extends BaseController {
    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        String hashed = BCrypt.hashpw("test", BCrypt.gensalt());
        Logger.INSTANCE.log(hashed);
        Logger.INSTANCE.log(BCrypt.checkpw("test", hashed));
        return Logger.INSTANCE.getLogView(container.getContext());
    }
}
