package com.tesool.fitody.net;

import com.loopj.android.http.TextHttpResponseHandler;
import com.tesool.fitody.utils.JSONUtil;
import com.tesool.fitody.utils.LogUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by luowei on 2015/10/25.
 */
public abstract class JsonHttpHandler<T> extends TextHttpResponseHandler {
    private boolean enableCache = true;

    public JsonHttpHandler() {

    }

    public JsonHttpHandler(boolean enableCache) {
        this.enableCache = enableCache;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        LogUtils.d("----------MHttp response result(" + getRequestURI() + ")-------------\n"
                + responseString);

        try {
            JSONObject jo = new JSONObject(responseString);
            int code = jo.getInt(MHttp.RESPONSE_CODE);
            switch (code) {
                case 200:
                    String str = jo.getString(MHttp.RESPONSE_DETAIL);
                    T data = processResult(str);
                    onSuccess(data);
                    if (enableCache && str != null) {
                        MHttp.httpCache.put(getRequestURI().toString(), str);
                    }
                    break;
                default:
                    onFailure(code, jo.getString(MHttp.RESPONSE_DETAIL));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(-1, "数据格式错误");
        }
    }

    private T processResult(String dataStr) {
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        //检测泛型类型是否为String
        if (type instanceof Class && String.class.getName().equals(((Class) type).getName())) {
            return (T) dataStr;
        }
        return JSONUtil.fromJson(dataStr, type);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        LogUtils.w(statusCode + " " + responseString, throwable);
        String str = MHttp.httpCache.get(getRequestURI().toString());
        if (str != null && enableCache) {
            T data = processResult(str);
            onSuccess(data);
        } else {
            onFailure(statusCode, responseString);
        }
    }

    public abstract void onSuccess(T data);

    public abstract void onFailure(int errCode, String msg);

}
