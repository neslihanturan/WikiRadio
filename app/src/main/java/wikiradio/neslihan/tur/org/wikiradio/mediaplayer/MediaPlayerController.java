package wikiradio.neslihan.tur.org.wikiradio.mediaplayer;

import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by nesli on 24.12.2016.
 */

public class MediaPlayerController {
    private static String LOG_TAG = MediaPlayerController.class.getName();
    public static MediaPlayerCallback delegate = null;
    private static String pendingAction = "NONE";

    public static void playOrPause(String songurl) throws IOException{
        //isPlaying() is legal at any state except STATE_ERROR
        if(SingleMediaPlayer.getInstance().getState() != MediaPlayerState.STATE_ERROR){
            if(SingleMediaPlayer.getInstance().isPlaying()){
                pause();
            }else{
                play(songurl);
            }
        }
    }
    public static void playOrPause(FileDescriptor fileDescriptor) throws IOException{
        //isPlaying() is legal at any state except STATE_ERROR
        if(SingleMediaPlayer.getInstance().getState() != MediaPlayerState.STATE_ERROR){
            if(SingleMediaPlayer.getInstance().isPlaying()){
                pause();
            }else{
                play(fileDescriptor);
            }
        }
    }
    public static void play(String songurl) throws IOException {
        Log.d(LOG_TAG," play(String songurl)");
        // STATE_PREPARED | STATE_STARTED |  STATE_PAUSED
        if(SingleMediaPlayer.getInstance().getState() >= MediaPlayerState.STATE_PREPARED){
            SingleMediaPlayer.getInstance().playMediaPlayer();
            delegate.onMediaPlayerPlaying();
        }else {
            pendingAction = "PLAY";
            cleanMediaPlayer();
            restartMediaPlayer(songurl);
        }
    }
    private static void play(FileDescriptor fileDescriptor) throws IOException {
        Log.d(LOG_TAG," play(FileDescriptor fileDescriptor)");
        // STATE_PREPARED | STATE_STARTED |  STATE_PAUSED
        if(SingleMediaPlayer.getInstance().getState() >= MediaPlayerState.STATE_PREPARED){
            SingleMediaPlayer.getInstance().playMediaPlayer();
            delegate.onMediaPlayerPlaying();
        }else {
            pendingAction = "PLAY";
            cleanMediaPlayer();
            restartMediaPlayer(fileDescriptor);
        }
    }
    public static void pause() throws IOException {
        Log.d(LOG_TAG," pause()");
        //STATE_STARTED | STATE_PAUSED | STATE_PLAYBACK_COMPLETED
        if(SingleMediaPlayer.getInstance().getState() > MediaPlayerState.STATE_PREPARED || SingleMediaPlayer.getInstance().getState() == MediaPlayerState.STATE_PLAYBACK_COMPLETED){
            if(SingleMediaPlayer.getInstance().isPlaying()){
                SingleMediaPlayer.getInstance().pauseMediaPlayer();
                delegate.onMediaPlayerPaused();
            }
        }
    }
    public static void changeSong(String songUrl) throws IOException{
        Log.d(LOG_TAG," changeSong(String songUrl)");
        // STATE_IDLE
        if(SingleMediaPlayer.getInstance().getState() == MediaPlayerState.STATE_IDLE){
            SingleMediaPlayer.getInstance().setDataMediaPlayer(songUrl);
        }else {
            cleanMediaPlayer();
            changeSong(songUrl);
        }
    }
    public static void changeSong(FileDescriptor fileDescriptor) throws IOException{
        Log.d(LOG_TAG," changeSong(FileDescriptor fileDescriptor)");
        // STATE_IDLE
        if(SingleMediaPlayer.getInstance().getState() == MediaPlayerState.STATE_IDLE){
            SingleMediaPlayer.getInstance().setDataMediaPlayer(fileDescriptor);
        }else {
            cleanMediaPlayer();
            changeSong(fileDescriptor);
        }
    }
    public static void cleanMediaPlayer(){
        Log.d(LOG_TAG," cleanMediaPlayer()");
        // Legal for all states
        SingleMediaPlayer.getInstance().resetMediaPlayer();
    }
    public static void restartMediaPlayer(String songUrl) throws IOException {
        Log.d(LOG_TAG," restartMediaPlayer(String songUrl)");
        // STATE_STOPPED | STATE_PLAYBACK_COMPLETED |  STATE_ERROR
        if(SingleMediaPlayer.getInstance().getState() == MediaPlayerState.STATE_IDLE ){
            SingleMediaPlayer.getInstance().setDataMediaPlayer(songUrl);
            SingleMediaPlayer.getInstance().prepareMediaPlayer(songUrl);
        }
    }
    public static void restartMediaPlayer(FileDescriptor fileDescriptor) throws IOException {
        Log.d(LOG_TAG," restartMediaPlayerFileDescriptor fileDescriptor)");
        // STATE_STOPPED | STATE_PLAYBACK_COMPLETED |  STATE_ERROR
        if(SingleMediaPlayer.getInstance().getState() == MediaPlayerState.STATE_IDLE ){
            SingleMediaPlayer.getInstance().setDataMediaPlayer(fileDescriptor);
            SingleMediaPlayer.getInstance().prepareMediaPlayer(fileDescriptor);
        }
    }
    public static String getNowPlayingURL(){
        if(SingleMediaPlayer.getInstance().getState() >= MediaPlayerState.STATE_INITIALIZED){
            return SingleMediaPlayer.getInstance().getDataSourceURL();
        }else{
            return null;
        }
    }
    public static void handlePendingAction(String songUrl) throws IOException {
        Log.d(LOG_TAG," handlePendingAction(String songUrl)");
        switch (pendingAction){
            case "NONE":
                break;
            case "PLAY":
                play(songUrl);
                pendingAction = "NONE";
                break;
        }
    }
    public static void handlePendingAction(FileDescriptor fileDescriptor) throws IOException {
        Log.d(LOG_TAG," handlePendingAction(FileDescriptor fileDescriptor)");
        switch (pendingAction){
            case "NONE":
                break;
            case "PLAY":
                play(fileDescriptor);
                pendingAction = "NONE";
                break;
        }
    }

}