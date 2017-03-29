package wikiradio.neslihan.tur.org.wikiradio.proxy;

import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 11.01.2017.
 */

public interface CacheControlCallback {
    void onFileConsumed(AudioFile curPtr);
    void onNextFileRequested();
    void onEmptyCache();
}
