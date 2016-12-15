package wikiradio.neslihan.tur.org.wikiradio.data.interfaces;

import wikiradio.neslihan.tur.org.wikiradio.Constant;

/**
 * Sellects corresponding Interface for retrofit object
 */

public class InterfaceSelector {
    public static Class getInterfaceClass(String strategy){
        if(strategy.equals(Constant.MEDIA_WIKI_API)){
            return MwAPIInterface.class;
        }
        else if(strategy.equals(Constant.REST_API)){
            return RestfulAPIInterface.class;
        }
        else if(strategy.equals(Constant.AUDIO_STREAM)){
            return AudioStreamInterface.class;
        }
        else{
            return null;
        }
    }
}
