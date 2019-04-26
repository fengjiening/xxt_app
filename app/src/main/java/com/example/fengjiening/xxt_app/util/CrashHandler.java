package com.example.fengjiening.xxt_app.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Process;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CrashHandler用于处理未捕获的程序异常，并把异常信息保存在sdcard里
 * @author chenronggang
 *
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = CrashHandler.class.getSimpleName();
    
    /**
     * 未捕获异常信息保存地址
     */
    private static final String SDCARD_PATH = "/mnt/sdcard/sinovoice/smartform/uncaughtException/crashlog/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".txt";

    private static CrashHandler mCrashHandler = new CrashHandler();
    private UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return mCrashHandler;
    }

    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    /**
     * uncaughtException，当程序中有未被捕获的异常，系统将会自动调用uncaughtException()方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            // 导出异常信息到SD卡中
            SaveExceptionToSDCard(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ex.printStackTrace();

        //如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }

    }
    
    /**
     * 把未捕获的异常信息保存到sdcard
     * @param ex 未捕获的异常信息
     * @throws IOException
     */
    private void SaveExceptionToSDCard(Throwable ex) throws IOException {
        // 如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //JTLog.w(TAG, "sdcard unmounted,不能正常保存异常信息");
                
                return;
        }

        File dir = new File(SDCARD_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss").format(new Date(current));
        File file = new File(SDCARD_PATH + FILE_NAME + time + FILE_NAME_SUFFIX);

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            
            //打印当前时间
            pw.println(time);
            
            //打印手机信息
            savePhoneInfo(pw);
            pw.println();
            
            //打印异常信息
            ex.printStackTrace(pw);
            pw.flush();
            pw.close();
            
            //JTLog.d(TAG, "save crash info succeed");
        } catch (Exception e) {
            //JTLog.e(TAG, "save crash info failed");
        }
    }
    
    /**
     * 把手机信息也保存到文件
     * @param pw 打印流
     * @throws NameNotFoundException
     */
    private void savePhoneInfo(PrintWriter pw) throws NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        //android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        // cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }
}
