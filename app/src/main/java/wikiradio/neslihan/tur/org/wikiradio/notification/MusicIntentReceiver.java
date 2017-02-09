package wikiradio.neslihan.tur.org.wikiradio.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import wikiradio.neslihan.tur.org.wikiradio.ButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.RadioActivity;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerCallback;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.SingleMediaPlayer;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.proxy.App;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallback;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheController2;

/**
 * Created by nesli on 07.02.2017.
 */

public class MusicIntentReceiver extends BroadcastReceiver implements AudioInfoCallbak{
    private static String LOG_TAG = MusicIntentReceiver.class.getName();
    private Context context;
    public static CacheControlCallback cacheControlCallback;
    MediaPlayer mediaPlayer = null;
    String songUrl;


    @Override
    public void onReceive(Context ctx, Intent intent) {
        String action = intent.getAction();
        context = ctx;

        //TODO: null p. e. if push activity before service. Because onReceive never triggered.


        if (action.equals(
                android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
        }else{
            if(Constant.ACTION.PLAY_ACTION.equals(action)) {
                try {
                    lock();
                    ButtonListener.playOrPause(NotificationService.context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(LOG_TAG,"Pressed Play");
            } else if(Constant.ACTION.NEXT_ACTION.equals(action)) {
                try {
                    lock();
                    ButtonListener.nextSong(NotificationService.context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(LOG_TAG,"Pressed Next");
            }
        }
    }

    public static void lock(){

    }

    public static void unlock(){

    }

    private void playOrPause() throws IOException {
        //lock();
        if(CacheController2.getCurrentURL()==null){
            nextSong();
        }else{
            MediaPlayerController.playOrPause(CacheController2.getCurrentURL());
        }
    }
    private void playSong(AudioFile audioFile) throws IOException {
        Log.d(LOG_TAG,"playSong method started");
        MediaPlayerController.play(audioFile.getProxyUrl());
        //textView.setText("Audio Title: "+audioFile.getTitle()+"\n Category:"+audioFile.getCategory());
        //App.getProxy(this).registerCacheListener(this, audioFile.getProxyUrl());

    }
    private void nextSong() throws IOException {
        Log.d(LOG_TAG,"next song requested");
        //lock();
        //App.getProxy(context).unregisterCacheListener(context);
        // there is a file that is played previously
        if(CacheController2.getCurrentAudio()!=null){
            Log.d(LOG_TAG,"current audio is null");
            cacheControlCallback.onFileConsumed();
        }
        Log.d(LOG_TAG,"call onNextFileRequested()");
        cacheControlCallback.onNextFileRequested();
        AudioFile newAudioFile = CacheController2.getCurrentAudio();
        if(newAudioFile==null){
            Log.d(LOG_TAG,"newAudioFile is null");
            DataUtils.getRandomAudio(Constant.categorySet,this);
        }else{
            Log.d(LOG_TAG,"newAudioFile is NOT null");
            newAudioFile.setProxyUrl(App.getProxy(context).getProxyUrl(newAudioFile.getUrl()));
            MediaPlayerController.changeSong(newAudioFile.getProxyUrl());
            //seekBar.setSecondaryProgress(seekBar.getMax());
            playSong(newAudioFile);
        }
    }

    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {
        try {
            //App.getProxy(context).registerCacheListener(this, audioFile.getUrl());
            audioFile.setProxyUrl(App.getProxy(context).getProxyUrl(audioFile.getUrl()));
            MediaPlayerController.changeSong(audioFile.getProxyUrl());
            playSong(audioFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Class sender) {

    }

}
