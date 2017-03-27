package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;

import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.model.TTSFile;

/**
 * Created by nesli on 28.02.2017.
 */

public class CommonsCacheController extends AbstractCacheController implements BackgroundCachingIsDone, AudioInfoCallbak{
    private String LOG_TAG = CommonsCacheController.class.getName();
    private static CommonsCacheController INSTANCE;

    private static HashMap<String , AudioFile> cachingFileHashMap;
    private static HashSet<String> categorySet;


    private CommonsCacheController(){

    }

    public CommonsCacheController getInstance() {
        Log.d(LOG_TAG," getInstance()");
        if(INSTANCE == null){
            INSTANCE = new CommonsCacheController();
        }
        return INSTANCE;
    }

    @Override
    public void startToCaching() {
        cacheFilesOnBackground();
    }

    @Override
    public void cacheFilesOnBackground() {
        if (cachingFileHashMap.size()<4){
            Log.d(LOG_TAG,"caching files on background");
            DataUtils.getRandomAudio(categorySet,this);
        }
    }

    @Override
    public void selectNextFile() {

    }

    @Override
    protected TTSFile getCurrentTTSFile() {
        return null;
    }

    @Override
    protected AudioFile getCurrentAudioFile() {
        return null;
    }

    @Override
    public void onFileConsumed(String curPtr) {

    }

    @Override
    public void onFileConsumed(TTSFile currFile) {

    }

    @Override
    public void onNextFileRequested() {

    }

    @Override
    public void onEmptyCache() {

    }

    @Override
    public void onBackgroundCachingIsDone(AudioFile audioFile) {

    }


    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {

    }

    @Override
    public void onError(Class sender) {

    }
}
