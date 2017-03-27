package wikiradio.neslihan.tur.org.wikiradio.ttscache;

import wikiradio.neslihan.tur.org.wikiradio.model.TTSFile;

/**
 * Created by nesli on 11.01.2017.
 */

public interface CacheControlCallbackForTTS {
    void onFileConsumed(TTSFile currFile);
    void onNextFileRequested();
    void onEmptyCache();
}
