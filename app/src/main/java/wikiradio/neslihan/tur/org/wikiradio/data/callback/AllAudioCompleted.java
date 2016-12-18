package wikiradio.neslihan.tur.org.wikiradio.data.callback;

import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 18.12.2016.
 */

public interface AllAudioCompleted {
    void onSuccess(Class sender);
    void onError(Class sender);
}
