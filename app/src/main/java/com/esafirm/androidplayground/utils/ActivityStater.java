package com.esafirm.androidplayground.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivityStater {
    public static void start(Context context, Class<? extends Activity> clazz) {
        context.startActivity(new Intent(context, clazz));
    }
}
