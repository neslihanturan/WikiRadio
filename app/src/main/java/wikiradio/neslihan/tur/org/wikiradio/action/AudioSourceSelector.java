package wikiradio.neslihan.tur.org.wikiradio.action;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;

import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 28.02.2017.
 */

public class AudioSourceSelector {
    private static String LOG_TAG = AudioSourceSelector.class.getName();

    private static String selectAudioSource(Context context){
        Log.d(LOG_TAG,"selectAudioSource");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean onlyCommons = preferences.getBoolean("only_commons",false);
        boolean onlyTTS = preferences.getBoolean("only_tts",false);
        boolean both = preferences.getBoolean("both",true);

        if (onlyCommons){
            Log.d(LOG_TAG,"ONLYCOMMONS");
            return "ONLYCOMMONS";
        }
        else if (onlyTTS){
            Log.d(LOG_TAG,"ONLYTTS");
            return "ONLYTTS";
        }
        else if (both){
            Log.d(LOG_TAG,"BOTH");
            return "BOTH";
        }
        return null;
    }

    public static void operate(String action, Context context) throws IOException {
        Log.d(LOG_TAG,"operate");
        String audioSourceSelection = selectAudioSource(context);

        if (audioSourceSelection.equals("ONLYCOMMONS")){
            audioFileActions(action, context);
        }
        else if (audioSourceSelection.equals("ONLYTTS")){
            ttsActions(action, context);
        }
        else if (audioSourceSelection.equals("BOTH")){
            if (Math.random() < 0.5) {
                audioFileActions(action, context);
                Log.d(LOG_TAG,"audioFileActions selected");
            }
            else {
                ttsActions(action, context);
                Log.d(LOG_TAG,"ttsActions selected");
            }
        }
    }

    private static void audioFileActions(String action, Context context) throws IOException {
        Log.d(LOG_TAG,"audioFileActions");
        if(action.equals(Constant.ACTION.NEXT_ACTION)){
            AudioFileButtonListener.nextSong(context);
        }
        else if(action.equals(Constant.ACTION.PLAY_ACTION)){
            AudioFileButtonListener.playOrPause(context);
        }
    }

    private static void ttsActions(String action, Context context) throws IOException {
        Log.d(LOG_TAG,"ttsActions");
        if(action.equals(Constant.ACTION.NEXT_ACTION)){
            TTSButtonListener.nextSong(context);
        }
        else if(action.equals(Constant.ACTION.PLAY_ACTION)){
            TTSButtonListener.playOrPause(context);
        }
    }


}
