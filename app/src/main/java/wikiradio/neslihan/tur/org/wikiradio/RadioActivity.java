package wikiradio.neslihan.tur.org.wikiradio;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.CategoryListCallback;
import wikiradio.neslihan.tur.org.wikiradio.data.statistic.Measurement;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerCallback;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

public class RadioActivity extends AppCompatActivity implements MediaPlayerCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        MediaPlayerController.delegate = this;

        try {
            MediaPlayerController.play("https://upload.wikimedia.org/wikipedia/commons/b/ba/Aboun.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //double startTime = System.currentTimeMillis();
        //Measurement measurement = new Measurement();
        //measurement.prepareReport(true);
        //double stopTime = System.currentTimeMillis();
        //double elapsedTime = stopTime - startTime;
        //Log.d(LOG_TAG,"elapsed time first call: "+elapsedTime );

    }

    @Override
    public void onMediaPlayerPaused() {

    }

    @Override
    public void onMediaPlayerPlaying() {

    }
}
