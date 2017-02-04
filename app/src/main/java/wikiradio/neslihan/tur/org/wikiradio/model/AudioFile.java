package wikiradio.neslihan.tur.org.wikiradio.model;

import com.danikula.videocache.CacheListener;

import java.util.concurrent.Future;

/**
 * Created by nesli on 15.12.2016.
 */

public class AudioFile {
    private String url;
    private String title;
    private int size;
    private String category;
    private int percentsAvailable;
    private CacheListener cacheListener;
    private Future threadPtr;
    private boolean isFullyCached;
    private Thread thread;
    private String proxyUrl;

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPercentsAvailable() {
        return percentsAvailable;
    }

    public void setPercentsAvailable(int percentsAvailable) {
        this.percentsAvailable = percentsAvailable;
    }

    public CacheListener getCacheListener() {
        return cacheListener;
    }

    public void setCacheListener(CacheListener cacheListener) {
        this.cacheListener = cacheListener;
    }

    public Future getThreadPtr() {
        return threadPtr;
    }

    public void setThreadPtr(Future threadPtr) {
        this.threadPtr = threadPtr;
    }

    @Override
    public String toString() {
        return "AudioFile{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                ", category='" + category + '\'' +
                '}';
    }
}
