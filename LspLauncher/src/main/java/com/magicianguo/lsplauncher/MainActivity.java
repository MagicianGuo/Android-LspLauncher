package com.magicianguo.lsplauncher;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import java.io.OutputStream;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean hasRoot = false;
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
        } catch (Exception e) {
            e.printStackTrace();
            String toastMessage;
            if (hasRoot) {
                toastMessage = "启动失败！";
            } else {
                toastMessage = "请授予Root权限！";
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        } finally {
            finish();
        }
    }
}