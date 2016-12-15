package wikiradio.neslihan.tur.org.wikiradio.data.pojo;

import java.util.Arrays;

/**
 * Created by nesli on 17.10.2016.
 */

public class MwJsonPage {
    private String pagelanguage;
    private String title;
    private MwJsonImage[] imageinfo;

    public MwJsonImage[] getImageinfo() {
        return imageinfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setImageinfo(MwJsonImage[] imageinfo) {
        this.imageinfo = imageinfo;
    }

    public String getPagelanguage() {
        return pagelanguage;
    }

    public void setPagelanguage(String pagelanguage) {
        this.pagelanguage = pagelanguage;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "pagelanguage='" + pagelanguage + '\'' +
                "title='" + title + '\'' +
                ", imageinfo=" + Arrays.toString(imageinfo) +
                '}';
    }
}
