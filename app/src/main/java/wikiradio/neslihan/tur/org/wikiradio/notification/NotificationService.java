package wikiradio.neslihan.tur.org.wikiradio.notification;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import wikiradio.neslihan.tur.org.wikiradio.action.AudioFileButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerCallback;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 26.12.2016.
 */

public class NotificationService extends Service implements AudioInfoCallbak, MediaPlayerCallback {
    private String LOG_TAG = NotificationService.class.getName();
    RadioNotification radioNotification;
    public static Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        Log.d(LOG_TAG,"service is starting");
        context = this;
        MediaPlayerController.delegateService = this;
        radioNotification = new RadioNotification(this, Constant.ISPLAYING.PLAYING);
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {
        AudioFileButtonListener.onSuccess(audioFile, this);
    }

    @Override
    public void onError(Class sender) {

    }

    @Override
    public void onMediaPlayerPaused() {
        MusicIntentReceiver.unlock();
        radioNotification.updateNotification(Constant.ISPLAYING.PAUSED);
    }

    @Override
    public void onMediaPlayerPlaying() {
        MusicIntentReceiver.unlock();
        radioNotification.updateNotification(Constant.ISPLAYING.PLAYING);
    }
}
