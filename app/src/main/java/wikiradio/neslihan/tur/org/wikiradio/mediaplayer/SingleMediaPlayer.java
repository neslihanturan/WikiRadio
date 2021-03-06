package wikiradio.neslihan.tur.org.wikiradio.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.action.AudioSourceSelector;
import wikiradio.neslihan.tur.org.wikiradio.notification.NotificationService;

/**
 * Created by nesli on 24.12.2016.
 */
public class SingleMediaPlayer extends MediaPlayer {
    private static String LOG_TAG = SingleMediaPlayer.class.getName();
    private int state;
    private static SingleMediaPlayer INSTANCE;
    private String dataSourceURL;

    private SingleMediaPlayer(){
        Log.i(LOG_TAG," SingleMediaPlayer()");
        this.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.state = MediaPlayerState.STATE_IDLE;
    }
    public static SingleMediaPlayer getInstance() {
        Log.i(LOG_TAG," getInstance()");
        if(INSTANCE == null){
            INSTANCE = new SingleMediaPlayer();
        }
        return INSTANCE;
    }
    public static void setDataMediaPlayer(String songUrl) throws IOException {
        Log.i(LOG_TAG," setDataMediaPlayer(String songUrl)");
        INSTANCE.setDataSource(songUrl);
        INSTANCE.dataSourceURL = songUrl;
        INSTANCE.state = MediaPlayerState.STATE_INITIALIZED;
    }
    public static void setDataMediaPlayer(FileDescriptor fileDescriptor) throws IOException {
        Log.i(LOG_TAG," setDataMediaPlayer(FileDescriptor fileDescriptor)");
        INSTANCE.setDataSource(fileDescriptor);
        INSTANCE.state = MediaPlayerState.STATE_INITIALIZED;
    }
    public static void prepareMediaPlayer(final String songUrl) throws IOException {
        Log.i(LOG_TAG," prepareMediaPlayer(final String songUrl)");
        INSTANCE.prepareAsync();
        INSTANCE.state = MediaPlayerState.STATE_PREPARING;
        INSTANCE.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(LOG_TAG," onPrepared(MediaPlayer mp)");
                try {
                    INSTANCE.state = MediaPlayerState.STATE_PREPARED;
                    MediaPlayerController.handlePendingAction(songUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        INSTANCE.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i(LOG_TAG," onError(MediaPlayer mp, int what, int extra)");
                INSTANCE.state = MediaPlayerState.STATE_ERROR;
                return false;
            }
        });
        INSTANCE.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i(LOG_TAG," onCompletion(MediaPlayer mp)");
                INSTANCE.state = MediaPlayerState.STATE_PLAYBACK_COMPLETED;
                try {
                    AudioSourceSelector.operate(Constant.ACTION.NEXT_ACTION,NotificationService.context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void prepareMediaPlayer(final FileDescriptor fileDescriptor) throws IOException {
        Log.i(LOG_TAG," prepareMediaPlayer(final String songUrl)");
        INSTANCE.prepareAsync();
        INSTANCE.state = MediaPlayerState.STATE_PREPARING;
        INSTANCE.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(LOG_TAG," onPrepared(MediaPlayer mp)");
                try {
                    INSTANCE.state = MediaPlayerState.STATE_PREPARED;
                    MediaPlayerController.handlePendingAction(fileDescriptor);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        INSTANCE.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i(LOG_TAG," onError(MediaPlayer mp, int what, int extra)");
                INSTANCE.state = MediaPlayerState.STATE_ERROR;
                return false;
            }
        });
        INSTANCE.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i(LOG_TAG," onCompletion(MediaPlayer mp)");
                INSTANCE.state = MediaPlayerState.STATE_PLAYBACK_COMPLETED;
                try {
                    AudioSourceSelector.operate(Constant.ACTION.NEXT_ACTION,NotificationService.context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void playMediaPlayer(){
        Log.i(LOG_TAG," playMediaPlayer()");
        INSTANCE.start();
        INSTANCE.state = MediaPlayerState.STATE_STARTED;
    }
    public void pauseMediaPlayer(){
        Log.i(LOG_TAG," pauseMediaPlayer()");
        INSTANCE.pause();
        INSTANCE.state = MediaPlayerState.STATE_PAUSED;
    }
    public void stopMediaPlayer(){
        Log.i(LOG_TAG," stopMediaPlayer()");
        INSTANCE.stop();
        INSTANCE.state = MediaPlayerState.STATE_STOPPED;
    }
    public void releaseMediaPlayer(){
        Log.i(LOG_TAG," releaseMediaPlayer()");
        INSTANCE.release();
        INSTANCE.state = MediaPlayerState.STATE_RELEASED;
    }
    public String getDataSourceURL(){
        return dataSourceURL;
    }

    /**
     * After reset(), the object is like being just created.
     * Legal for all states
     */
    public void resetMediaPlayer(){
        Log.i(LOG_TAG," resetMediaPlayer()");
        INSTANCE.reset();
        INSTANCE.state = MediaPlayerState.STATE_IDLE;
    }
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void seekTo(int msec) throws IllegalStateException {
        // allowed at {Prepared, Started, Paused, PlaybackCompleted}
        // permitted at {Idle, Initialized, Stopped, Error}
        Log.i(LOG_TAG,"seel to");
        super.seekTo(msec);
    }
}
