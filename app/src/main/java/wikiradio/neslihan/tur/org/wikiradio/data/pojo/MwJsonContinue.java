package wikiradio.neslihan.tur.org.wikiradio.data.pojo;

import com.google.gson.annotations.SerializedName;

public class MwJsonContinue{
    private String gaccontinue;
    @SerializedName("continue")
    private String continueField;

    private String psoffset;

    public String getContinueField() {
        return continueField;
    }

    public void setContinueField(String continueField) {
        this.continueField = continueField;
    }

    public void setPsoffset(String psoffset) {
        this.psoffset = psoffset;
    }

    public String getPsoffset ()
    {
        return psoffset;
    }

    public String getGaccontinue() {
        return gaccontinue;
    }

    public void setGaccontinue(String gaccontinue) {
        this.gaccontinue = gaccontinue;
    }

    /*public String getPsoffset ()
    {
        return gaccontinue;
    }*/

    /*public void setPsoffset (String psoffset)
    {
        this.gaccontinue = psoffset;
    }*/

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