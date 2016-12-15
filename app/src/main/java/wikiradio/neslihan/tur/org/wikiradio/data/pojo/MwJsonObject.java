package nes.com.audiostreamer.model.gson;

import com.google.gson.annotations.SerializedName;

import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonContinue;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonQuery;


/**
 * Created by nesli on 17.10.2016.
 */

public class MwJsonObject {
    private MwJsonQuery query;

    @SerializedName("continue")
    private MwJsonContinue continueField;     //same continue object is used.

    private String batchcomplete;

    public MwJsonQuery getQuery ()
    {
        return query;
    }

    public void setQuery (MwJsonQuery query)
    {
        this.query = query;
    }

    public MwJsonContinue getContinueField ()
    {
        return continueField;
    }

    public void setContinueField (MwJsonContinue continueField)
    {
        this.continueField = continueField;
    }

    public String getBatchcomplete ()
    {
        return batchcomplete;
    }

    public void setBatchcomplete (String batchcomplete)
    {
        this.batchcomplete = batchcomplete;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [query = "+query+", continue = "+continueField+", batchcomplete = "+batchcomplete+"]";
    }
}
