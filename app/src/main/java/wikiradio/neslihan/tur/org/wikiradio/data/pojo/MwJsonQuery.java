package wikiradio.neslihan.tur.org.wikiradio.data.pojo;

import java.util.Map;

/**
 * Created by nesli on 17.10.2016.
 */

public class MwJsonQuery {
    public Map<String, MwJsonPage> pages;


    public Map<String, MwJsonPage> getPages() {
        return pages;
    }

    public void setPages(Map<String, MwJsonPage> pages) {
        this.pages = pages;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pages = "+pages+"]";
    }
}
