package com.shwy.bestjoy.utils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bestjoy on 16/3/10.
 */
public class HandlerTimerManager {
    private static final HandlerTimerManager INSTANCE = new HandlerTimerManager();
    private List<HandlerTimer> mHandlerTimerList;

    private void checkHandlerTimerList() {
        if (mHandlerTimerList == null)
            mHandlerTimerList = new ArrayList(6);
    }

    public static HandlerTimerManager getInstance() {
        return INSTANCE;
    }

    public HandlerTimer create(long time, Runnable runnable) {
        HandlerTimer handlerTimer = new HandlerTimer(time, runnable);
        checkHandlerTimerList();
        mHandlerTimerList.add(handlerTimer);
        return handlerTimer;
    }

    public void stopAll() {
        if (mHandlerTimerList != null) {
            Iterator<HandlerTimer> iterator = mHandlerTimerList.iterator();
            while (iterator.hasNext()) {
                HandlerTimer handlerTimer = iterator.next();
                handlerTimer.stop();
            }
            mHandlerTimerList.clear();
        }

    }

    public void stop(HandlerTimer handlerTimer) {
        boolean handled = false;
        if (mHandlerTimerList != null) {
            if (mHandlerTimerList.contains(handlerTimer)) {
                mHandlerTimerList.remove(handlerTimer);
                handlerTimer.stop();
                handled = true;
            }
        }
        if (!handled) {
            handlerTimer.stop();
        }
    }
}
