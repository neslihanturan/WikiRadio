package wikiradio.neslihan.tur.org.wikiradio.action;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.RadioActivity;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.proxy.App;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallback;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallbackForTTS;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheController2;
import wikiradio.neslihan.tur.org.wikiradio.ttscache.TTSCacheController;

/**
 * Created by nesli on 28.02.2017.
 */

public class AudioFileButtonListener {
    public static String LOG_TAG = AudioFileButtonListener.class.getName();
    public static CacheControlCallback cacheControlCallback;
    public static CacheControlCallbackForTTS cacheControlCallbackForTTS;

    public static void playOrPause(Context context) throws IOException {
        Log.d(LOG_TAG,"playOrPause");
        //if(CacheController2.getCurrentURL()==null){
        if(Constant.nowPlayingAudio == null && Constant.nowPlayingFile == null){
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
        else if (TTSCacheController.getCurrentFile()!=null){
            cacheControlCallbackForTTS.onFileConsumed();
        }

        Log.d(LOG_TAG,"call onNextFileRequested()");
        cacheControlCallback.onNextFileRequested();
        AudioFile newAudioFile = CacheController2.getCurrentAudio();
        if(newAudioFile==null){
            Log.d(LOG_TAG,"newAudioFile is null");
            DataUtils.getRandomAudio(Constant.categorySet,(AudioInfoCallbak) context);
        }else{
            Log.d(LOG_TAG,"newAudioFile is NOT null");
            newAudioFile.setProxyUrl(App.getProxy(context).getProxyUrl(newAudioFile.getAudioUrl()));
            MediaPlayerController.changeSong(newAudioFile.getProxyUrl());
            // TODO:seekBar.setSecondaryProgress(seekBar.getMax());
            RadioActivity.setSecondarySeekbarMax();
            playSong(newAudioFile);
        }
    }

    public static void playSong(AudioFile audioFile) throws IOException{
        Log.d(LOG_TAG,"playSong");
        Constant.nowPlayingAudio = audioFile;
        MediaPlayerController.play(audioFile.getProxyUrl());
        // TODO:nowPlayingAudio = audioFile;
        RadioActivity.registerCacheListener();
        //App.getProxy(this).registerCacheListener(this, audioFile.getProxyUrl());
    }

    public static void onSuccess(AudioFile audioFile, Context context) {
        try {
            Log.d(LOG_TAG,"onSuccess Audiofile");
            //Constant.proxy.registerCacheListener((CacheListener) context, audioFile.getAudioUrl());
            audioFile.setProxyUrl(Constant.proxy.getProxyUrl(audioFile.getAudioUrl()));
            MediaPlayerController.changeSong(audioFile.getProxyUrl());
            playSong(audioFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
