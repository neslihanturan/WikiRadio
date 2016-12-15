package wikiradio.neslihan.tur.org.wikiradio.data.pojo;

/**
 * Created by nesli on 17.10.2016.
 */

public class MwJsonImage {
    private String descriptionurl;

    private String descriptionshorturl;

    private String canonicaltitle;

    private String mime;

    private String user;

    private String mediatype;

    private String url;

    public String getDescriptionurl ()
    {
        return descriptionurl;
    }

    public void setDescriptionurl (String descriptionurl)
    {
        this.descriptionurl = descriptionurl;
    }

    public String getDescriptionshorturl ()
    {
        return descriptionshorturl;
    }

    public void setDescriptionshorturl (String descriptionshorturl)
    {
        this.descriptionshorturl = descriptionshorturl;
    }

    public String getCanonicaltitle ()
    {
        return canonicaltitle;
    }

    public void setCanonicaltitle (String canonicaltitle)
    {
        this.canonicaltitle = canonicaltitle;
    }

    public String getMime ()
    {
        return mime;
    }

    public void setMime (String mime)
    {
        this.mime = mime;
    }

    public String getUser ()
    {
        return user;
    }

    public void setUser (String user)
    {
        this.user = user;
    }

    public String getMediatype ()
    {
        return mediatype;
    }

    public void setMediatype (String mediatype)
    {
        this.mediatype = mediatype;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [descriptionurl = "+descriptionurl+", descriptionshorturl = "+descriptionshorturl+", canonicaltitle = "+canonicaltitle+", mime = "+mime+", user = "+user+", mediatype = "+mediatype+", url = "+url+"]";
    }
}
