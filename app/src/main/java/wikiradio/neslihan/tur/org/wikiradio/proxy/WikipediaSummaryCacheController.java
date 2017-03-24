package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.content.Intent;

import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 28.02.2017.
 */

public class WikipediaSummaryCacheController extends AbstractCacheController implements BackgroundCachingIsDone{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WikipediaSummaryCacheController(String name) {
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
    public void onEmptyCache() {

    }

    @Override
    public void onBackgroundCachingIsDone(AudioFile audioFile) {

    }
}
