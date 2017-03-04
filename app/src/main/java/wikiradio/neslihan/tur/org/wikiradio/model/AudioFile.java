package wikiradio.neslihan.tur.org.wikiradio.model;

import com.danikula.videocache.CacheListener;

import java.util.concurrent.Future;

/**
 * Created by nesli on 15.12.2016.
 */

public class AudioFile extends SourceFile{
    private String audioUrl;
    private boolean isFullyCached;
    private String proxyUrl;
    private String category;

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public boolean isFullyCached() {
        return isFullyCached;
    }

    public void setFullyCached(boolean fullyCached) {
        isFullyCached = fullyCached;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "AudioFile{" +
                "audioUrl='" + audioUrl + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                ", category='" + category + '\'' +
                '}';
    }
}
