package wikiradio.neslihan.tur.org.wikiradio.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by nesli on 26.12.2016.
 */

public class NotificationService extends Service {
    private String LOG_TAG = NotificationService.class.getName();
    RadioNotification radioNotification;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        Log.d(LOG_TAG,"service is starting");
        radioNotification = new RadioNotification(this);
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
