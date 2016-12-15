package wikiradio.neslihan.tur.org.wikiradio.data.retrofit;

import wikiradio.neslihan.tur.org.wikiradio.Constant;

/**
 * Selects and creates corresponding factory by URL
 */

public class RetrofitFactoryProducer {
    public static AbstractRetrofitFactory getFactory(String baseURL){
        if(baseURL.equals(Constant.EN_WIKIPEDIA_BASE_URL)){
            return new EnWikipediaRetrofitFactory();
        }
        else if(baseURL.equals(Constant.COMMONS_BASE_URL)){
            return new CommonsRetrofitFactory();
        }else {
            return null;
        }
    }
}
