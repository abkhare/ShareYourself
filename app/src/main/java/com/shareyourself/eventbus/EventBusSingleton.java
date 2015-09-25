package com.shareyourself.eventbus;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public final class EventBusSingleton
{
    private final String TAG = "EventBusSingleton";
    private final Handler mainThread = new Handler(Looper.getMainLooper());
    private static EventBusSingleton instance = new EventBusSingleton ();

    private final Bus eventBus = new Bus(ThreadEnforcer.MAIN);

    private EventBusSingleton ()
    {

    }

    /**
     * Returns a singleton instance of the EventBus
     */
    public static EventBusSingleton instance()
    {
        return instance;
    }

    public void unregister(Object listener)
    {
        eventBus.unregister(listener);
    }

    public void register (Object listener)
    {
        eventBus.register(listener);
    }

    public void postEvent (final Object event)
    {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                eventBus.post(event);
            }
            else {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        postEvent(event);
                    }
                });
            }
        } catch (Exception ex){
            Log.e(TAG, "EventBusSingleton.postEvent!" + ex.getMessage());
            ex.printStackTrace();
        }
    }

}

