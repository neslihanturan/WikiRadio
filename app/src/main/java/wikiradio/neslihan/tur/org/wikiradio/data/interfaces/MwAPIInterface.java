package wikiradio.neslihan.tur.org.wikiradio.data.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonObject;

/**
 * Interface for retrofit service to get all possible audio included categories and random audio files from those categories.
 */

public interface MwAPIInterface{
    @Headers({                              //TODO: Add interceptor with okhttp3 for pass header to all requests
            "User-Agent: AudioStreamerAndroidApp/tur.neslihan@gmail.com"
    })
    //@GET("w/api.php?action=query&continue=gaccontinue||&generator=allcategories&gacprefix=Audio files&gacmin=1&gaclimit=500&&gacprop=size&format=json")
    @GET("w/api.php?action=query&list=prefixsearch&pssearch=Category:Audio%20files&pslimit=500&format=json")

    Call<MwJsonObject> getRelevantCategories(@Query("psoffset") String psoffset);

    @Headers({                              //TODO: Add interceptor with okhttp3 for pass header to all requests
            "User-Agent: AudioStreamerAndroidApp/tur.neslihan@gmail.com"
    })
    @GET("w/api.php?action=query&continue=gaccontinue||&generator=allcategories&gacprefix=Audio files&gacmin=1&gaclimit=500&&gacprop=size&format=json")
    //@GET("w/api.php?action=query&list=prefixsearch&pssearch=Category:Audio%20files&pslimit=500&format=json")

    Call<MwJsonObject> getRelevantCategories2(@Query("gaccontinue") String gaccontinue);


    @Headers({                              //TODO: Add interceptor with okhttp3 for pass header to all requests
            "User-Agent: AudioStreamerAndroidApp/tur.neslihan@gmail.com"
    })
    @GET("w/api.php?action=query&generator=categorymembers&gcmtype=file&prop=info|imageinfo&format=json&gcmlimit=100&iiprop=url|user|canonicaltitle|mime|mediatype")
    Call<MwJsonObject> getRandomAudio(@Query("gcmtitle") String categoryTitle);

}
