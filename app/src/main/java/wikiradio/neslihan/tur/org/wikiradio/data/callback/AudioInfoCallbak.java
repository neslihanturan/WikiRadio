package wikiradio.neslihan.tur.org.wikiradio.data.callback;

import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Call when get audio information data process end with success or error
 */

public interface AudioInfoCallbak {
    void onSuccess(AudioFile audioFile, boolean isCategoryNonEmpty);
    void onError();
}
