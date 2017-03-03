package wikiradio.neslihan.tur.org.wikiradio.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.io.IOException;

import wikiradio.neslihan.tur.org.wikiradio.action.AudioSourceSelector;

/**
 * Created by nesli on 07.02.2017.
 */

public class MusicIntentReceiver extends BroadcastReceiver{
    private static String LOG_TAG = MusicIntentReceiver.class.getName();
    private Context context;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        String action = intent.getAction();
        context = ctx;

        //TODO: null p. e. if push activity before service. Because onReceive never triggered.


        if (action.equals(
                android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
        }else{
            try {
                //operate proper action
                AudioSourceSelector.operate(action,context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void lock(){

    }

    public static void unlock(){

    }

}
