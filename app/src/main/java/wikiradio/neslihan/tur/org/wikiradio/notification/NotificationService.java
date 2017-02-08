package wikiradio.neslihan.tur.org.wikiradio.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 26.12.2016.
 */

public class NotificationService extends Service implements AudioInfoCallbak {
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

    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {

    }

    @Override
    public void onError(Class sender) {

    }
}
