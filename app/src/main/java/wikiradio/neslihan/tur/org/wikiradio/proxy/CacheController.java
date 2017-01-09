package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;

import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 09.01.2017.
 * IntentService is used to not wait main thread
 */

public class CacheController extends IntentService implements AudioInfoCallbak{
    public static String LOG_TAG = CacheController.class.getName();
    private static int lastPtr;
    private static int currPtr;
    public static ArrayList<String> cachedURLs = new ArrayList<>();
    private static HashSet<String> categorySet;
    private static CountDownLatch startSignal;

    public CacheController() {
        super("CacheController");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CacheController(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.d(LOG_TAG,"cache controller is started");
            categorySet = getCategorySet(intent);
            updateCachedURLs();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private HashSet<String> getCategorySet(Intent intent){
        return (HashSet<String>) intent.getSerializableExtra("category_set");
    }

    private void updateCachedURLs() throws InterruptedException {
        while(lastPtr<4){
            Log.d(LOG_TAG,"add random audio");
            DataUtils.getRandomAudio(categorySet,this);
            startSignal = new CountDownLatch(1);
            startSignal.await();
            lastPtr++;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {
        cachedURLs.add(audioFile.getUrl()); //get next audio file url
        Log.d(LOG_TAG,"audio file :"+audioFile.getTitle());
        startSignal.countDown();
    }

    @Override
    public void onError(Class sender) {

    }
}
