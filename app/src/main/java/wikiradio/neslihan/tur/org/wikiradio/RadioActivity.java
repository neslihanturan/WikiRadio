package wikiradio.neslihan.tur.org.wikiradio;

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
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

public class RadioActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        //double startTime = System.currentTimeMillis();
        Measurement measurement = new Measurement();
        measurement.prepareReport(true);
        //double stopTime = System.currentTimeMillis();
        //double elapsedTime = stopTime - startTime;
        //Log.d(LOG_TAG,"elapsed time first call: "+elapsedTime );

    }
}
