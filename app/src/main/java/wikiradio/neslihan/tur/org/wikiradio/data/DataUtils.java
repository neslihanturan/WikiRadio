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
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonPrefixsearch;
import wikiradio.neslihan.tur.org.wikiradio.data.retrofit.RetrofitServiceCache;

/**
 * Created by nesli on 15.12.2016.
 */

public class DataUtils {
    private static String LOG_TAG = DataUtils.class.getName();
    public static void getRandomCategory(String psoffset, final CategoryListCallback callback){
        MwAPIInterface mwAPIInterface = ((MwAPIInterface)RetrofitServiceCache.getService(Constant.COMMONS_BASE_URL,
                                        Constant.MEDIA_WIKI_API));
        Call<MwJsonObject> commonsQueryResponse = mwAPIInterface
                                                    .getRelevantCategories(psoffset);

        Log.d(LOG_TAG,mwAPIInterface.toString());

        commonsQueryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                //if continue, recursive call with new psoffset
                if(response.body().getContinueField()!=null){
                    if(response.body().getContinueField().getPsoffset()!=null)
                    getRandomCategory(response.body().getContinueField().getPsoffset(),callback);
                    return;
                }
                Log.d(LOG_TAG,"getRandomCategory method onResponse");
                ArrayList<String> categoryList = new ArrayList<>();
                for(MwJsonPrefixsearch ps : response.body().getQuery().getPrefixsearch()){
                    categoryList.add(ps.getTitle());
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
    /*public static void getRandomCategory2(String gaccontinue, final CategoryListCallback callback){
        Call<MwJsonObject> commonsQueryResponse = ((MwAPIInterface)RetrofitServiceCache.getService(Constant.COMMONS_BASE_URL,
                Constant.MEDIA_WIKI_API))
                .getRelevantCategories2(gaccontinue);
        commonsQueryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                //if continue, recursive call with new psoffset
                if(response.body().getContinueField()!=null){
                    if(response.body().getContinueField().getGaccontinue()!=null)
                        getRandomCategory2(response.body().getContinueField().getGaccontinue(),callback);
                    return;
                }
                Log.d(LOG_TAG,"getRandomCategory2 method onResponse");
                ArrayList<String> categoryList = new ArrayList<>();
                for(String key: response.body().getQuery().getPages().keySet()){
                    //Log.d("i","gson "+response.body().getQuery().getPages().get(key).getTitle());
                    categoryList.add(response.body().getQuery().getPages().get(key).getTitle());
                }
                Log.d(LOG_TAG,"getRandomCategory2 is finishing, RandomCategoryCallback is "+callback);
                callback.onSuccess(categoryList);
                Log.d(LOG_TAG,"getRandomCategory2 method is finished, CategoryListCallback.onSuccess method has been thrown");
            }
            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                Log.d(LOG_TAG,"getRandomCategory2 method is finished, CategoryListCallback2.onFailure method has been thrown");
            }
        });
    }*/
}
