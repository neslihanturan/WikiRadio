package wikiradio.neslihan.tur.org.wikiradio.ttscache;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import wikiradio.neslihan.tur.org.wikiradio.RadioActivity;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.SummaryCallback;
import wikiradio.neslihan.tur.org.wikiradio.model.WikipediaPageSummary;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallback;

/**
 * Created by nesli on 15.02.2017.
 */

public class TTSCacheController extends IntentService implements SummaryCallback, TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener, CacheControlCallback{
    private static TTSCacheController INSTANCE;
    private static String LOG_TAG = TTSCacheController.class.getName();
    private static int expectedSize;
    private ArrayList<String> cachedFiles = new ArrayList<>();
    private static HashSet<WikipediaPageSummary> wikipediaPageSummaries;
    private HashMap<String, String> uteranceIDFileNameMap = new HashMap<>();
    private TextToSpeech ttobj;
    private Context context;
    private String candidateFileName;
    private static String selectedFileName;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TTSCacheController(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG," TTSCacheController()");
        RadioActivity.ttsCacheControlCallback = this;
        ttobj = new TextToSpeech(context.getApplicationContext(),this);
        this.context = this;
    }

    private void cacheFilesOnBackground(){
        if (cachedFiles.size()<4){
            Log.d(LOG_TAG,"caching tts files on background");
            DataUtils.getRandomSummary(this);
        }
    }

    @Override
    public void onSuccess(WikipediaPageSummary wikipediaPageSummary) {
        wikipediaPageSummaries.add(wikipediaPageSummary);
        HashMap<String, String> myHashRender = new HashMap();
        String destinationFileName = Environment.getExternalStorageDirectory().getPath() + "/" + wikipediaPageSummary.getTitle();
        String textToConvert = wikipediaPageSummary.getExtract();
        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, textToConvert);
        ttobj.synthesizeToFile(textToConvert, myHashRender, destinationFileName);
        candidateFileName = destinationFileName;
        ttobj.setOnUtteranceCompletedListener(this);

    }

    @Override
    public void onError() {

    }

    //on TTS init
    @Override
    public void onInit(int status) {
        ttobj.setLanguage(Locale.US);
        cacheFilesOnBackground();
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        cachedFiles.add(candidateFileName);
        expectedSize++;
        cacheFilesOnBackground();
    }

    @Override
    public void onFileConsumed() {
        cachedFiles.remove(selectedFileName);
        File fdelete = new File(selectedFileName);
        if (fdelete.delete()) {
            Log.d(LOG_TAG,"onfileconsumedfile Deleted :");
        } else {
            Log.d(LOG_TAG,"onfileconsumedfile not Deleted :");
        }
    }

    @Override
    public void onNextFileRequested() {
        selectNextFile();
    }

    @Override
    public void onCurrentFileCached() {

    }

    private FileDescriptor selectNextFile(){
        if(cachedFiles.isEmpty()){
            return null;
        }else{
            FileInputStream fileInputStream;
            FileDescriptor fd = null;
            try {
                selectedFileName = cachedFiles.get(0);
                fileInputStream = new FileInputStream(new File(selectedFileName));
                fd = fileInputStream.getFD();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return fd;
        }
    }

    public static FileDescriptor getCurrentFile(){
        if(selectedFileName!=null){
            FileDescriptor fd = null;
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(selectedFileName));
                fd = fileInputStream.getFD();;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fd;
        }else {
            return null;
        }

    }


}
