package wikiradio.neslihan.tur.org.wikiradio.data.callback;

import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 17.12.2016.
 */

public interface AllAudioInfo {
    void onSuccess(AudioFile audioFile, Class sender);
    void onError(Class sender);
}
