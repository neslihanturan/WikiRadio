package wikiradio.neslihan.tur.org.wikiradio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.CategoryListCallback;

public class RadioActivity extends AppCompatActivity implements CategoryListCallback{
    private String LOG_TAG = RadioActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        DataUtils.getRandomCategory("",this);
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
}
