package com.tesool.fitody.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tesool.fitody.App;
import com.tesool.fitody.cache.HttpCache;
import com.tesool.fitody.config.AppConfig;
import com.tesool.fitody.utils.LogUtils;

/**
 * Created by luowei on 2015/10/25.
 */
public class MHttp {
    public static final String TOKEN = "Token";
    public static final String RESPONSE_CODE = "code";
    public static final String RESPONSE_DETAIL = "detail";
    public static final HttpCache httpCache = new HttpCache();
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        url = getAbsoluteUrl(url);
        LogUtils.d("-------------MHttp(get url)--------------- \n" + url
                + "\n-----    " + params);
        client.addHeader(TOKEN, App.TOKEN);
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        url = getAbsoluteUrl(url);
        LogUtils.d("-------------MHttp(post url)--------------- \n" + url
                +"\n-----    " + params);
        client.addHeader(TOKEN, App.TOKEN);
        client.post(url, params, responseHandler);
    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        LogUtils.d("-------------MHttp(put url)--------------- \n" + url
                +"\n-----    " + params);
        client.addHeader(TOKEN, App.TOKEN);
        client.put(url, params, responseHandler);
    }

    public static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        LogUtils.d("-------------MHttp(delete url)--------------- \n" + url
                + "\n-----    " + params);
        client.addHeader(TOKEN, App.TOKEN);
        client.delete(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return AppConfig.HTTP_SERVER + relativeUrl;
    }
}
