package com.example.graduate.findingcooking;


import android.os.Build;
import com.example.graduate.findingcooking.base.BaseApplication;

public class RecipeApplication extends BaseApplication implements Thread.UncaughtExceptionHandler{
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Device Model: ").append(Build.MODEL).append("\n");
            sb.append("OS: ").append(Build.VERSION.RELEASE).append("\n");
            sb.append("App Code: ").append(BuildConfig.VERSION_CODE).append("\n");
            sb.append("App Name: ").append(BuildConfig.VERSION_NAME).append("\n");
            sb.append(printExceptionStack(ex));
        }
        mUncaughtExceptionHandler.uncaughtException(thread, ex);
    }

    private String printExceptionStack(Throwable ex) {
        Throwable th = ex;
        StringBuilder sb = new StringBuilder();
        while (th != null) {
            sb.append("Caused By: ").append(th.getClass().getName()).append(":").append(th.getMessage()).append("\n");
            sb.append("Caused Detail: ");
            for (StackTraceElement ste : th.getStackTrace()) {
                sb.append("\tat ").append(ste).append("\n");
            }
            th = th.getCause();
        }
        return sb.toString();
    }
}
