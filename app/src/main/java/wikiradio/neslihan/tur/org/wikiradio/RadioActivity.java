package wikiradio.neslihan.tur.org.wikiradio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.CategoryListCallback;

public class RadioActivity extends AppCompatActivity implements CategoryListCallback{
    private String LOG_TAG = RadioActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        long startTime = System.currentTimeMillis();
        DataUtils.getRandomCategory2("",this);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        Log.d(LOG_TAG,"elapsed time1: "+elapsedTime );

        startTime = System.currentTimeMillis();
        DataUtils.getRandomCategory("",this);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        Log.d(LOG_TAG,"elapsed time1: "+elapsedTime );

        startTime = System.currentTimeMillis();
        DataUtils.getRandomCategory2("",this);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        Log.d(LOG_TAG,"elapsed time2: "+elapsedTime );
    }

    @Override
    public void onSuccess(ArrayList<String> categoryList) {
        //for (String s : categoryList){
        //    Log.d(LOG_TAG,s);
        //}
        Log.d(LOG_TAG,categoryList.size()+"");
    }


    @Override
    public void onError() {

    }
}
