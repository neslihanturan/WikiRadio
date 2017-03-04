package wikiradio.neslihan.tur.org.wikiradio.action;

import android.content.Context;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.RadioActivity;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallback;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallbackForTTS;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheController2;
import wikiradio.neslihan.tur.org.wikiradio.ttscache.TTSCacheController;

/**
 * Created by nesli on 28.02.2017.
 */

public class TTSButtonListener{
    private static String LOG_TAG = TTSButtonListener.class.getName();
    public static CacheControlCallback cacheControlCallback;
    public static CacheControlCallbackForTTS cacheControlCallbackForTTS;



    public static void nextSong(Context context) throws IOException {
        Log.d(LOG_TAG,"nextSong");
        RadioActivity.unregisterCacheListener();

        //waste previously playing files, it can be an audio or TTS file
        if(CacheController2.getCurrentAudio()!=null){
            Log.d(LOG_TAG,"current audio is null");
            cacheControlCallback.onFileConsumed();
        }
        else if (TTSCacheController.getCurrentFile()!=null){
            cacheControlCallbackForTTS.onFileConsumed();
        }

        //request next file
        cacheControlCallbackForTTS.onNextFileRequested();

        FileDescriptor fileDescriptor = TTSCacheController.getCurrentFile();
        if(fileDescriptor==null){
            Log.d(LOG_TAG,"newAudioFile is null");
            //TODO: make please wait animation
        }else{
            Log.d(LOG_TAG,"newAudioFile is NOT null");
            //newAudioFile.setProxyUrl(App.getProxy(context).getProxyUrl(newAudioFile.getAudioUrl()));
            MediaPlayerController.changeSong(fileDescriptor);
            // TODO:seekBar.setSecondaryProgress(seekBar.getMax());
            RadioActivity.setSecondarySeekbarMax();
            playSong(fileDescriptor);
        }


    }

    public static void playOrPause(Context context) throws IOException{
        Log.d(LOG_TAG,"playOrPause");
        if(Constant.nowPlayingAudio == null && Constant.nowPlayingFile == null){
            nextSong(context);
        }else{
            MediaPlayerController.playOrPause(TTSCacheController.getCurrentFile());
        }
    }

    private static void playSong(FileDescriptor fileDescriptor) throws IOException {
        Constant.nowPlayingFile = fileDescriptor;
        MediaPlayerController.play(fileDescriptor);
        // TODO:nowPlayingAudio = audioFile;
        //RadioActivity.registerCacheListener();
    }
}
