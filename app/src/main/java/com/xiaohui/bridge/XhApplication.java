package com.xiaohui.bridge;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.storage.DesEncrypt;
import com.xiaohui.bridge.storage.Store;

import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import java.io.File;

/**
 * Created by xhChen on 14/9/22.
 */
public class XhApplication extends Application {

    private static final String[] MEDIA_DIR = {"Picture", "Voice", "Video"};
    private static final String CACHE_PATH = Environment.getExternalStorageDirectory() + "/iBridge/";
    private static final String STORE_NAME = "bridge.storage";
    private static final String KEY = "@WSXCDE#$RV3edc";
    private static final String DEFAULT_USER_NAME = "Default";

    private static Application application;
    private BinderFactory binderFactory;
    private Cookie cookie;
    private Store store;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        BinderFactoryBuilder builder = new BinderFactoryBuilder();
        binderFactory = builder.build();
        cookie = new Cookie();
        store = new Store(this, STORE_NAME, new DesEncrypt(KEY));
        createCacheFolder();
        initImageLoader();
    }

    private void initImageLoader() {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(5 * 1024 * 1024) // 5 Mb
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public BinderFactory getBinderFactory() {
        return binderFactory;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public static Application getApplication() {
        return application;
    }

    public Store getStore() {
        return store;
    }

    public String getCurrentUserName() {
        return store.getString(Keys.USER, DEFAULT_USER_NAME);
    }

    public void setCurrentUserName(String userName) {
        store.putString(Keys.USER, userName);
    }

    private void createCacheFolder() {
        for (String name : MEDIA_DIR) {
            String path = CACHE_PATH + name + "/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    public String getCachePath() {
        return CACHE_PATH;
    }

    public String getCachePathForPicture() {
        return CACHE_PATH + MEDIA_DIR[0] + "/";
    }

    public String getCachePathForVoice() {
        return CACHE_PATH + MEDIA_DIR[1] + "/";
    }

    public String getCachePathForVideo() {
        return CACHE_PATH + MEDIA_DIR[2] + "/";
    }
}
