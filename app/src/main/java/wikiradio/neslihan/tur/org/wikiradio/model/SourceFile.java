package wikiradio.neslihan.tur.org.wikiradio.model;

/**
 * Created by nesli on 04.03.2017.
 */

public class SourceFile {
    //private inheritance is not possible
    protected String pageUrl;
    protected String title;
    protected int size;

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
