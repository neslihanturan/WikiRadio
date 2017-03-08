package wikiradio.neslihan.tur.org.wikiradio.proxy;

/**
 * Created by nesli on 11.01.2017.
 */

public interface CacheControlCallback {
    void onFileConsumed(String curPtr);
    void onNextFileRequested();
    void onCurrentFileCached();
}
