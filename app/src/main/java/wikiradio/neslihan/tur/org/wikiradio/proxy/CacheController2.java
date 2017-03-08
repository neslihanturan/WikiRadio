package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.danikula.videocache.file.Md5FileNameGenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.RadioActivity;
import wikiradio.neslihan.tur.org.wikiradio.action.AudioFileButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.action.TTSButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.notification.MusicIntentReceiver;

/**
 * Created by nesli on 29.01.2017.
 */

public class CacheController2 extends IntentService implements AudioInfoCallbak, CacheListener,CacheControlCallback, BackgroundCachingIsDone{
    private String LOG_TAG = CacheController2.class.getName();
    private static HttpProxyCacheServer proxy;
    private static HashSet<String> categorySet;
    private static HashMap<String , AudioFile> cachingFileHashMap;
    private static String currPtr;
    private static Context context;
    public static int expectedSize;
    private FileNameGenerator fileNameGenerator = new Md5FileNameGenerator();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CacheController2(String name) {
        super(name);
    }

    public CacheController2() {
        super("Cachecontroller2");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
            Log.d(LOG_TAG,"cache controller is started");
            //RadioActivity.cacheControlCallback = this;
            //MusicIntentReceiver.cacheControlCallback = this;
            //ButtonListener.cacheControlCallback = this;
            AudioFileButtonListener.cacheControlCallback = this;
            TTSButtonListener.cacheControlCallback = this;
            categorySet = Constant.categorySet;
            context = this;
            proxy = App.getProxy(this);
            cachingFileHashMap = new HashMap<>();
            cacheFilesOnBackground();
    }

    private void cacheFilesOnBackground(){
        if (expectedSize<4){
            Log.d(LOG_TAG,"caching files on background");
            DataUtils.getRandomAudio(categorySet,this);
        }
    }

    public static String getCurrentURL(){
        if(currPtr == null){
            return null;
        }else{
            String proxyUrl = proxy.getProxyUrl(currPtr);
            return proxyUrl;
        }
    }

    public static AudioFile getCurrentAudio(){
        if(currPtr != null){
            return cachingFileHashMap.get(currPtr);
        }else{
            return null;
        }
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        //Log.d(LOG_TAG, String.format("onCacheAvailable. percents: %d, file: %s", percentsAvailable, cacheFile));
        //if(percentsAvailable == 100){
            //cachingFileHashMap.get(url).setFullyCached(true);
        //}
    }

    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {
        Log.d(LOG_TAG,"on audio file url success");
        audioFile.setFullyCached(false);
        new BackgroundTask().execute(audioFile,(CacheListener)this,(BackgroundCachingIsDone)this);

    }

    @Override
    public void onError(Class sender) {

    }

    @Override
    public void onFileConsumed(String currPtr) {
        expectedSize--;
        //Log.d(LOG_TAG,"onfileconsumed title:"+cachingFileHashMap.get(currPtr).getTitle());

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
        if(cachingFileHashMap.containsKey(currPtr)){
            cachingFileHashMap.remove(currPtr);
        }


        cacheFilesOnBackground();
    }

    @Override
    public void onNextFileRequested() {
        Log.d(LOG_TAG,"on next file requested");
        //stopBackgroundCaching();
        selectNextFile();
    }

    private void selectNextFile(){
        Log.d(LOG_TAG,"on select next file");
        if(cachingFileHashMap.size() > 0){
            for(AudioFile audioFile : cachingFileHashMap.values()){
                currPtr = audioFile.getAudioUrl();
                return;
            }
        }else{
            currPtr = null;

        }
    }

    @Override
    public void onCurrentFileCached() {
        Log.d(LOG_TAG,"on current file cached");
        //continueBackgroundCaching();
    }

    @Override
    public void onBackgroundCachingIsDone(AudioFile audioFile) {
        expectedSize++;
        Log.d(LOG_TAG,"onBackgroundCachingIsDone started audiofile added to cache"+audioFile.getTitle());
        cachingFileHashMap.put(audioFile.getAudioUrl(),audioFile);
        cacheFilesOnBackground();
    }

    public class BackgroundTask extends AsyncTask<Object, AudioFile, AudioFile> {
        BackgroundCachingIsDone cachingIsDoneCallback;
        String LOG_TAG = BackgroundTask.class.getName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AudioFile doInBackground(Object... params) {
            Log.d(LOG_TAG,"do in background");
            AudioFile audioFile = (AudioFile) params[0];
            String originUrl = audioFile.getAudioUrl();
            String title = audioFile.getTitle();
            CacheListener context = (CacheListener) params[1];
            this.cachingIsDoneCallback = (BackgroundCachingIsDone) params[2];
            Log.d(LOG_TAG,"kickstarted"+" thread is:"+Thread.currentThread()+" title is:"+title);
            //should I pass them to thread as parameter?
            //proxy.registerCacheListener(context, originUrl);
            //proxy.unregisterCacheListener(this);
            URL url = null;
            String proxyUrl = proxy.getProxyUrl(originUrl);
            try {
                url = new URL(proxyUrl);
                InputStream inputStream = url.openStream();
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = 0;
                while ((length = inputStream.read(buffer)) != -1) {
                    //Since we just need to kick start the prefetching, dont need to do anything here
                    //  or we can use ByteArrayOutputStream to write down the data to disk
                }
                inputStream.close();
                audioFile.setFullyCached(true);
                audioFile.setProxyUrl(proxyUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return audioFile;
        }

        @Override
        protected void onPostExecute(AudioFile result) {
            Log.d(LOG_TAG,"on file is cached");
            cachingIsDoneCallback.onBackgroundCachingIsDone(result);
            super.onPostExecute(result);
        }

    }

}
