package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileCache;
import com.danikula.videocache.file.FileNameGenerator;
import com.danikula.videocache.file.Md5FileNameGenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.RadioActivity;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.SingleMediaPlayer;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 09.01.2017.
 * IntentService is used instead of Service to not wait main thread
 */

public class CacheController extends IntentService implements AudioInfoCallbak, CacheListener,CacheControlCallback{
    public static String LOG_TAG = CacheController.class.getName();
    private static int lastPtr;
    private static String currPtr;
    private static HashMap<String,AudioFile> cachedURLMap = new HashMap<>();
    //private static HashMap<String,Integer> cachedURLMapByURL = new HashMap<>();
    public static ArrayList<String> cachedURLs = new ArrayList<>();
    private static HashSet<String> categorySet;
    private static CountDownLatch startSignal;
    private static Context context;
    private ExecutorService executor = Executors.newFixedThreadPool(4);
    private FileNameGenerator fileNameGenerator = new Md5FileNameGenerator();
    private static HttpProxyCacheServer proxy;

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
            context = this;
            //threadGroup = new ThreadGroup("CahceController");
            Log.d(LOG_TAG,"cache controller is started");
            RadioActivity.cacheControlCallback = this;
            categorySet = getCategorySet(intent);
            updateCachedURLs();
            proxy = App.getProxy(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private HashSet<String> getCategorySet(Intent intent){
        return (HashSet<String>) intent.getSerializableExtra("category_set");
    }

    private void updateCachedURLs() throws InterruptedException {
        Log.d(LOG_TAG,"updateCachedURLs, number of cached files:"+cachedURLMap.size());
        int size = cachedURLMap.size();
        int need = Constant.MAX_CACHED_FILE - size;
        while(need > 0){
            Log.d(LOG_TAG,"add random audio");
            DataUtils.getRandomAudio(categorySet,this);
            need --;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {
        Log.d(LOG_TAG,"onSuccess"+" thread is:"+Thread.currentThread());
        audioFile.setThreadPtr(executor.submit(new Thread(new KickStarterRunnable(audioFile.getUrl(), audioFile.getTitle()))));
        Log.d(LOG_TAG,"onSuccess created thread is "+audioFile.getThreadPtr());
        cachedURLs.add(audioFile.getUrl()); //get next audio file url
        cachedURLMap.put(audioFile.getUrl(),audioFile);
        Log.d(LOG_TAG,"audio file :"+audioFile.getTitle());

    }

    public static String getCurrentURL(){
        if(currPtr ==null){
            return null;
        }else{
            String proxyUrl = proxy.getProxyUrl(currPtr);
            return proxyUrl;
        }
    }

    public static AudioFile getCurrentAudio(){
        if(currPtr ==null){
            return null;
        }else{
            String proxyUrl = proxy.getProxyUrl(currPtr);
            return cachedURLMap.get(currPtr);
        }
    }

    @Override
    public void onFileConsumed() {
        Log.d(LOG_TAG,"onfileconsumed title:"+cachedURLMap.get(currPtr).getTitle());
        cachedURLMap.get(currPtr).getThreadPtr().cancel(true);

        File fdelete;

        if(proxy.isCached(currPtr)){
            fdelete = new File(context.getExternalCacheDir()+"/video-cache/"+fileNameGenerator.generate(currPtr));
        }else {
            fdelete = new File(context.getExternalCacheDir()+"/video-cache/"+fileNameGenerator.generate(currPtr)+".download");
        }
        if (fdelete.delete()) {
            Log.d(LOG_TAG,"onfileconsumedfile Deleted :" + context.getExternalCacheDir()+"/video-cache/"+fileNameGenerator.generate(currPtr));
        } else {
            Log.d(LOG_TAG,"onfileconsumedfile not Deleted :" + context.getExternalCacheDir()+"/video-cache/"+fileNameGenerator.generate(currPtr));
        }
        cachedURLMap.remove(currPtr);
    }

    @Override
    public void onNextFileRequested() {
            Log.d(LOG_TAG,"next file requested, number of cached files:"+cachedURLMap.size());
            currPtr = getMax();
    }

    private void stopOtherThreads(){
        //TODO: sleep other threads to give priority to currently playing audio

        for (Map.Entry<String, AudioFile> entry : cachedURLMap.entrySet())
        {
            if(entry.getKey().equals(currPtr)){
                continue;
            }
            //cancel all threads other than currently playing thread
            entry.getValue().getThreadPtr().cancel(true);
            Log.d(LOG_TAG,"Thread for:"+entry.getValue().getTitle()+"is stopped");
            //executor.shutdown();
        }
    }
    private void wakeUpOtherThreads(){
        //TODO: wakeUp other threads after currently playing audio is fully cached
        for (Map.Entry<String, AudioFile> entry : cachedURLMap.entrySet())
        {
            if(entry.getKey().equals(currPtr)){
                continue;
            }
            //reactivate all cancelled threads
            entry.getValue().setThreadPtr(executor.submit(new Thread(new KickStarterRunnable(entry.getValue().getUrl(), entry.getValue().getTitle()))));
            Log.d(LOG_TAG,"Thread for:"+entry.getValue().getTitle()+"is started");
        }
    }

    @Override
    public void onProcessCompleted() {
        try {
            updateCachedURLs();
            stopOtherThreads();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCurrentFileCached() {
        wakeUpOtherThreads();
    }

    private final class KickStarterRunnable implements Runnable {
        String url;
        String title;

        public KickStarterRunnable(String url, String title) {
            this.url = url;
            this.title = title;
        }

        @Override
        public void run() {
            kickStartVideoCaching(url, title);
        }
    }

    private void kickStartVideoCaching(String originUrl,String title){
        Log.d(LOG_TAG,"kickstarted"+" thread is:"+Thread.currentThread()+" title is:"+title);
        //should I pass them to thread as parameter?
        proxy.registerCacheListener(this, originUrl);
        //proxy.unregisterCacheListener(this);
        URL url = null;
        try {
            url = new URL(proxy.getProxyUrl(originUrl));
            InputStream inputStream = url.openStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                //Since we just need to kick start the prefetching, dont need to do anything here
                //  or we can use ByteArrayOutputStream to write down the data to disk
            }
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Class sender) {

    }

    //TODO: remove this listener, only use for secondaryProgress of seekbar, on RadioActivity
    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {

        //Log.d(LOG_TAG, String.format("onCacheAvailable. percents: %d, file: %s, title: %s", percentsAvailable, cacheFile, cachedURLMap.get(url).getTitle()));
        Log.d(LOG_TAG, String.format("onCacheAvailable. percents: %d, title: %s", percentsAvailable, cachedURLMap.get(url).getTitle()));
        if(cachedURLMap.containsKey(url)){
            cachedURLMap.get(url).setPercentsAvailable(percentsAvailable);
        }
    }

    private static String getMax(){
        Map.Entry<String, AudioFile> maxEntry = null;

        for (Map.Entry<String, AudioFile> entry : cachedURLMap.entrySet())
        {
            //if only consider percentsAvailable, we tend to choose shorter ones.
            //compare audiofile size * cachedPercentile
            if (maxEntry == null ||
                    (entry.getValue().getSize()*entry.getValue().getPercentsAvailable())
                            >
                            ((maxEntry.getValue().getSize()*maxEntry.getValue().getPercentsAvailable())))
            {
                maxEntry = entry;
            }
            Log.d(LOG_TAG,"** cachedURLMap entry audio title is:"+entry.getValue().getTitle()+"\n" +
                    " its percent is:"+entry.getValue().getPercentsAvailable()+"\n" +
                    "its bytes available is:"+entry.getValue().getPercentsAvailable()*entry.getValue().getSize()/100);
        }
        Log.d(LOG_TAG,"selected audio is:"+maxEntry.getValue().getTitle()+" its percents is:"+maxEntry.getValue().getPercentsAvailable());
        return maxEntry.getKey();
    }
}
