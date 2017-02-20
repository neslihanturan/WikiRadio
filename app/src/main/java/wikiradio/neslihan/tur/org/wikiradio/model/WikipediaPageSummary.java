package wikiradio.neslihan.tur.org.wikiradio.model;

/**
 * Created by nesli on 17.02.2017.
 */

public class WikipediaPageSummary {
    private String title;
    private String extract;
    private String thumbnailSource;
    private int thumbnailHeight;
    private int thumbnailWidth;

    public WikipediaPageSummary(int thumbnailWeight, int thumbnailHeight, String thumbnailSource, String extract, String title) {
        this.thumbnailWidth = thumbnailWeight;
        this.thumbnailHeight = thumbnailHeight;
        this.thumbnailSource = thumbnailSource;
        this.extract = extract;
        this.title = title;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    public String getThumbnailSource() {
        return thumbnailSource;
    }

    public void setThumbnailSource(String thumbnailSource) {
        this.thumbnailSource = thumbnailSource;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

