package wikiradio.neslihan.tur.org.wikiradio.data.pojo;

import com.google.gson.annotations.SerializedName;

public class MwJsonContinue{
    private String gaccontinue;
    @SerializedName("continue")
    private String continueField;

    public String getPsoffset ()
    {
        return gaccontinue;
    }

    public void setPsoffset (String psoffset)
    {
        this.gaccontinue = psoffset;
    }

    public String getContinue ()
    {
        return continueField;
    }

    public void setContinue (String _continue)
    {
        this.continueField = _continue;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [psoffset = "+gaccontinue+", continue = "+continueField+"]";
    }
}