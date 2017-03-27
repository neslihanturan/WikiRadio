package wikiradio.neslihan.tur.org.wikiradio.proxy;

import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.model.TTSFile;
import wikiradio.neslihan.tur.org.wikiradio.ttscache.CacheControlCallbackForTTS;

/**
 * Created by nesli on 28.02.2017.
 */

public abstract class AbstractCacheController implements CacheControlCallback, CacheControlCallbackForTTS {

    protected abstract void cacheFilesOnBackground();
    protected abstract void selectNextFile();
    protected abstract TTSFile getCurrentTTSFile();
    protected abstract AudioFile getCurrentAudioFile();
    public abstract void startToCaching();

}
