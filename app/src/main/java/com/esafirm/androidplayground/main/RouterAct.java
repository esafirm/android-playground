package com.esafirm.androidplayground.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.esafirm.androidplayground.R;
import com.esafirm.androidplayground.common.BaseAct;
import com.esafirm.androidplayground.common.ControllerMaker;

public class RouterAct extends BaseAct {

    private static final String EXTRA_CONTROLLER = "Extra.Controller";

    private Router router;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router);

        ControllerMaker maker = (ControllerMaker) getIntent()
                .getExtras()
                .getSerializable(EXTRA_CONTROLLER);

        router = Conductor.attachRouter(this, (ViewGroup) findViewById(R.id.container), savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(maker.makeController()));
        }
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }

    /* --------------------------------------------------- */
    /* > Stater */
    /* --------------------------------------------------- */

    public static void start(Context context, ControllerMaker controller) {
        context.startActivity(new Intent(context, RouterAct.class)
                .putExtra(EXTRA_CONTROLLER, controller));
    }
}
