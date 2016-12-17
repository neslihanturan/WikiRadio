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

import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.CategoryListCallback;

public class RadioActivity extends AppCompatActivity implements CategoryListCallback{
    private String LOG_TAG = RadioActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        double startTime = System.currentTimeMillis();
        DataUtils.getRandomCategory("",this);
        double stopTime = System.currentTimeMillis();
        double elapsedTime = stopTime - startTime;
        Log.d(LOG_TAG,"elapsed time first call: "+elapsedTime );

        startTime = System.currentTimeMillis();
        DataUtils.getRandomCategory("",this);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        Log.d(LOG_TAG,"elapsed time second call: "+elapsedTime );

        RetrieveFeedTask retrieveFeedTask=new RetrieveFeedTask();
        retrieveFeedTask.execute("https://upload.wikimedia.org/wikipedia/commons/f/f1/Banjo.ogg");


//208057 Banjo
        //331095 Star

        //10159 Dances

        //1915 Augustin
        //
    }

    @Override
    public void onSuccess(ArrayList<String> categoryList) {
        for (String s : categoryList){
            Log.d(LOG_TAG,s);
        }
        Log.d(LOG_TAG,categoryList.size()+"");
    }


    @Override
    public void onError() {

    }

    private class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        private Exception exception;

        protected Void doInBackground(String... urls) {
            int file_size = 0;
            try {
                URL url = new URL("https://upload.wikimedia.org/wikipedia/commons/6/60/Augustijn2.MID");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.addRequestProperty("User-Agent", "Mozilla/4.76");
                file_size = conn.getContentLength();


            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file_size <= 0) {
                Log.d(LOG_TAG,"downloaded file size "+file_size );
            } else {
                Log.d(LOG_TAG,"downloaded file size "+file_size );
            }
            return null;
        }

        protected void onPostExecute(Void feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }
}
