package wikiradio.neslihan.tur.org.wikiradio.ttscache;

import android.content.Context;
import android.os.AsyncTask;
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
import java.util.Locale;

import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.action.AudioFileButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.action.TTSButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.SummaryCallback;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.model.TTSFile;
import wikiradio.neslihan.tur.org.wikiradio.proxy.AbstractCacheController;

/**
 * Created by nesli on 28.02.2017.
 */

public class WikipediaSummaryCacheController extends AbstractCacheController implements TextToSpeech.OnUtteranceCompletedListener,TextToSpeech.OnInitListener,SummaryCallback {
    private static String LOG_TAG = WikipediaSummaryCacheController.class.getName();
    private static WikipediaSummaryCacheController INSTANCE;

    private Context context;
    private TextToSpeech ttobj;
    private ArrayList<TTSFile> cachedFiles = new ArrayList<>();
    private TTSFile candidateFile;
    private TTSFile currentFile;

    private WikipediaSummaryCacheController(Context context){
        TTSButtonListener.cacheControlCallbackForTTS = this;
        AudioFileButtonListener.cacheControlCallbackForTTS = this;
        this.context = context;
    }

    public static WikipediaSummaryCacheController getInstance(Context context) {
        Log.d(LOG_TAG," getInstance()");
        if(INSTANCE == null){
            INSTANCE = new WikipediaSummaryCacheController(context);
        }
        return INSTANCE;
    }

    @Override
    public void startToCaching() {
        cacheFilesOnBackground();
    }

    @Override
    public void cacheFilesOnBackground() {
        if(ttobj == null){
            // Creating tts object may be slow sometimes
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG,"AsyncTask");
                    ttobj = new TextToSpeech(context, WikipediaSummaryCacheController.this);
                }
            });
        }else if (cachedFiles.size()< Constant.CACHE.TTS_CACHE_LIMIT){
            Log.d(LOG_TAG,"cacheFilesOnBackground");
            DataUtils.getRandomSummary(this);
        }
    }

    /*
    * Selects one of cached files to play most recently
    * */
    @Override
    public void selectNextFile() {
        Log.d(LOG_TAG,"selectNextFile");
        if(cachedFiles.size()<=0){
            currentFile = null;
        }else{
            FileInputStream fileInputStream;
            FileDescriptor fd;
            try {
                currentFile = cachedFiles.get(0);
                fileInputStream = new FileInputStream(new File(currentFile.getFileName()));
                fd = fileInputStream.getFD();
                currentFile.setFileDescriptor(fd);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * Returns first file to be used from cache
    * */
    @Override
    public TTSFile getCurrentTTSFile() {
            Log.d(LOG_TAG,"getCurrentFile");
            if(currentFile !=null){
                Log.d(LOG_TAG,"current file: "+currentFile.getFileName());
                return currentFile;
            }else {
                Log.d(LOG_TAG,"current file is null");
                return null;
            }
    }

    @Override
    public AudioFile getCurrentAudioFile() {
        // Do nothing
        return null;
    }

    @Override
    public void onFileConsumed(String curPtr) {
        // Do nothing
    }

    /*
    * Means the file is listened and terminated/changed by user
    * */
    @Override
    public void onFileConsumed(TTSFile currFile) {
        Log.d(LOG_TAG,"onFileConsumed");

        // Remove from caching files array
        if(currFile!=null && cachedFiles.contains(currFile)){
            Log.d(LOG_TAG,"onFileConsumed2 removed from array:"+currFile.getFileName());
            cachedFiles.remove(currFile);
        }

        // Remove from memory
        File fdelete = new File(currFile.getFileName());
        if (fdelete.delete()) {
            Log.d(LOG_TAG,"onfileconsumedfile Deleted :"+currFile.getFileName());
        } else {
            Log.d(LOG_TAG,"onfileconsumedfile not Deleted :"+currFile.getFileName());
        }
    }

    @Override
    public void onNextFileRequested() {
        Log.d(LOG_TAG,"onNextFileRequested");
        selectNextFile();
    }

    /*
    * Means we remove all added files from cache, ie. when we kill the app
    * */
    @Override
    public void onEmptyCache() {
        Log.d(LOG_TAG,"onEmptyCache");
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/ttscache/");
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

    /*
    * Means ttsobj is ready
    * */
    @Override
    public void onInit(int status) {
        Log.d(LOG_TAG,"onInit TTS");
        ttobj.setLanguage(Locale.US);
        cacheFilesOnBackground();
    }

    /*
    *  Means text to speech process is done and a sound file is ready
    * */
    @Override
    public void onUtteranceCompleted(String utteranceId) {
        Log.d(LOG_TAG,"onUtteranceCompleted");
        cachedFiles.add(candidateFile);
        TTSButtonListener.onNewFileCached();
        cacheFilesOnBackground();
    }

    /*
    * Means text of a random summary from en.wikipedia is ready
    * */
    @Override
    public void onSuccess(TTSFile ttsFile) {
        Log.d(LOG_TAG,"onSuccess Summary");
        HashMap<String, String> myHashRender = new HashMap();

        File folder = new File(Environment.getExternalStorageDirectory() + "/"+Constant.CACHE.TTS_CACHE_DIR);
        if (!folder.exists()) {
            folder.mkdir(); // create dir if not exists
        }
        // Request to write speech of text file to an audio file
        String destinationFileName = Environment.getExternalStorageDirectory().getPath() + "/"+Constant.CACHE.TTS_CACHE_DIR+"/" +ttsFile.getTitle();
        String textToConvert = ttsFile.getExtract();
        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, textToConvert);
        ttobj.synthesizeToFile(textToConvert, myHashRender, destinationFileName); // don't worry, this process is asynchronous
        ttsFile.setFileName(destinationFileName);
        candidateFile = ttsFile;
        ttobj.setOnUtteranceCompletedListener(this);
    }

    /*
    * Means an error occurred while a random summary from en.wikipedia is get
    * */
    @Override
    public void onError() {
        Log.d(LOG_TAG,"an error occured");
        cacheFilesOnBackground();
    }

    public  void destroyTTS(){
        //Close the Text to Speech Library
        if(ttobj != null) {

            ttobj.stop();
            ttobj.shutdown();
            Log.d(LOG_TAG, "TTS Destroyed");
        }
    }
}
