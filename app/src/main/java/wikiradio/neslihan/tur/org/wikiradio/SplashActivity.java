package wikiradio.neslihan.tur.org.wikiradio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;

import retrofit2.Call;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.CategoryListCallback;
import wikiradio.neslihan.tur.org.wikiradio.data.interfaces.MwAPIInterface;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonObject;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlService;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControllerReadyCallback;

/**
 * Created by nesli on 09.01.2017.
 */

public class SplashActivity extends Activity implements CategoryListCallback, CacheControllerReadyCallback{
    private static String LOG_TAG = SplashActivity.class.getName();
    private HashSet<String> categorySet;
    private Toast toast;
    private int pendingRequests;
    private MwAPIInterface mwAPIService;
    @NonNull
    private Call<MwJsonObject> queryResponse ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        categorySet = new HashSet<>();
        DataUtils.getCategoryList(categorySet,Constant.EMPTY_STRING,this);
        CacheControlService.cacheControllerReadyCallback = this;
        //DataUtils.getRandomSummary(this);

    }

    public void startApplication(){
        Intent intent = new Intent(SplashActivity.this, RadioActivity.class);
        intent.putExtra("category_set", categorySet);
        startActivity(intent);
        finish();
    }
    /*public void startOrganizerService(){
        Log.d(LOG_TAG,"intentservice is started");
        Intent intent = new Intent(SplashActivity.this, CacheController.class);
        intent.putExtra("category_set", categorySet);
        this.startService(intent);
        //finish();
    }*/
    public void startOrganizerService(){
        Log.d(LOG_TAG,"service is started");
        CacheControlService.cacheControllerReadyCallback = this;
        Intent intent = new Intent(SplashActivity.this, CacheControlService.class);
        intent.putExtra("category_set", categorySet);
        this.startService(intent);
        //finish();
    }

    private void replaceToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onSuccess(HashSet<String> categoryList) {
        Log.d(LOG_TAG,"on succes category");
        this.categorySet = categoryList;
        startOrganizerService();
    }

    @Override
    public void onError(Class sender) {

    }

    @Override
    public void onCacheControllerReady() {
        startApplication();
    }
}
