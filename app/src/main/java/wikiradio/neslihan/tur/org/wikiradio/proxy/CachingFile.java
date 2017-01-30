package wikiradio.neslihan.tur.org.wikiradio.proxy;

/**
 * Created by nesli on 29.01.2017.
 */

public class CachingFile {
    private String url;
    private Thread thread;
    private boolean isFullyCached;
    private Runnable runnable;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public boolean isFullyCached() {
        return isFullyCached;
    }

    public void setFullyCached(boolean fullyCached) {
        isFullyCached = fullyCached;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
