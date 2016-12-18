package wikiradio.neslihan.tur.org.wikiradio.model;

/**
 * Created by nesli on 15.12.2016.
 */

public class AudioFile {
    private String url;
    private String title;
    private double size;
    private String category;

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

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
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
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                ", category='" + category + '\'' +
                '}';
    }
}
