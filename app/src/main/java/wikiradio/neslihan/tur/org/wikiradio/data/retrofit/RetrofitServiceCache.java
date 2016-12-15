package wikiradio.neslihan.tur.org.wikiradio.data.retrofit;

import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.data.interfaces.AudioStreamInterface;
import wikiradio.neslihan.tur.org.wikiradio.data.interfaces.InterfaceSelector;
import wikiradio.neslihan.tur.org.wikiradio.data.interfaces.MwAPIInterface;
import wikiradio.neslihan.tur.org.wikiradio.data.interfaces.RestfulAPIInterface;
import wikiradio.neslihan.tur.org.wikiradio.data.interfaces.RetrofitMarkerInterface;

/**
 * This is semi singletone class, we have 3 instances and we are trying not to create them because they are memory
 * intensive objects
 */

public class RetrofitServiceCache {
    static RetrofitMarkerInterface INSTANCE;
    private static MwAPIInterface MwINSTANCE;
    private static AudioStreamInterface AsINSTANCE;
    private static RestfulAPIInterface RestINSTANCE;

    public static RetrofitMarkerInterface getService(String baseURL, String strategy){
        //case: getting summaries from en.wikipedia.org with restful api
        if(baseURL.equals(Constant.EN_WIKIPEDIA_BASE_URL) && strategy.equals(Constant.REST_API)){
            if(RestINSTANCE == null){
                RestINSTANCE = (RestfulAPIInterface) RetrofitFactoryProducer.getFactory(baseURL)
                                    .getClient().create(InterfaceSelector
                                    .getInterfaceClass(strategy));
            }
            INSTANCE = RestINSTANCE;
        }
        //case: getting category and audio informations from commons with mediawikiAPI
        else if(baseURL.equals(Constant.COMMONS_BASE_URL) && strategy.equals(Constant.MEDIA_WIKI_API)){
            if(MwINSTANCE == null){
                MwINSTANCE = (MwAPIInterface) RetrofitFactoryProducer.getFactory(baseURL)
                                .getClient().create(InterfaceSelector
                                .getInterfaceClass(strategy));
            }
            INSTANCE = MwINSTANCE;
        }
        //case: getting audio stream from commons
        else if(baseURL.equals(Constant.COMMONS_BASE_URL) && strategy.equals(Constant.AUDIO_STREAM)){
            if(AsINSTANCE == null){
                AsINSTANCE = (AudioStreamInterface) RetrofitFactoryProducer.getFactory(baseURL)
                                .getClient().create(InterfaceSelector
                                .getInterfaceClass(strategy));
            }
            INSTANCE = AsINSTANCE;
        }
        else{
            //problem
        }
        return INSTANCE;
    }
}
