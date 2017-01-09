package wikiradio.neslihan.tur.org.wikiradio.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.CategoryListCallback;
import wikiradio.neslihan.tur.org.wikiradio.data.interfaces.MwAPIInterface;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonObject;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonPage;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonPrefixsearch;
import wikiradio.neslihan.tur.org.wikiradio.data.retrofit.RetrofitServiceCache;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 15.12.2016.
 */

public class DataUtils {


    private static String LOG_TAG = DataUtils.class.getName();

    public static void getCategoryList(final HashSet<String> previous, String psoffset, final CategoryListCallback callback){
        MwAPIInterface mwAPIInterface = ((MwAPIInterface)RetrofitServiceCache.getService(Constant.COMMONS_BASE_URL,
                                        Constant.MEDIA_WIKI_API));
        Call<MwJsonObject> queryResponse = mwAPIInterface
                                                    .getRelevantCategories(psoffset);

        Log.d(LOG_TAG,"getCategoryList APIİnterface"+mwAPIInterface.toString());

        queryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                //if continue, recursive call with new psoffset
                if(response.body().getContinueField()!=null){
                    if(response.body().getContinueField().getPsoffset()!=null){
                        for(MwJsonPrefixsearch ps : response.body().getQuery().getPrefixsearch()){
                            previous.add(ps.getTitle());
                        }
                        getCategoryList(previous,response.body().getContinueField().getPsoffset(),callback);
                        return;
                    }
                }
                Log.d(LOG_TAG,"getCategoryList method onResponse");
                //ArrayList<String> categoryList = new ArrayList<>();
                for(MwJsonPrefixsearch ps : response.body().getQuery().getPrefixsearch()){
                    previous.add(ps.getTitle());
                }
                Log.d(LOG_TAG,"getCategoryList is finishing, RandomCategoryCallback is "+callback);
                callback.onSuccess(previous);
                Log.d(LOG_TAG,"getCategoryList method is finished, CategoryListCallback.onSuccess method has been thrown");
            }
            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                Log.d(LOG_TAG,"getCategoryList method is finished, CategoryListCallback.onFailure method has been thrown");
                callback.onError(CategoryListCallback.class);
            }
        });
    }

    //TODO: add continuation example:Category:Audio files of cantillation
    public static void getRandomAudio(final Set<String> categorySet, final AudioInfoCallbak callback){
        //get random category title from all possible categories
        List<String> categoryList = new ArrayList<>();
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(categorySet.size());
        categoryList.addAll(categorySet);
        //categoryList.addAll(categoryList);  //to be able to get indexth element. It is not possible in set without iterating whole list
        final String categoryTitle = categoryList.get(index);
        MwAPIInterface mwAPIInterface = ((MwAPIInterface)RetrofitServiceCache.getService(Constant.COMMONS_BASE_URL,
                                            Constant.MEDIA_WIKI_API));
        Call<MwJsonObject> queryResponse = mwAPIInterface
                                            .getRandomAudio(categoryTitle);

        Log.d(LOG_TAG,"getCategoryList APIİnterface"+mwAPIInterface.toString());
        queryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                Log.d(LOG_TAG,"getRandomAudio method is on response");
                //case: category empty , this may happens when category is redirected
                if(response.body().getQuery()==null){
                    getRandomAudio(categorySet, callback); //recursive call to re-randomize
                    return;
                }
                else{
                    //to store selected audio
                    AudioFile audioFile = new AudioFile();
                    //random audio from category
                    Random generator = new Random();
                    //get request url, get gcmtitle parameter which is category title
                    String categoryTitle = call.request().url().queryParameter("gcmtitle");
                    Object[] values = response.body().getQuery().getPages().values().toArray();
                    Object randomValue = values[generator.nextInt(values.length)];

                    Log.d(LOG_TAG,"getRandomAudio selected image info: "+((MwJsonPage)randomValue).getImageinfo()[0].toString());
                    audioFile.setUrl(((MwJsonPage)randomValue).getImageinfo()[0].getUrl());
                    audioFile.setTitle(((MwJsonPage)randomValue).getImageinfo()[0].getCanonicaltitle());
                    audioFile.setCategory(categoryTitle);
                    Log.d(LOG_TAG,"getRandomAudio is finishing, RandomAudioCallback is:" + callback);
                    callback.onSuccess(audioFile, AudioInfoCallbak.class);    //true means valid category
                    Log.d(LOG_TAG,"getRandomAudio onSuccess has been thrown, method is finished");
                }
            }
            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                callback.onError(AudioInfoCallbak.class);
            }
        });
        Log.d(LOG_TAG,"getRandomAudio is returned");

    }

    //TODO: add continuation
    public static void getAllAudioFiles(final Set<String> categoryList, final AudioInfoCallbak callback) {
        MwAPIInterface mwAPIInterface = ((MwAPIInterface) RetrofitServiceCache.getService(Constant.COMMONS_BASE_URL,
                Constant.MEDIA_WIKI_API));
        for (final String categoryTitle : categoryList) {
            Log.d(LOG_TAG,"category list size:"+categoryList.size());
            Call<MwJsonObject> queryResponse = mwAPIInterface
                    .getRandomAudio(categoryTitle);
            queryResponse.enqueue(new Callback<MwJsonObject>() {
                @Override
                public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                    if (response.body().getQuery() == null) {
                        callback.onSuccess(null, AudioInfoCallbak.class);
                    } else {
                        Object[] values = response.body().getQuery().getPages().values().toArray();
                        //get request url, get gcmtitle parameter which is category title
                        String categoryTitle=call.request().url().queryParameter("gcmtitle");
                        for(Object obj: values){
                            //Store selected audio in an object
                            AudioFile audioFile = new AudioFile();
                            audioFile.setUrl(((MwJsonPage) obj).getImageinfo()[0].getUrl());
                            audioFile.setTitle(((MwJsonPage) obj).getImageinfo()[0].getCanonicaltitle());
                            audioFile.setCategory(categoryTitle);
                            audioFile.setSize(((MwJsonPage) obj).getImageinfo()[0].getSize());
                            callback.onSuccess(audioFile, AudioInfoCallbak.class);
                        }
                    }
                }
                @Override
                public void onFailure(Call<MwJsonObject> call, Throwable t) {
                    callback.onError(AudioInfoCallbak.class);
                }
            });
        }
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
