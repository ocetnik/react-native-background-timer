package com.ocetnik.timer;

import android.os.Handler;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.lang.Runnable;

public class BackgroundTimerModule extends ReactContextBaseJavaModule {

    private Handler handler;
    private ReactContext reactContext;
    private Runnable runnable;

    public BackgroundTimerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNBackgroundTimer";
    }

    @ReactMethod
    public void start(final int delay) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                sendEvent(reactContext, "backgroundTimer");
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
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
