package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.content.Intent;
import android.speech.tts.TextToSpeech;

/**
 * Created by nesli on 28.02.2017.
 */

public class CommonsCacheController extends AbstractCacheController implements TextToSpeech.OnUtteranceCompletedListener,TextToSpeech.OnInitListener {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CommonsCacheController(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onFileConsumed(String curPtr) {

    }

    @Override
    public void onNextFileRequested() {

    }

    @Override
    public void onCurrentFileCached() {

    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {

    }
}
