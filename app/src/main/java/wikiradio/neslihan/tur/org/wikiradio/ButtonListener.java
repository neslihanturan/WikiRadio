package wikiradio.neslihan.tur.org.wikiradio;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.danikula.videocache.CacheListener;

import java.io.IOException;

import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.proxy.App;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallback;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheController2;

/**
 * Created by nesli on 08.02.2017.
 */

public class ButtonListener {
    public static String LOG_TAG = ButtonListener.class.getName();
    public static CacheControlCallback cacheControlCallback;

    public static void playOrPause(Context context) throws IOException {
        //if(CacheController2.getCurrentURL()==null){
        if(Constant.nowPlaying==null){
            nextSong(context);
        }else{
            MediaPlayerController.playOrPause(CacheController2.getCurrentURL());
        }
    }

    public static void nextSong(Context context) throws IOException {
        Log.d(LOG_TAG,"next song requested");
        //lock();
        RadioActivity.unregisterCacheListener();
        //TODO: remowe wasted file
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
            DataUtils.getRandomAudio(Constant.categorySet,(AudioInfoCallbak) context);
        }else{
            Log.d(LOG_TAG,"newAudioFile is NOT null");
            newAudioFile.setProxyUrl(App.getProxy(context).getProxyUrl(newAudioFile.getUrl()));
            MediaPlayerController.changeSong(newAudioFile.getProxyUrl());
            // TODO:seekBar.setSecondaryProgress(seekBar.getMax());
            RadioActivity.setSecondarySeekbarMax();
            playSong(newAudioFile);
        }
    }

    public static void playSong(AudioFile audioFile) throws IOException{
        Log.d(LOG_TAG,"playSong method started");
        Constant.nowPlaying = audioFile;
        MediaPlayerController.play(audioFile.getProxyUrl());
        // TODO:nowPlaying = audioFile;
        RadioActivity.registerCacheListener();
        //App.getProxy(this).registerCacheListener(this, audioFile.getProxyUrl());
    }

    public static void onSuccess(AudioFile audioFile, Context context) {
        try {
            //Constant.proxy.registerCacheListener((CacheListener) context, audioFile.getUrl());
            audioFile.setProxyUrl(Constant.proxy.getProxyUrl(audioFile.getUrl()));
            MediaPlayerController.changeSong(audioFile.getProxyUrl());
            playSong(audioFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
