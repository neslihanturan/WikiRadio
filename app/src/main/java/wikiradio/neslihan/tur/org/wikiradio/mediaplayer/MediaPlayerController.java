package wikiradio.neslihan.tur.org.wikiradio.mediaplayer;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by nesli on 24.12.2016.
 */

public class MediaPlayerController {
    public static MediaPlayerCallback delegate = null;
    private static String pendingAction = "NONE";

    public static void play(String songurl) throws IOException {
        if(SingleMediaPlayer.getInstance().getState() >= MediaPlayerState.STATE_PREPARED){        // STATE_PREPARED | STATE_STARTED |  STATE_PAUSED
            SingleMediaPlayer.getInstance().playMediaPlayer();
            delegate.mediaPlayerPlaying();
        }else {
            pendingAction = "PLAY";
            cleanMediaPlayer();
            restartMediaPlayer(songurl);
        }
    }
    public static void play(FileDescriptor fileDescriptor) throws IOException {
        // STATE_PREPARED | STATE_STARTED |  STATE_PAUSED
        if(SingleMediaPlayer.getInstance().getState() >= MediaPlayerState.STATE_PREPARED){
            SingleMediaPlayer.getInstance().playMediaPlayer();
            delegate.mediaPlayerPlaying();
        }else {
            pendingAction = "PLAY";
            cleanMediaPlayer();
            restartMediaPlayer(fileDescriptor);
        }
    }
    public static void pause() throws IOException {
        //STATE_STARTED | STATE_PAUSED | STATE_PLAYBACK_COMPLETED
        if(SingleMediaPlayer.getInstance().getState() > MediaPlayerState.STATE_PREPARED || SingleMediaPlayer.getInstance().getState() == MediaPlayerState.STATE_PLAYBACK_COMPLETED){
            if(SingleMediaPlayer.getInstance().isPlaying()){
                SingleMediaPlayer.getInstance().pauseMediaPlayer();
                delegate.mediaPlayerPaused();
            }
        }
    }
    public static void changeSong(String songUrl) throws IOException{
        if(SingleMediaPlayer.getInstance().getState() == MediaPlayerState.STATE_IDLE){             // setDataSource is only valid in IDLE state
            SingleMediaPlayer.getInstance().setDataMediaPlayer(songUrl);
        }else {
            cleanMediaPlayer();
            changeSong(songUrl);
        }
    }
    public static void changeSong(FileDescriptor fileDescriptor) throws IOException{
        if(SingleMediaPlayer.getInstance().getState() == MediaPlayerState.STATE_IDLE){             // setDataSource is only valid in IDLE state
            SingleMediaPlayer.getInstance().setDataMediaPlayer(fileDescriptor);
        }else {
            cleanMediaPlayer();
            changeSong(fileDescriptor);
        }
    }
    public static void cleanMediaPlayer(){
        //Legal for all states
        SingleMediaPlayer.getInstance().resetMediaPlayer();
    }

    public static void restartMediaPlayer(String songUrl) throws IOException {
        if(SingleMediaPlayer.getInstance().getState() == MediaPlayerState.STATE_IDLE ){       // STATE_STOPPED | STATE_PLAYBACK_COMPLETED |  STATE_ERROR
            SingleMediaPlayer.getInstance().setDataMediaPlayer(songUrl);
            SingleMediaPlayer.getInstance().prepareMediaPlayer(songUrl);
        }
    }

    public static void restartMediaPlayer(FileDescriptor fileDescriptor) throws IOException {
        if(SingleMediaPlayer.getInstance().getState() == MediaPlayerState.STATE_IDLE ){       // STATE_STOPPED | STATE_PLAYBACK_COMPLETED |  STATE_ERROR
            SingleMediaPlayer.getInstance().setDataMediaPlayer(fileDescriptor);
            SingleMediaPlayer.getInstance().prepareMediaPlayer(fileDescriptor);
        }
    }



    public static void handlePendingAction(String songUrl) throws IOException {
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
