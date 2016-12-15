package wikiradio.neslihan.tur.org.wikiradio.data;

import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.CategoryListCallback;
import wikiradio.neslihan.tur.org.wikiradio.data.interfaces.MwAPIInterface;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonObject;
import wikiradio.neslihan.tur.org.wikiradio.data.retrofit.RetrofitServiceCache;

/**
 * Created by nesli on 15.12.2016.
 */

public class DataUtils {
    private static String LOG_TAG = DataUtils.class.getName();
    public static void getRandomCategory(final CategoryListCallback callback){
        Call<MwJsonObject> commonsQueryResponse = ((MwAPIInterface)RetrofitServiceCache.getService(Constant.COMMONS_BASE_URL,
                                                    Constant.MEDIA_WIKI_API))
                                                    .getRelevantCategories("");
        commonsQueryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                Log.d(LOG_TAG,"getRandomCategory method onResponse");
                ArrayList<String> categoryList = new ArrayList<>();
                for(String key: response.body().getQuery().getPages().keySet()){
                    Log.d("i","gson "+response.body().getQuery().getPages().get(key).getTitle());
                    categoryList.add(response.body().getQuery().getPages().get(key).getTitle());
                }
                Log.d(LOG_TAG,"getRandomCategory is finishing, RandomCategoryCallback is "+callback);
                callback.onSuccess(categoryList);
                Log.d(LOG_TAG,"getRandomCategory method is finished, CategoryListCallback.onSuccess method has been thrown");
            }

            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                Log.d(LOG_TAG,"getRandomCategory method is finished, CategoryListCallback.onFailure method has been thrown");
            }
        });
    }
}
