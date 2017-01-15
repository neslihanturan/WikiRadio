package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import wikiradio.neslihan.tur.org.wikiradio.RadioActivity;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 15.01.2017.
 */

public class CacheControlService extends Service implements CacheListener,CacheControlCallback, AudioInfoCallbak {
    private String LOG_TAG = CacheControlService.class.getName();
    private HttpProxyCacheServer proxy ;
    public static CacheControllerReadyCallback cacheControllerReadyCallback;
    public static boolean onCreate;
    private HashSet<String> categorySet;
    private static ConcurrentHashMap<String,AudioFile> cachedURLMap = new ConcurrentHashMap<>();
    public static CacheControlFeedback cacheControlFeedbackActivity;
    public static CacheControlFeedback cacheControlFeedbackService;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG,"service is started");
        onCreate = true;
        proxy = App.getProxy(this);
        categorySet = getCategorySet(intent);
        cacheNewFile();
        cacheNewFile();
        RadioActivity.cacheControlCallback = this;
        cacheControllerReadyCallback.onCacheControllerReady();
        return START_STICKY;
    }

    private HashSet<String> getCategorySet(Intent intent){
        return (HashSet<String>) intent.getSerializableExtra("category_set");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        Log.d(LOG_TAG, String.format("onCacheAvailable. percents: %d, file: %s, url: %s", percentsAvailable, cacheFile, url));
    }

    @Override
    public void onFileConsumed(final AudioFile audioFile) {
        cachedURLMap.remove(audioFile.getUrl());
        Handler handler = new Handler();
        final Runnable r = new Runnable()
        {
            public void run()
            {

            Log.d(LOG_TAG,"on file consumed, title:"+audioFile.getTitle());
            audioFile.getAsyncTask().cancel(true);

            }
        };
        handler.postDelayed(r, 0);
        cacheNewFile(); //new file to cache
        return;
    }

    @Override
    public AudioFile onNextFileRequested() {
        Log.d(LOG_TAG,"on next file requested");
        while (cachedURLMap.isEmpty()){
            try {
                //TODO add timeout
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        AudioFile audioFile = cachedURLMap.elements().nextElement();
        cacheControlFeedbackActivity.onNewSongSet(audioFile);
        cacheControlFeedbackService.onNewSongSet(audioFile);
        Log.d(LOG_TAG,"selected audio is:"+audioFile.getTitle());
        return audioFile;
    }


    @Override
    public void onProcessCompleted() {
        Log.d(LOG_TAG,"on process completed");

    }

    @Override
    public void onSuccess(final AudioFile audioFile, Class sender) {
        Log.d(LOG_TAG,"on success thread:"+Thread.currentThread());
        Log.d(LOG_TAG,"girdi");

        proxy.registerCacheListener(this, audioFile.getUrl());
        audioFile.setAsyncTask(new KickStarterAsynctask());
        audioFile.getAsyncTask().execute(new String[]{audioFile.getUrl(), audioFile.getTitle()});

        //thread safe modificaion
        cachedURLMap.putIfAbsent(audioFile.getUrl(),audioFile);
        proxy.registerCacheListener(this, audioFile.getUrl());
        if(onCreate){
            onCreate = false;
            cacheControllerReadyCallback.onCacheControllerReady();
            //onProcessCompleted();
        }else{

        }

    }

    @Override
    public void onError(Class sender) {

    }


   /* private final class KickStarterRunnable implements Runnable {
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
    }*/

    private class KickStarterAsynctask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            kickStartVideoCaching(params[0],params[1]);
            return null;
        }

        private void kickStartVideoCaching(String originUrl,String title){
            Log.d(LOG_TAG,"kickstarted"+" thread is:"+Thread.currentThread()+" title is:"+title);
            //should I pass them to thread as parameter?

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
    }



    public interface CacheControlFeedback{
        void onNewSongSet(AudioFile audioFile);
        void onPlayingStateChanged();
    }

    private void cacheNewFile(){
        Log.d(LOG_TAG,"cache new file");
        DataUtils.getRandomAudio(categorySet,this);
    }
}
