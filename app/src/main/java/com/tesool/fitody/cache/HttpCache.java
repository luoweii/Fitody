package com.tesool.fitody.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import com.tesool.fitody.utils.LogUtils;
import com.tesool.fitody.utils.OtherUtils;

public class HttpCache {

    private final LruMemoryCache<String, String> mMemoryCache;
    private DiskLruCache mDiskLruCache;
    private final Object mDiskCacheLock = new Object();

    private final static int DEFAULT_CACHE_SIZE = 1024 * 100;// string length
    private final int DISK_CACHE_INDEX = 0;
    private int cacheSize = DEFAULT_CACHE_SIZE;
    private boolean diskCacheEnabled = true;
    private int diskCacheSize = 1024 * 1024 * 50;  // 50M
    private String diskCachePath;

    public HttpCache() {
        this(HttpCache.DEFAULT_CACHE_SIZE);
    }

    public HttpCache(int strLength) {
        this.cacheSize = strLength;

        mMemoryCache = new LruMemoryCache<String, String>(this.cacheSize) {
            @Override
            protected int sizeOf(String key, String value) {
                if (value == null) return 0;
                return value.length();
            }
        };

//
    }
    
    public void initDiskCache() {
        // Set up disk cache
        synchronized (mDiskCacheLock) {
            if (diskCacheEnabled && diskCachePath != null && mDiskLruCache == null) {
                File diskCacheDir = new File(diskCachePath);
                if (diskCacheDir.exists() || diskCacheDir.mkdirs()) {
                    long availableSpace = OtherUtils.getAvailableSpace(diskCacheDir);
                    long diskCacheSize = this.diskCacheSize;
                    diskCacheSize = availableSpace > diskCacheSize ? diskCacheSize : availableSpace;
                    try {
                        mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, diskCacheSize);
                        LogUtils.d("create disk cache success");
                    } catch (Throwable e) {
                        mDiskLruCache = null;
                        LogUtils.e("create disk cache error", e);
                    }
                }
            }
        }
    }

    public void setCacheSize(int strLength) {
        mMemoryCache.setMaxSize(strLength);
    }

    public void put(String url, String result) {
    	if (url == null || result == null) return;
    	
    	OutputStream outputStream = null;
        if (mDiskLruCache == null) {
            initDiskCache();
        } 
        
        if (diskCacheEnabled && mDiskLruCache != null) {
            try {
                DiskLruCache.Editor editor = mDiskLruCache.edit(url);
                if (editor != null) {
                    outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                    byte[] buffer = new byte[4096];
                    int len = 0;
                    BufferedInputStream bis = null;
                    bis = new BufferedInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
                    BufferedOutputStream out = new BufferedOutputStream(outputStream);
                    while ((len = bis.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.flush();
                    out.close();
                    editor.commit();
                }
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
            }
        }
        
        mMemoryCache.put(url, result);
    }

	public String get(String url) {
		if (url == null) return null;
		String result = null;
		result = mMemoryCache.get(url);
		if (result == null && diskCacheEnabled) {
			result = getFromDiskCache(url);
		}
		return result;
	}

    /**
     * Get the bitmap file from disk cache.
     *
     * @param url Unique identifier for which item to get
     * @return The file if found in cache.
     */
    public String getFromDiskCache(String url) {
        synchronized (mDiskCacheLock) {
        	if (mDiskLruCache == null) {
                initDiskCache();
            }
            if (mDiskLruCache != null) {
                DiskLruCache.Snapshot snapshot = null;
                try {
                    snapshot = mDiskLruCache.get(url);
                    if (snapshot != null) {
                    	String result = getStringFromInputStream(snapshot.getInputStream(DISK_CACHE_INDEX));
                    	if(result != null)
                    		mMemoryCache.put(url, result);
                        return result;
                    }
                } catch (Throwable e) {
                    LogUtils.e(e.getMessage(), e);
                } finally {
                    Util.closeQuietly(snapshot);
                }
            }
        }
        return null;
    }
    
    /**  
     * 将InputStream转换成String  
     * @param in InputStream  
     * @return String  
     * @throws Exception  
     *   
     */  
    private String getStringFromInputStream(InputStream in) throws Exception{  
          
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[4096];  
        int count = -1;  
        while((count = in.read(data,0,4096)) != -1)  
            outStream.write(data, 0, count);  
          
        data = null;  
        return new String(outStream.toByteArray(),"UTF-8");  
    }
    
    public void clear() {
        mMemoryCache.evictAll();
        
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
                try {
                    mDiskLruCache.delete();
                    mDiskLruCache.close();
                } catch (Throwable e) {
                    LogUtils.e(e.getMessage(), e);
                }
                mDiskLruCache = null;
            }
        }
        initDiskCache();
    }
    
    public boolean isDiskCacheEnabled() {
		return diskCacheEnabled;
	}

	public void setDiskCacheEnabled(boolean diskCacheEnabled) {
		this.diskCacheEnabled = diskCacheEnabled;
	}

	public String getDiskCachePath() {
		return diskCachePath;
	}

	public void setDiskCachePath(String diskCachePath) {
		this.diskCachePath = diskCachePath;
	}
}
