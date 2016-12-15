package wikiradio.neslihan.tur.org.wikiradio.data.pojo;

/**
 * Created by nesli on 15.12.2016.
 */

public class MwJsonPrefixsearch {
    private int ns;
    private String title;
    private int pageid;

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNs() {
        return ns;
    }

    public void setNs(int ns) {
        this.ns = ns;
    }
}
