package io.boterop.timer;

import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class BackgroundTimerModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext reactApplicationContext;

    public BackgroundTimerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        BackgroundTimerModule.reactApplicationContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNBackgroundTimer";
    }

    static ReactApplicationContext getReactApplicationContext() {
        return reactApplicationContext;
    }

    static void emitEvent(String eventName, Object data) {
        if (reactApplicationContext != null && reactApplicationContext.hasActiveCatalystInstance()) {
            reactApplicationContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, data);
        }
    }

    @ReactMethod
    public void start(final int delay) {
        Intent serviceIntent = new Intent(getReactApplicationContext(), BackgroundTimerService.class);
        serviceIntent.putExtra("delayMs", (long) delay);
        ContextCompat.startForegroundService(getReactApplicationContext(), serviceIntent);
    }

    @ReactMethod
    public void stop() {
        Intent serviceIntent = new Intent(getReactApplicationContext(), BackgroundTimerService.class);
        getReactApplicationContext().stopService(serviceIntent);
    }

    @ReactMethod
    public void setTimeout(final int id, final double timeout) {
        final ReactContext reactContext = getReactApplicationContext();
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (reactContext.hasActiveCatalystInstance()) {
                    reactContext
                            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                            .emit("backgroundTimer.timeout", id);
                }
            }
        }, (long) timeout);
    }

    @ReactMethod
    public void addListener(String eventName) {}

    @ReactMethod
    public void removeListeners(Integer count) {}
}
