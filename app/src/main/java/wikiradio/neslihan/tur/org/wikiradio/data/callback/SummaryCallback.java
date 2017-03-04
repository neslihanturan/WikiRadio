package wikiradio.neslihan.tur.org.wikiradio.data.callback;

import java.util.HashSet;

import wikiradio.neslihan.tur.org.wikiradio.model.TTSFile;
import wikiradio.neslihan.tur.org.wikiradio.model.WikipediaPageSummary;

/**
 * Created by nesli on 17.02.2017.
 */

public interface SummaryCallback {
    void onSuccess(TTSFile ttsFile);
    void onError();
}
