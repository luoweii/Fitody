package com.luowei.fitody.utils;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.luowei.fitody.App;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by luowei on 2015/8/10.
 */
public class CommonUtil {
    public static void showToast(CharSequence cs) {
        Toast.makeText(App.context, cs, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int id) {
        Toast.makeText(App.context, id, Toast.LENGTH_SHORT).show();
    }

    public static int dp2px(float dipValue) {
        final float scale = App.context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static float px2dp(float pxValue) {
        final float scale = App.context.getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f);
    }

    /**
     * 获取手机号码
     *
     * @return
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
    }

    public static void showScreenDpi(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        LogUtils.d("--------------------屏幕分辨率--------------------\n" +
                dm.widthPixels + "*" + dm.heightPixels + " density:" + dm.density + " dpi:" + dm.densityDpi);
    }

    /**
     * 隐藏输入法
     */
    public static void hideInput(Activity activity) {
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 清除本地缓存
     */
    public static float cleanCache() {
        try {
            File f = App.context.getExternalCacheDir();
            File f1 = App.context.getCacheDir();
            float size = (deleteFile(f) + deleteFile(f1)) / 1024f / 1024f;
            return size;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 删除文件夹里面的文件不删除文件夹
     *
     * @param file
     */
    public static long deleteFile(File file) {
        long length = 0;

        if (file.isFile()) {
            length = file.length();
            file.delete();
            return length;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            for (int i = 0; i < childFiles.length; i++) {
                length += deleteFile(childFiles[i]);
            }
//            file.delete();
        }
        return length;
    }

    /**
     * 获取格式化的时间
     *
     * @return
     */
    public static String getFormatTime(int second) {
        SimpleDateFormat sdf = new SimpleDateFormat("m:ss");
        return sdf.format(new Date(second * 1000));
    }
}