package wikiradio.neslihan.tur.org.wikiradio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;

import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.FileUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.CategoryListCallback;
import wikiradio.neslihan.tur.org.wikiradio.proxy.App;

/**
 * Created by nesli on 09.01.2017.
 */

public class SplashActivity extends Activity implements CategoryListCallback{
    private static String LOG_TAG = SplashActivity.class.getName();
    private Toast toast;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        Constant.proxy = App.getProxy(this);
        if(!sharedPref.contains("firstRun")){
            replaceToast("it is first run");
            DataUtils.getCategoryList(Constant.categorySet,Constant.EMPTY_STRING,this);

        }else{
            replaceToast("not first run");
            Constant.categorySet = FileUtils.readFromFile(this);
            startOrganizerService();
            startApplication();
        }

    }

    public void startApplication(){
        Intent intent = new Intent(SplashActivity.this, RadioActivity.class);
        startActivity(intent);
        finish();
    }
    public void startOrganizerService(){
        Log.i(LOG_TAG,"service is started");
        //Intent intent = new Intent(SplashActivity.this, CacheController2.class);
        //this.startService(intent);
        //Intent intent2 = new Intent(SplashActivity.this, TTSCacheController.class);
        //this.startService(intent2);
        
    }
    private void replaceToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public void onSuccess(HashSet<String> categorySet) {
        Constant.categorySet = categorySet;
        FileUtils.saveToFile(categorySet, "categoryset.txt",this);
        startOrganizerService();
        startApplication();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("firstRun", false);
        editor.commit();
    }

    @Override
    public void onError(Class sender) {

    }
}
