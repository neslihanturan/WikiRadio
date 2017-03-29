package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.content.Context;
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
import wikiradio.neslihan.tur.org.wikiradio.action.AudioFileButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.action.TTSButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.model.TTSFile;

/**
 * Created by nesli on 28.02.2017.
 */

public class CommonsCacheController extends AbstractCacheController implements BackgroundCachingIsDone, AudioInfoCallbak{
    private static String LOG_TAG = CommonsCacheController.class.getName();
    private static CommonsCacheController INSTANCE;

    private Context context;
    private HashMap<String , AudioFile> cachingFileHashMap;
    private HttpProxyCacheServer proxy;
    private HashSet<String> categorySet;
    private AudioFile currPtr;
    private FileNameGenerator fileNameGenerator = new Md5FileNameGenerator();


    private CommonsCacheController(Context context){
        TTSButtonListener.cacheControlCallback = this;
        AudioFileButtonListener.cacheControlCallback = this;
        this.context = context;
        proxy = App.getProxy(context);
        categorySet = Constant.categorySet;
        cachingFileHashMap = new HashMap<>();
    }

    public static CommonsCacheController getInstance(Context context) {
        Log.d(LOG_TAG," getInstance()");
        if(INSTANCE == null){
            INSTANCE = new CommonsCacheController(context);
        }
        return INSTANCE;
    }

    @Override
    public void startToCaching() {
        cacheFilesOnBackground();
    }

    @Override
    public void cacheFilesOnBackground() {
        if (cachingFileHashMap.size()< Constant.CACHE.COMMONS_CACHE_LIMIT){
            Log.d(LOG_TAG,"caching files on background");
            DataUtils.getRandomAudio(categorySet,this);
        }
    }

    @Override
    public void selectNextFile() {
        Log.d(LOG_TAG,"on select next file");
        if(cachingFileHashMap.size() > 0){
            for(AudioFile audioFile : cachingFileHashMap.values()){
                currPtr = audioFile;
                return;
            }
        }else{
            currPtr = null;
        }
    }

    @Override
    public TTSFile getCurrentTTSFile() {
        return null;
    }

    @Override
    public AudioFile getCurrentAudioFile() {
        if(currPtr != null){
            return cachingFileHashMap.get(currPtr);
        }else{
            return null;
        }
    }

    public String getCurrentProxyURL(){
        if(currPtr == null){
            return null;
        }else{
            String proxyUrl = proxy.getProxyUrl(currPtr.getAudioUrl());
            return proxyUrl;
        }
    }

    @Override
    public void onFileConsumed(String curPtr) {
        File fdelete;

        if(proxy.isCached(currPtr.getAudioUrl())){
            fdelete = new File(context.getExternalCacheDir()+"/video-cache/"+fileNameGenerator.generate(currPtr.getAudioUrl()));
        }else {
            fdelete = new File(context.getExternalCacheDir()+"/video-cache/"+fileNameGenerator.generate(currPtr.getAudioUrl())+".download");
        }
        if (fdelete.delete()) {
            Log.d(LOG_TAG,"onfileconsumedfile Deleted :" + context.getExternalCacheDir()+"/video-cache/"+fileNameGenerator.generate(currPtr.getAudioUrl()));
        } else {
            Log.d(LOG_TAG,"onfileconsumedfile not Deleted :" + context.getExternalCacheDir()+"/video-cache/"+fileNameGenerator.generate(currPtr.getAudioUrl()));
        }
        if(cachingFileHashMap.containsKey(currPtr)){
            cachingFileHashMap.remove(currPtr);
        }
        cacheFilesOnBackground();
    }

    @Override
    public void onFileConsumed(TTSFile currFile) {
        // Do nothing
    }

    @Override
    public void onNextFileRequested() {
        Log.d(LOG_TAG,"on next file requested");
        selectNextFile();
    }

    @Override
    public void onEmptyCache() {
        Log.d(LOG_TAG,"onEmptyCache");
        File dir = new File(context.getExternalCacheDir()+"/video-cache/");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
                Log.d(LOG_TAG,"file is deleted");
            }
        }
    }

    @Override
    public void onBackgroundCachingIsDone(AudioFile audioFile) {
        Log.d(LOG_TAG,"onBackgroundCachingIsDone started audiofile added to cache"+audioFile.getTitle());
        cachingFileHashMap.put(audioFile.getAudioUrl(),audioFile);
        cacheFilesOnBackground();
    }

    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {
        Log.d(LOG_TAG,"on audio file url success thread:"+Thread.currentThread());
        audioFile.setFullyCached(false);
        new CommonsCacheController.BackgroundTask().execute(audioFile,(BackgroundCachingIsDone)this);
    }

    @Override
    public void onError(Class sender) {

    }

    private class BackgroundTask extends AsyncTask<Object, AudioFile, AudioFile> {
        BackgroundCachingIsDone cachingIsDoneCallback;
        String LOG_TAG = CommonsCacheController.BackgroundTask.class.getName();

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
            this.cachingIsDoneCallback = (BackgroundCachingIsDone) params[1];
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
