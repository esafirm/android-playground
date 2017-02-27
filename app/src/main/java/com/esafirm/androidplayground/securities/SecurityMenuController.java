package com.esafirm.androidplayground.securities;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.RouterTransaction;
import com.esafirm.androidplayground.common.BaseController;
import com.esafirm.androidplayground.common.MenuFactory;
import com.esafirm.androidplayground.common.OnNavigatePage;

import java.util.Arrays;

public class SecurityMenuController extends BaseController {

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return MenuFactory.create(container.getContext(), Arrays.asList(
                "Bcrypt"
        ), new OnNavigatePage() {
            @Override
            public void navigate(int index) {
                getRouter().pushController(RouterTransaction.with(new BcryptController()));
            }
        });
    }
}
