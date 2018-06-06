package com.and2long.facecompare;

import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;

public class App extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        OkGo.getInstance().init(this);
        CrashExceptionHandler.getInstance().init(this);
    }

    public static Context getGlobalContext() {
        return appContext;
    }
}
