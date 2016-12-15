package wikiradio.neslihan.tur.org.wikiradio.data.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.RestfulJsonObject;

/**
 * Interface for retrofit service to get summaries from wikipedia files randomly.
 */

public interface RestfulAPIInterface{
    @Headers({                              //TODO: Add interceptor with okhttp3 for pass header to all requests
            "User-Agent: AudioStreamerAndroidApp/tur.neslihan@gmail.com"
    })
    @GET("api/rest_v1/page/random/summary")
    Call<RestfulJsonObject> getRandomSummary();
}
