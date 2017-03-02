package wikiradio.neslihan.tur.org.wikiradio.action;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nesli on 28.02.2017.
 */

public class AudioSourceSelector {
    private static String selectAudioSource(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean onlyCommons = preferences.getBoolean("only_commons",false);
        boolean onlyTTS = preferences.getBoolean("only_tts",false);
        boolean both = preferences.getBoolean("both",true);

        if (onlyCommons){
            return "ONLYCOMMONS";
        }
        else if (onlyTTS){
            return "ONLYTTS";
        }
        else if (both){
            return "BOTH";
        }

        return null;
    }

    public static void operate(String audioSource, Context context){
        String audioSourceSelection = selectAudioSource(context);

        
    }

}
