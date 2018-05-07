package com.jianglei.jllog.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianglei on 5/5/18.
 */

public class NetInfoVo implements Parcelable{
    private String url;
    /**
     *请求头部数据
     */
    private Map<String,String>requestHeader=new HashMap<>();
    /**
     * url上带的参数
     */
    private Map<String,String>requsetUrlParams = new HashMap<>();
    /**
     * 请求表单数据
     */
    private Map<String,String>requestForm = new HashMap<>();

    /**
     * 返回的json数据
     */
    private String responseJson;

    public NetInfoVo(){

    }

    protected NetInfoVo(Parcel in) {
        url = in.readString();
        responseJson = in.readString();
        in.readMap(requestHeader,NetInfoVo.class.getClassLoader());
        in.readMap(requsetUrlParams,NetInfoVo.class.getClassLoader());
        in.readMap(requestForm,NetInfoVo.class.getClassLoader());
    }

    public static final Creator<NetInfoVo> CREATOR = new Creator<NetInfoVo>() {
        @Override
        public NetInfoVo createFromParcel(Parcel in) {
            return new NetInfoVo(in);
        }

        @Override
        public NetInfoVo[] newArray(int size) {
            return new NetInfoVo[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public Map<String, String> getRequsetUrlParams() {
        return requsetUrlParams;
    }

    public void setRequsetUrlParams(Map<String, String> requsetUrlParams) {
        this.requsetUrlParams = requsetUrlParams;
    }

    public Map<String, String> getRequestForm() {
        return requestForm;
    }

    public void setRequestForm(Map<String, String> requestForm) {
        this.requestForm = requestForm;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    @Override
    public String toString() {
        return url+"   "+getResponseJson();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(responseJson);
        dest.writeMap(requestHeader);
        dest.writeMap(requsetUrlParams);
        dest.writeMap(requestForm);
    }
}
