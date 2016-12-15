package wikiradio.neslihan.tur.org.wikiradio.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nesli on 27.10.2016.
 */

public class RestfulJsonThumbnail {
    @SerializedName("source")
    private String source;
    @SerializedName("width")
    private Integer width;
    @SerializedName("height")
    private Integer height;

    /**
     *
     * @return
     * The source
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     * The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return
     * The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     *
     * @param width
     * The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     *
     * @return
     * The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     *
     * @param height
     * The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

}
