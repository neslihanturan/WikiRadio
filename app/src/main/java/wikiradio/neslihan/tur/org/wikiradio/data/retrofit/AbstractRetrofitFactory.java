package wikiradio.neslihan.tur.org.wikiradio.data.retrofit;

import retrofit2.Retrofit;

/**
 * AbstarctFactory pattern is imlpemented. getClient method will be filled different for diffrent baseURL's.
 */

public abstract class AbstractRetrofitFactory {
    abstract Retrofit getClient();
}
