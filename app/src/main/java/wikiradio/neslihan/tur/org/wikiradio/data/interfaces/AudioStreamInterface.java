package wikiradio.neslihan.tur.org.wikiradio.data.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Interface for retrofit service to get AUDIO_STREAM of an audio in given url
 */

public interface AudioStreamInterface extends RetrofitMarkerInterface {
    //Base url will be ignored for this case
    @GET
    //Urls are determined in runtime so used @Url anotation
    Call<ResponseBody> getAudioStreams(@Url String url);
}
