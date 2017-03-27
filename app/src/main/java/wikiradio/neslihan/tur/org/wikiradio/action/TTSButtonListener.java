package wikiradio.neslihan.tur.org.wikiradio.action;

import android.content.Context;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.RadioActivity;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.model.TTSFile;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallback;
import wikiradio.neslihan.tur.org.wikiradio.ttscache.CacheControlCallbackForTTS;
import wikiradio.neslihan.tur.org.wikiradio.ttscache.WikipediaSummaryCacheController;

/**
 * Created by nesli on 28.02.2017.
 */

public class TTSButtonListener{
    private static String LOG_TAG = TTSButtonListener.class.getName();
    public static CacheControlCallback cacheControlCallback;
    public static CacheControlCallbackForTTS cacheControlCallbackForTTS;
    private static Context waitingContext;
    public static boolean isActionWaits;



    public static void nextSong(Context context) throws IOException {
        Log.d(LOG_TAG,"nextSong");
        RadioActivity.unregisterCacheListener();


        //waste previously playing files, it can be an audio or TTS file
        if(Constant.isAudioPlaying && Constant.nowPlayingAudio != null){
            Log.d(LOG_TAG,"an audio is playing");
            cacheControlCallback.onFileConsumed(Constant.nowPlayingAudio.getAudioUrl());
        }
        else if(!Constant.isAudioPlaying && Constant.nowPlayingFile != null){
            Log.d(LOG_TAG,"a file is playing: "+Constant.nowPlayingFile.getFileName());
            cacheControlCallbackForTTS.onFileConsumed(Constant.nowPlayingFile);
        }

        //request next file
        cacheControlCallbackForTTS.onNextFileRequested();


        TTSFile selectedTTSFile = WikipediaSummaryCacheController.getInstance(context).getCurrentTTSFile();

        if(selectedTTSFile==null){
            Log.d(LOG_TAG,"selectedTTSFile == null");
            isActionWaits = true;
            waitingContext = context;
            RadioActivity.waitAnimation();
        }else {
            FileDescriptor fileDescriptor = selectedTTSFile.getFileDescriptor();

            Log.d(LOG_TAG,"newAudioFile is NOT null");
            //newAudioFile.setProxyUrl(App.getProxy(context).getProxyUrl(newAudioFile.getAudioUrl()));
            MediaPlayerController.changeSong(fileDescriptor);
            // TODO:seekBar.setSecondaryProgress(seekBar.getMax());
            RadioActivity.setSecondarySeekbarMax();
            playSong(selectedTTSFile);
        }
    }

    public static void playOrPause(Context context) throws IOException{
        Log.d(LOG_TAG,"playOrPause");
        if(Constant.nowPlayingAudio == null && Constant.nowPlayingFile == null){
            nextSong(context);
        }else{
            MediaPlayerController.playOrPause(WikipediaSummaryCacheController.getInstance(context).getCurrentTTSFile().getFileDescriptor());
        }
    }

    private static void playSong(TTSFile ttsFile) throws IOException {
        isActionWaits = false;
        Constant.nowPlayingFile = ttsFile;
        Constant.isAudioPlaying = false;
        MediaPlayerController.play(ttsFile.getFileDescriptor());
        // TODO:nowPlayingAudio = audioFile;
        //RadioActivity.registerCacheListener();
    }

    public static void onNewFileCached() {
        try {
            if(isActionWaits){
                //RadioActivity.replaceToast("operate waiting audio");
                Log.d(LOG_TAG,"onNewFileCached");
                isActionWaits = false;
                nextSong(waitingContext);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void seekToForward(){
        Log.d(LOG_TAG,"seekTo Duration:");
        MediaPlayerController.seekTo(MediaPlayerController.getCurrentPosition()+
                MediaPlayerController.getDuration()/10);
    }


    public static void seekToRewind(){
        Log.d(LOG_TAG,"seekTo Duration:");
        MediaPlayerController.seekTo(MediaPlayerController.getCurrentPosition()-
                MediaPlayerController.getDuration()/10);
    }

    // The structure is weird here. However there is no need to apply this on AudioFileButtonListener too
    public static void onStopActivity(){
        Log.d(LOG_TAG,"on stop activity");
        cacheControlCallback.onEmptyCache();
        cacheControlCallbackForTTS.onEmptyCache();
    }
}
