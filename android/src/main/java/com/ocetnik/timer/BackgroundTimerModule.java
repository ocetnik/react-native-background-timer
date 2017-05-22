package com.ocetnik.timer;

import android.os.Handler;
import android.os.HandlerThread;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.lang.Runnable;

public class BackgroundTimerModule extends ReactContextBaseJavaModule {

    private HandlerThread handlerThread;
    private Handler handler;
    private ReactContext reactContext;
    private Runnable runnable;

    private static final String TIMER_NAME = "timer_name";
    private static final String TIMEOUT_NAME = "timeout_name";

    public BackgroundTimerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "BackgroundTimer";
    }

    @ReactMethod
    public void start(final int delay) {
        handlerThread = new HandlerThread(TIMER_NAME);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                sendEvent(reactContext, "backgroundTimer");
                handler.postDelayed(runnable, delay);
            }
        };

        handler.post(runnable);
    }

    @ReactMethod
    public void stop() {
        // avoid null pointer exceptio when stop is called without start
        if (handler != null) handler.removeCallbacks(runnable);
    }

    private void sendEvent(ReactContext reactContext, String eventName) {
        reactContext
        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
        .emit(eventName, null);
    }

    @ReactMethod
    public void setTimeout(final int id, final int timeout) {
        HandlerThread handlerThread = new HandlerThread(TIMEOUT_NAME);
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getReactApplicationContext().hasActiveCatalystInstance()) {
                    getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("backgroundTimer.timeout", id);
                }
            }
        }, timeout);
    }

    /*@ReactMethod
    public void clearTimeout(final int id) {
        // todo one day..
        // not really neccessary to have
    }*/
}
