package io.boterop.timer;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class BackgroundTimerService extends Service {
    private static final String CHANNEL_ID = "BackgroundTimerChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_NAME = "BackgroundTimerService";
    
    private Handler handler;
    private Runnable runnable;
    private int delay;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            delay = intent.getIntExtra("delayMs", 0);
            
            handler = new Handler(Looper.getMainLooper());
            runnable = new Runnable() {
                @Override
                public void run() {
                    sendEvent();
                    if (delay > 0) {
                        handler.postDelayed(this, delay);
                    }
                }
            };
            
            handler.postDelayed(runnable, delay);
        }
        
        // Start foreground to prevent service from being killed on Android 14+
        startForeground(NOTIFICATION_ID, createNotification());
        
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelCompat channel = new NotificationChannelCompat.Builder(
                CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW)
                .setName(NOTIFICATION_NAME)
                .build();
            NotificationManagerCompat.from(this).createNotificationChannel(channel);
        }
    }

    private NotificationCompat.Builder createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Background Timer Running")
                .setContentText("Timer is active in background")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setOngoing(true);
    }

    private void sendEvent() {
        ReactApplicationContext reactContext = BackgroundTimerModule.getReactApplicationContext();
        if (reactContext != null && reactContext.hasActiveCatalystInstance()) {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("backgroundTimer", null);
        }
    }
}