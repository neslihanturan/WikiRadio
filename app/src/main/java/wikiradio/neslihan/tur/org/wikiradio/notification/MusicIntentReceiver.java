package wikiradio.neslihan.tur.org.wikiradio.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.SingleMediaPlayer;

/**
 * Created by nesli on 15.01.2017.
 */

public class MusicIntentReceiver extends BroadcastReceiver {
    MediaPlayer mediaPlayer = null;
    String songUrl;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        String action = intent.getAction();

        songUrl = getExtra(intent);
        mediaPlayer = SingleMediaPlayer.getInstance();

        if (action.equals(
                android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
        }else{
            if(Constant.ACTION.PLAY_ACTION.equals(action)) {
                try {
                    MediaPlayerController.play(songUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("i","Pressed Play");
            } else if(Constant.ACTION.PAUSE_ACTION.equals(action)) {
                try {
                    MediaPlayerController.pause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("i","Pressed Next");
            } else if(Constant.ACTION.NEXT_ACTION.equals(action)) {
                try {
                    MediaPlayerController.pause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("i","Pressed Next");
            }
        }
    }

    private String getExtra(Intent intent){
        Bundle extras = intent.getExtras();
        if(extras == null) {
            return null;
        } else {
            return extras.getString("songUrl");
        }
    }

}
