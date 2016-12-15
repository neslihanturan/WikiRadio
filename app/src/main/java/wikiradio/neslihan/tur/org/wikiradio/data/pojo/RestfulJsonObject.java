package wikiradio.neslihan.tur.org.wikiradio.data.pojo;

import com.google.gson.annotations.SerializedName;


/**
 * Created by nesli on 27.10.2016.
 */

public class RestfulJsonObject {

    @SerializedName("title")
    private String title;
    @SerializedName("extract")
    private String extract;
    @SerializedName("thumbnail")
    private RestfulJsonThumbnail thumbnail;
    @SerializedName("lang")
    private String lang;
    @SerializedName("dir")
    private String dir;
    @SerializedName("timestamp")
    private String timestamp;

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The extract
     */
    public String getExtract() {
        return extract;
    }

    /**
     *
     * @param extract
     * The extract
     */
    public void setExtract(String extract) {
        this.extract = extract;
    }

    /**
     *
     * @return
     * The thumbnail
     */
    public RestfulJsonThumbnail getThumbnail() {
        return thumbnail;
    }

    /**
     *
     * @param thumbnail
     * The thumbnail
     */
    public void setThumbnail(RestfulJsonThumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     *
     * @return
     * The lang
     */
    public String getLang() {
        return lang;
    }

    /**
     *
     * @param lang
     * The lang
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     *
     * @return
     * The dir
     */
    public String getDir() {
        return dir;
    }

    /**
     *
     * @param dir
     * The dir
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     *
     * @return
     * The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     * The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}