package wikiradio.neslihan.tur.org.wikiradio.model;

import android.os.AsyncTask;

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
    private Thread workerThread;
    private AsyncTask asyncTask;

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

    public Thread getWorkerThread() {
        return workerThread;
    }

    public void setWorkerThread(Thread workerThread) {
        this.workerThread = workerThread;
    }

    public void setThreadPtr(Future threadPtr) {
        this.threadPtr = threadPtr;
    }

    public AsyncTask getAsyncTask() {
        return asyncTask;
    }

    public void setAsyncTask(AsyncTask asyncTask) {
        this.asyncTask = asyncTask;
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
