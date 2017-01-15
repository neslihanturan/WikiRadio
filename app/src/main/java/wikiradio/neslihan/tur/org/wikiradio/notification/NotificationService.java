package wikiradio.neslihan.tur.org.wikiradio.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import wikiradio.neslihan.tur.org.wikiradio.RadioActivity;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlService;

/**
 * Created by nesli on 26.12.2016.
 */

public class NotificationService extends Service implements CacheControlService.CacheControlFeedback{
    private String LOG_TAG = NotificationService.class.getName();
    RadioNotification radioNotification;
    AudioFile audioFile;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        CacheControlService.cacheControlFeedbackService = this;
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
    public void onNewSongSet(AudioFile audioFile) {
        this.audioFile = audioFile;
    }

    @Override
    public void onPlayingStateChanged() {
        //playOrPauseSong();
    }

}
