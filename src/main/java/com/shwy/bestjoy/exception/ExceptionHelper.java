package com.shwy.bestjoy.exception;

import android.content.Context;

import com.bestjoy.mobile.basic.R;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
/**
 * Created by bestjoy on 16/3/8.
 */
public class ExceptionHelper {

    private Context mContext;
    private static final ExceptionHelper INSTANCE = new ExceptionHelper();


    public static ExceptionHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 集成该库必须调用此方法初始化.
     * @param context
     */
    public void setContext(Context context) {
        mContext = context;

    }


    /**
     * 返回常用的错误信息
     * @param e
     * @return
     */
    public String getGeneralErrorMessage(Exception e) {
        e.printStackTrace();
        String errorMessage = "";
        if (e instanceof StatusException) {
            errorMessage = getNetworkError(((StatusException) e).getStatusCode());
        } else if (e instanceof FileNotFoundException) {
            errorMessage = e.getMessage();
        } else if (e instanceof ClientProtocolException) {
            errorMessage = getNetworkException(ExceptionCode.ClientProtocolExceptionCode);
        } else if (e instanceof ConnectTimeoutException) {
            errorMessage = getNetworkException(ExceptionCode.ConnectTimeoutExceptionCode);
        } else if (e instanceof UnknownHostException) {
            errorMessage = getNetworkException(ExceptionCode.UnknownHostExceptionCode);
        } else if (e instanceof HttpHostConnectException) {
            errorMessage = getNetworkException(ExceptionCode.HttpHostConnectExceptionCode);
        } else if (e instanceof SocketException) {
            errorMessage = getNetworkException(ExceptionCode.SocketExceptionCode);
        } else if (e instanceof SocketTimeoutException) {
            errorMessage = getNetworkException(ExceptionCode.SocketTimeoutExceptionCode);
        } else if (e instanceof IOException) {
            errorMessage = e.getMessage();
        } else if (e instanceof JSONException) {
            errorMessage = getNetworkError(String.valueOf(ExceptionCode.JSONExceptionCode));
        } else {
            errorMessage = e.getMessage();
        }
        return errorMessage;
    }


    /***
     * 显示通常的网络连接错误
     * @return
     */
    public String getGernalNetworkError() {
        return mContext.getString(R.string.basic_msg_gernal_network_error);
    }

    public String getNetworkError(String statusCode) {
        return mContext.getString(R.string.basic_msg_network_error_statue, statusCode);
    }
    public String getNetworkException(int code) {
        return mContext.getString(R.string.basic_format_network_exception, String.valueOf(code));
    }
}
