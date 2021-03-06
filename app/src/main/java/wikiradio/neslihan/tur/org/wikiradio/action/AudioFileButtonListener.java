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
import wikiradio.neslihan.tur.org.wikiradio.proxy.CommonsCacheController;
import wikiradio.neslihan.tur.org.wikiradio.ttscache.CacheControlCallbackForTTS;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheController2;

/**
 * Created by nesli on 28.02.2017.
 */

public class AudioFileButtonListener {
    public static String LOG_TAG = AudioFileButtonListener.class.getName();
    public static CacheControlCallback cacheControlCallback;
    public static CacheControlCallbackForTTS cacheControlCallbackForTTS;

    public static void playOrPause(Context context) throws IOException {
        Log.i(LOG_TAG,"playOrPause");
        //if(CacheController2.getCurrentURL()==null){
        if(Constant.nowPlayingAudio == null && Constant.nowPlayingFile == null){
            nextSong(context);
        }else{
            MediaPlayerController.playOrPause(CommonsCacheController.getInstance(context).getCurrentProxyURL());
        }
    }

    public static void nextSong(Context context) throws IOException {
        Log.i(LOG_TAG,"next song requested");
        //lock();
        RadioActivity.unregisterCacheListener();
        //TODO: remowe wasted file
        // there is a file that is played previously

        if(Constant.isAudioPlaying && Constant.nowPlayingAudio != null){
            Log.i(LOG_TAG,"an audio is playing");
            cacheControlCallback.onFileConsumed(Constant.nowPlayingAudio.getAudioUrl());
        }
        else if(!Constant.isAudioPlaying && Constant.nowPlayingFile != null){
            Log.i(LOG_TAG,"a file is playing");
            cacheControlCallbackForTTS.onFileConsumed(Constant.nowPlayingFile);
        }

        Log.i(LOG_TAG,"call onNextFileRequested()");
        cacheControlCallback.onNextFileRequested();
        AudioFile newAudioFile = CommonsCacheController.getInstance(context).getCurrentAudioFile();
        if(newAudioFile==null){
            Log.i(LOG_TAG,"newAudioFile is null");
            DataUtils.getRandomAudio(Constant.categorySet,(AudioInfoCallbak) context);
        }else{
            Log.i(LOG_TAG,"newAudioFile is NOT null");
            newAudioFile.setProxyUrl(App.getProxy(context).getProxyUrl(newAudioFile.getAudioUrl()));
            MediaPlayerController.changeSong(newAudioFile.getProxyUrl());
            // TODO:seekBar.setSecondaryProgress(seekBar.getMax());
            RadioActivity.setSecondarySeekbarMax();
            playSong(newAudioFile);
        }
    }

    public static void playSong(AudioFile audioFile) throws IOException{
        Log.i(LOG_TAG,"playSong");
        Constant.nowPlayingAudio = audioFile;
        Constant.nowPlayingFile = null;
        Constant.isAudioPlaying = true;
        MediaPlayerController.play(audioFile.getProxyUrl());
        // TODO:nowPlayingAudio = audioFile;
        RadioActivity.registerCacheListener();
        //App.getProxy(this).registerCacheListener(this, audioFile.getProxyUrl());
    }

    public static void onSuccess(AudioFile audioFile, Context context) {
        try {
            Log.i(LOG_TAG,"onSuccess Audiofile");
            //Constant.proxy.registerCacheListener((CacheListener) context, audioFile.getAudioUrl());
            audioFile.setProxyUrl(Constant.proxy.getProxyUrl(audioFile.getAudioUrl()));
            MediaPlayerController.changeSong(audioFile.getProxyUrl());
            playSong(audioFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void seekToForward(){
        Log.i(LOG_TAG,"seekTo Duration:");
        MediaPlayerController.seekTo(MediaPlayerController.getCurrentPosition()+
                MediaPlayerController.getDuration()/10);
    }


    public static void seekToRewind(){
        Log.i(LOG_TAG,"seekTo Duration:");
        MediaPlayerController.seekTo(MediaPlayerController.getCurrentPosition()-
                MediaPlayerController.getDuration()/10);
    }
}
