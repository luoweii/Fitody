package com.tesool.fitody;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.tesool.fitody.utils.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 骆巍 on 2015/8/12.
 */
public class App extends Application {
    public static Context context;

    //保存全局环境变量
    public static Map<Object, Object> maps;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            LogUtils.e("uncaughtException, thread:" + thread.toString(), ex);
            //退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            //重启程序
//            final Intent intent = getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getApplicationContext().startActivity(intent);
//            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.customTagPrefix = "FIT";
        //处理未捕获的异常
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
        context = getApplicationContext();
        if (maps == null) maps = new HashMap<>();

        initImageLoader();

//        LeakCanary.install(this);

    }

    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(200))
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY)
                .build();

        ImageLoaderConfiguration imageconfig = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .threadPoolSize(3)
                .memoryCacheExtraOptions(720, 1280)  // 缓存到内存的图片大小范围
                .diskCacheSize(50 * 1024 * 1024)  // 50Mb
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .defaultDisplayImageOptions(options)
                .denyCacheImageMultipleSizesInMemory().build();
        ImageLoader.getInstance().init(imageconfig);
    }

}
