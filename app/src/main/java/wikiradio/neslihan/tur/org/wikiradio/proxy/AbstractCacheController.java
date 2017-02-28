package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.app.IntentService;

/**
 * Created by nesli on 28.02.2017.
 */

public abstract class AbstractCacheController extends IntentService implements CacheControlCallback {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AbstractCacheController(String name) {
        super(name);
    }
}
