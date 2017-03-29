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
import wikiradio.neslihan.tur.org.wikiradio.data.callback.SummaryCallback;
import wikiradio.neslihan.tur.org.wikiradio.data.interfaces.MwAPIInterface;
import wikiradio.neslihan.tur.org.wikiradio.data.interfaces.RestfulAPIInterface;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonObject;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonPage;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonPrefixsearch;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.RestfulJsonObject;
import wikiradio.neslihan.tur.org.wikiradio.data.retrofit.RetrofitServiceCache;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.model.TTSFile;

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

        Log.i(LOG_TAG,"getCategoryList APIİnterface"+mwAPIInterface.toString());

        queryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                //if continue, recursive call with new psoffset
                if(response.body().getContinueField()!=null){
                    if(response.body().getContinueField().getPsoffset()!=null){
                        for(MwJsonPrefixsearch ps : response.body().getQuery().getPrefixsearch()){
                            //if(ps.getTitle()!=null){
                                previous.add(ps.getTitle());
                            //}
                        }
                        getCategoryList(previous,response.body().getContinueField().getPsoffset(),callback);
                        return;
                    }
                }
                Log.i(LOG_TAG,"getCategoryList method onResponse");
                //ArrayList<String> categoryList = new ArrayList<>();
                for(MwJsonPrefixsearch ps : response.body().getQuery().getPrefixsearch()){
                    //if(ps.getTitle()!=null){
                        previous.add(ps.getTitle());
                    //}
                }
                Log.i(LOG_TAG,"getCategoryList is finishing, RandomCategoryCallback is "+callback);
                callback.onSuccess(previous);
                Log.i(LOG_TAG,"getCategoryList method is finished, CategoryListCallback.onSuccess method has been thrown");
            }
            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                Log.i(LOG_TAG,"getCategoryList method is finished, CategoryListCallback.onFailure method has been thrown");
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

        Log.i(LOG_TAG,"getRandomAudio APIİnterface"+mwAPIInterface.toString());
        Log.i(LOG_TAG,"getRandomAudio called from"+Thread.currentThread());
        queryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                Log.i(LOG_TAG,"getRandomAudio method is on response");
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

                    Log.i(LOG_TAG,"getRandomAudio selected image info: "+((MwJsonPage)randomValue).getImageinfo()[0].toString());
                    audioFile.setAudioUrl(((MwJsonPage)randomValue).getImageinfo()[0].getUrl());
                    audioFile.setTitle(((MwJsonPage)randomValue).getImageinfo()[0].getCanonicaltitle());
                    audioFile.setPageUrl("https://commons.wikimedia.org/wiki/"+audioFile.getTitle());
                    audioFile.setCategory(categoryTitle);
                    audioFile.setSize(((MwJsonPage)randomValue).getImageinfo()[0].getSize());
                    Log.i(LOG_TAG,"getRandomAudio is finishing, RandomAudioCallback is:" + callback);
                    callback.onSuccess(audioFile, AudioInfoCallbak.class);    //true means valid category
                    Log.i(LOG_TAG,"getRandomAudio onSuccess has been thrown, method is finished");
                }
            }
            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                t.printStackTrace();
                Log.i(LOG_TAG,"getRandomAudio is onFailure");
                callback.onError(AudioInfoCallbak.class);
            }
        });
        Log.i(LOG_TAG,"getRandomAudio is returned1");
    }

    //TODO: add continuation
    public static void getAllAudioFiles(final Set<String> categoryList, final AudioInfoCallbak callback) {
        MwAPIInterface mwAPIInterface = ((MwAPIInterface) RetrofitServiceCache.getService(Constant.COMMONS_BASE_URL,
                Constant.MEDIA_WIKI_API));
        for (final String categoryTitle : categoryList) {
            Log.i(LOG_TAG,"category list size:"+categoryList.size());
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
                            audioFile.setAudioUrl(((MwJsonPage) obj).getImageinfo()[0].getUrl());
                            audioFile.setTitle(((MwJsonPage) obj).getImageinfo()[0].getCanonicaltitle());
                            audioFile.setCategory(categoryTitle);
                            audioFile.setSize(((MwJsonPage) obj).getImageinfo()[0].getSize());
                            audioFile.setPageUrl("https://commons.wikimedia.org/wiki/"+audioFile.getTitle());
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

    public static void getRandomSummary(final SummaryCallback callback){
        Log.i(LOG_TAG,"getRandomSummary");
        RestfulAPIInterface restfulAPIInterface = ((RestfulAPIInterface) RetrofitServiceCache.getService(Constant.EN_WIKIPEDIA_BASE_URL,
                Constant.REST_API));
        Call<RestfulJsonObject> restfulJsonObjectCall = restfulAPIInterface
                .getRandomSummary();
        restfulJsonObjectCall.enqueue((new Callback<RestfulJsonObject>() {

            @Override
            public void onResponse(Call<RestfulJsonObject> call, Response<RestfulJsonObject> response) {
               /* WikipediaPageSummary pageSummary = new WikipediaPageSummary(
                        response.body().getThumbnail().getWidth(),      //TODO: sometimes there is no thumbnail and throws null point ex.
                        response.body().getThumbnail().getWidth(),
                        response.body().getThumbnail().getSource(),
                        response.body().getExtract(),
                        response.body().getTitle());*/

                TTSFile ttsFile = new TTSFile();
                ttsFile.setTitle(response.body().getTitle());
                ttsFile.setPageUrl("http://en.wikipedia.org/wiki/"+ttsFile.getTitle());
                Log.i(LOG_TAG,"page url of tts "+ttsFile.getPageUrl());
                ttsFile.setExtract(response.body().getExtract());
                ttsFile.setThumbnailSource(response.body().getThumbnail().getSource());
                ttsFile.setThumbnailWidth(response.body().getThumbnail().getWidth());
                ttsFile.setThumbnailHeight(response.body().getThumbnail().getHeight());

                Log.i(LOG_TAG,response.body().getTitle() + " url"+response.body().getExtract());
                callback.onSuccess(ttsFile);
            }

            @Override
            public void onFailure(Call<RestfulJsonObject> call, Throwable t) {
                Log.i(LOG_TAG,"couldnt get data");
                callback.onError();
            }
        }));

    }

}
