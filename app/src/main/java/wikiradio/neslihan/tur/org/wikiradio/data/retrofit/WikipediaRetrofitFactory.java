package wikiradio.neslihan.tur.org.wikiradio.data.retrofit;

import retrofit2.Retrofit;

/**
 * Get retrofit object with base ur: https://en.wikipedia.org/
 */

public class WikipediaRetrofitFactory extends AbstractRetrofitFactory {
    private final String BASE_URL = "https://en.wikipedia.org/";
    @Override
    Retrofit getClient() {
        return GenericRetrofitFactory.newInstance(BASE_URL);
    }
}
