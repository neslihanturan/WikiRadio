package wikiradio.neslihan.tur.org.wikiradio.ttscache;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import wikiradio.neslihan.tur.org.wikiradio.RadioActivity;
import wikiradio.neslihan.tur.org.wikiradio.action.AudioFileButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.action.TTSButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.SummaryCallback;
import wikiradio.neslihan.tur.org.wikiradio.model.TTSFile;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallbackForTTS;

/**
 * Created by nesli on 15.02.2017.
 */

public class TTSCacheController extends Service implements SummaryCallback, TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener, CacheControlCallbackForTTS{
    private static String LOG_TAG = TTSCacheController.class.getName();
    private ArrayList<TTSFile> cachedFiles = new ArrayList<>();
    private static TextToSpeech ttobj;
    private Context context;
    private TTSFile candidateFile;
    private static TTSFile selectedFile;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG," onHandleIntent");
        //RadioActivity.ttsCacheControlCallback = this;
        TTSButtonListener.cacheControlCallbackForTTS = this;
        AudioFileButtonListener.cacheControlCallbackForTTS = this;
        this.context = this;
        cacheFilesOnBackground();
        return super.START_STICKY;
    }

    public static  void destroyTTS(){
        //Close the Text to Speech Library
        if(ttobj != null) {

            ttobj.stop();
            ttobj.shutdown();
            Log.d(LOG_TAG, "TTS Destroyed");
        }
    }

    private void cacheFilesOnBackground(){
        if(ttobj == null){
            ttobj = new TextToSpeech(context,this);
        }else if (cachedFiles.size()<4){
            Log.d(LOG_TAG,"cacheFilesOnBackground");
            DataUtils.getRandomSummary(this);
        }
    }

    @Override
    public void onSuccess(TTSFile ttsFile) {
        Log.d(LOG_TAG,"onSuccess Summary");
        HashMap<String, String> myHashRender = new HashMap();

        File folder = new File(Environment.getExternalStorageDirectory() + "/ttscache");
        boolean success = true;
        if (!folder.exists()) {
            folder.mkdir(); // create dir if not exists
        }

        String destinationFileName = Environment.getExternalStorageDirectory().getPath() + "/ttscache/" +ttsFile.getTitle();
        //context.getExternalCacheDir()+"/tts-cache/"+ttsFile.getTitle();
        String textToConvert = ttsFile.getExtract();
        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, textToConvert);
        ttobj.synthesizeToFile(textToConvert, myHashRender, destinationFileName);
        ttsFile.setFileName(destinationFileName);
        candidateFile = ttsFile;
        Log.d(LOG_TAG,"onSuccess Summary2" + candidateFile.getFileName());
        ttobj.setOnUtteranceCompletedListener(this);

    }

    @Override
    public void onError() {
        Log.d(LOG_TAG,"onError Summary");
    }

    //on TTS init
    @Override
    public void onInit(int status) {
        Log.d(LOG_TAG,"onInit TTS");
        ttobj.setLanguage(Locale.US);
        cacheFilesOnBackground();
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        Log.d(LOG_TAG,"onUtteranceCompleted TTS:" + candidateFile.getFileName());
        cachedFiles.add(candidateFile);
        TTSButtonListener.onNewFileCached();
        cacheFilesOnBackground();

    }

    @Override
    public void onFileConsumed(String curPtr) {
        //RadioActivity.replaceToast("TTS-onFileConsumed");
        Log.d(LOG_TAG,"onFileConsumed");
        //cachedFiles.contains(curPtr)
        if(selectedFile!=null && cachedFiles.contains(selectedFile)){
            Log.d(LOG_TAG,"onFileConsumed2 removed from array:"+selectedFile.getFileName());
            cachedFiles.remove(selectedFile);
        }

        File fdelete = new File(curPtr);
        if (fdelete.delete()) {
            Log.d(LOG_TAG,"onfileconsumedfile Deleted :"+curPtr);
        } else {
            Log.d(LOG_TAG,"onfileconsumedfile not Deleted :"+curPtr);
        }
    }

    @Override
    public void onNextFileRequested() {
        //RadioActivity.replaceToast("TTS-onNextFileRequested");
        Log.d(LOG_TAG,"onNextFileRequested");
        selectNextFile();
    }

    @Override
    public void onCurrentFileCached() {
        Log.d(LOG_TAG,"onCurrentFileCached");
    }

    @Override
    public void onEmptyCache() {
        Log.d(LOG_TAG,"worked");
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

    private FileDescriptor selectNextFile(){
        Log.d(LOG_TAG,"selectNextFile");
        if(cachedFiles.size()<=0){
            //RadioActivity.replaceToast("TTS-cached files is empty");
            Log.d(LOG_TAG,"cachedFiles not empty size is:"+cachedFiles.size());
            for(int i = 0; i<cachedFiles.size();i++){
                Log.d(LOG_TAG,i+". element is:"+cachedFiles.get(i));
            }
            selectedFile = null;
            return null;
        }else{
            FileInputStream fileInputStream;
            FileDescriptor fd = null;
            try {
                selectedFile = cachedFiles.get(0);
                //RadioActivity.replaceToast("TTS-selected file name is:"+ selectedFile.getFileName());
                fileInputStream = new FileInputStream(new File(selectedFile.getFileName()));
                fd = fileInputStream.getFD();
                selectedFile.setFileDescriptor(fd);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return fd;
        }
    }

    public static TTSFile getCurrentFile(){
        Log.d(LOG_TAG,"getCurrentFile");
        if(selectedFile !=null){
            /*FileDescriptor fd = null;
            try {
                Log.d(LOG_TAG,"selected file name :"+selectedFile.getFileName());
                FileInputStream fileInputStream = new FileInputStream(new File(selectedFile.getFileName()));
                fd = fileInputStream.getFD();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectedFile.setFileDescriptor(fd);*/
            Log.d(LOG_TAG,"getCurrentFile2"+selectedFile.getFileName());
            return selectedFile;
        }else {
            return null;
        }
    }
}
