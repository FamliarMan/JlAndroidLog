package com.jianglei.jllog;

import android.provider.MediaStore;

import com.jianglei.jllog.aidl.NetInfoVo;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by jianglei on 5/8/18.
 */

public class JlLogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();  //获取request
        Response response;
        response = chain.proceed(request); //OkHttp链式调用
        NetInfoVo netInfoVo = new NetInfoVo();
        HttpUrl httpUrl = request.url();
        //设置url
        netInfoVo.setUrl(httpUrl.toString());
        //设置Query参数
        Map<String, String> queryParams = new HashMap<>();
        for (int i = 0; i < httpUrl.querySize(); ++i) {
            queryParams.put(httpUrl.queryParameterName(i), httpUrl.queryParameterValue(i));
        }
        netInfoVo.setRequsetUrlParams(queryParams);
        //设置Header
        Map<String, String> headers = new HashMap<>();
        for (String name : request.headers().names()) {
            headers.put(name, request.header(name));
        }
        netInfoVo.setRequestHeader(headers);

        //设置post参数
        RequestBody requestBody = request.body();
        if (requestBody != null && requestBody instanceof FormBody) {
            Map<String, String> postParams = new HashMap<>();
            FormBody formRequestBody = (FormBody) requestBody;

            for (int i = 0; i < formRequestBody.size(); ++i) {
                postParams.put(formRequestBody.name(i), formRequestBody.value(i));
            }
            netInfoVo.setRequestForm(postParams);
        }

        //设置返回信息json
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            netInfoVo.setResponseJson("No ResponseBody!");
        } else {
            MediaType mediaType = responseBody.contentType();
            if (mediaType == null) {
                netInfoVo.setResponseJson("No content-type!");
            } else if (mediaType.toString().toLowerCase().contains("text/plain") ||
                    mediaType.toString().toLowerCase().contains("application/json")) {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();
                netInfoVo.setResponseJson(buffer.clone().readString(Charset.forName("UTF-8")));
                JlLog.notifyNetInfo(netInfoVo);
            }
        }
        return response;
    }

    /**
     * 请求体转String
     *
     * @param request
     * @return
     */
    private static String bodyToString(final RequestBody request) {

        try {
            final Buffer buffer = new Buffer();
            request.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
