package com.and2long.facecompare;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by and2long on 2017/11/1.
 */

public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static CrashExceptionHandler instance;
    private Context mContext;

    public static CrashExceptionHandler getInstance() {
        if (instance == null) {
            instance = new CrashExceptionHandler();
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 核心方法，当程序crash 会回调此方法， Throwable中存放这错误日志
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        saveCrashInfo2File(ex);
        SystemClock.sleep(2000);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void saveCrashInfo2File(Throwable ex) {
        String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        File logFile = new File(App.getGlobalContext().getExternalCacheDir(), "log.txt");
        StringWriter sw = new StringWriter();
        sw.append(timeStr).append('\n');
        ex.printStackTrace(new PrintWriter(sw));
        //系统版本等信息
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("程序版本：" + mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName + "\n");
            sb.append("安卓版本：" + Build.VERSION.RELEASE + " / " + Build.VERSION.SDK_INT + "\n");
            sb.append("手机型号：" + Build.MANUFACTURER + " / " + Build.MODEL + "\n");
            sb.append("处理器：" + Build.BOARD + "\n");
            writeFile(logFile, sb.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        writeFile(logFile, sw.toString());
        writeFile(logFile, "\n-----------------------一本正经的分割线-----------------------\n");
    }

    private void writeFile(File file, String msg) {
        FileWriter fw = null;
        try {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file, true);
            fw.append(msg);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
