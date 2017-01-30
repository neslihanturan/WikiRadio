package wikiradio.neslihan.tur.org.wikiradio.proxy;

import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 30.01.2017.
 */

public interface BackgroundCachingIsDone {
    void onBackgroundCachingIsDone(AudioFile audioFile);
}
