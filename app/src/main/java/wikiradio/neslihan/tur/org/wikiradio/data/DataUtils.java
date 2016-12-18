package wikiradio.neslihan.tur.org.wikiradio.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AllAudioCompleted;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AllAudioInfo;
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

    public static void getRandomCategory(final ArrayList<String> previous, String psoffset, final CategoryListCallback callback){
        MwAPIInterface mwAPIInterface = ((MwAPIInterface)RetrofitServiceCache.getService(Constant.COMMONS_BASE_URL,
                                        Constant.MEDIA_WIKI_API));
        Call<MwJsonObject> queryResponse = mwAPIInterface
                                                    .getRelevantCategories(psoffset);

        Log.d(LOG_TAG,"getRandomCategory APIİnterface"+mwAPIInterface.toString());

        queryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                //if continue, recursive call with new psoffset
                if(response.body().getContinueField()!=null){
                    if(response.body().getContinueField().getPsoffset()!=null){
                        for(MwJsonPrefixsearch ps : response.body().getQuery().getPrefixsearch()){
                            previous.add(ps.getTitle());
                        }
                        getRandomCategory(previous,response.body().getContinueField().getPsoffset(),callback);
                        return;
                    }
                }
                Log.d(LOG_TAG,"getRandomCategory method onResponse");
                //ArrayList<String> categoryList = new ArrayList<>();
                for(MwJsonPrefixsearch ps : response.body().getQuery().getPrefixsearch()){
                    previous.add(ps.getTitle());
                }
                Log.d(LOG_TAG,"getRandomCategory is finishing, RandomCategoryCallback is "+callback);
                callback.onSuccess(previous);
                Log.d(LOG_TAG,"getRandomCategory method is finished, CategoryListCallback.onSuccess method has been thrown");
            }
            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                Log.d(LOG_TAG,"getRandomCategory method is finished, CategoryListCallback.onFailure method has been thrown");
                callback.onError(CategoryListCallback.class);
            }
        });
    }

    public static void getRandomAudio(final List<String> categoryList, final AudioInfoCallbak callback){
        //get random category title from all possible categories
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(categoryList.size());
        final String categoryTitle = categoryList.get(index);
        MwAPIInterface mwAPIInterface = ((MwAPIInterface)RetrofitServiceCache.getService(Constant.COMMONS_BASE_URL,
                                            Constant.MEDIA_WIKI_API));
        Call<MwJsonObject> queryResponse = mwAPIInterface
                                            .getRandomAudio(categoryTitle);

        Log.d(LOG_TAG,"getRandomCategory APIİnterface"+mwAPIInterface.toString());
        queryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                Log.d(LOG_TAG,"getRandomAudio method is on response");
                //case: category empty , this may happens when category is redirected
                if(response.body().getQuery()==null){
                    getRandomAudio(categoryList, callback); //recursive call to re-randomize
                    return;
                }
                else{
                    //to store selected audio
                    AudioFile audioFile = new AudioFile();
                    //random audio from category
                    Random generator = new Random();
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

    public static void getAllAudioFiles(final List<String> categoryList, final AllAudioInfo callback, final AllAudioCompleted allAudioCompleted) {
        final String lastCategoryTitle = categoryList.get(categoryList.size()-1);
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
                        //emptyCategories.add(categoryTitle);
                        callback.onSuccess(null, AllAudioInfo.class);
                        Log.d(LOG_TAG, "empty category occured" + categoryTitle);
                    } else {
                        Object[] values = response.body().getQuery().getPages().values().toArray();
                        for(Object obj: values){

                            //to store selected audio
                            AudioFile audioFile = new AudioFile();
                            //Log.d(LOG_TAG, "getAllAudioFiles selected image info: " + ((MwJsonPage) obj).getImageinfo()[0].toString());
                            audioFile.setUrl(((MwJsonPage) obj).getImageinfo()[0].getUrl());
                            audioFile.setTitle(((MwJsonPage) obj).getImageinfo()[0].getCanonicaltitle());
                            audioFile.setCategory(categoryTitle);
                            audioFile.setSize(((MwJsonPage) obj).getImageinfo()[0].getSize());
                            //Log.d(LOG_TAG, "getAllAudioFiles is finishing, RandomAudioCallback is:" + callback);
                            callback.onSuccess(audioFile, AllAudioInfo.class);    //true means valid category
                        }
                    }
                    if(categoryTitle.equals(lastCategoryTitle)){
                        allAudioCompleted.onSuccess(AllAudioCompleted.class);
                    }
                }

                @Override
                public void onFailure(Call<MwJsonObject> call, Throwable t) {
                    callback.onError(AllAudioInfo.class);
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
