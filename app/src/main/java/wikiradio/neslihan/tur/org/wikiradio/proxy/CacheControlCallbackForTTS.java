package wikiradio.neslihan.tur.org.wikiradio.proxy;

/**
 * Created by nesli on 11.01.2017.
 */

public interface CacheControlCallbackForTTS {
    void onFileConsumed(String currPtr);
    void onNextFileRequested();
    void onCurrentFileCached();
    void onEmptyCache();
}
