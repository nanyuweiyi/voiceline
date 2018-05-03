package com.carlos.voiceline;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * ================================================
 * 描   述: 基于WeakReference封装的BaseHandler类
 * 作   者：tnn
 * 创建日期：2018/5/3
 * 版   本：1.0.0
 * ================================================
 */
public class BaseHandler<T extends BaseHandler.BaseHandlerCallBack> extends Handler {

    WeakReference<T> wr;

    public BaseHandler(T t) {
        wr = new WeakReference<T>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T t = wr.get();
        if (t != null) {
            t.callBack(msg);
        }
    }

    public interface BaseHandlerCallBack {
        public void callBack(Message msg);
    }

}
