package com.magicianguo.lsplauncher;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.OutputStream;

public class MainActivity extends Activity {
    private static final int MSG_KILL_PROCESS = 1;
    private static final Handler sHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_KILL_PROCESS) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean hasRoot = false;
        boolean execSuccess = false;
        try {
            Process process = Runtime.getRuntime().exec("su");
            hasRoot = true;
            OutputStream os = process.getOutputStream();
            String cmd;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cmd = "am broadcast -a android.telephony.action.SECRET_CODE -d android_secret_code://5776733 android";
            } else {
                cmd = "am broadcast -a android.provider.Telephony.SECRET_CODE -d android_secret_code://5776733 android";
            }
            os.write(cmd.getBytes());
            os.flush();
            os.close();
            execSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            String toastMessage;
            if (hasRoot) {
                toastMessage = "启动失败！";
            } else {
                toastMessage = "请授予Root权限！";
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        }
        finish();

        // 执行后，定时关闭应用
        if (execSuccess) {
            killProcess(10 * 1000);
        } else {
            killProcess(3 * 1000);
        }
    }

    private void killProcess(long delayMillis) {
        sHandler.removeMessages(MSG_KILL_PROCESS);
        sHandler.sendEmptyMessageDelayed(MSG_KILL_PROCESS, delayMillis);
    }
}