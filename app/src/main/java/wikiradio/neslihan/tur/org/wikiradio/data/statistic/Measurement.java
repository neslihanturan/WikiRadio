package wikiradio.neslihan.tur.org.wikiradio.data.statistic;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AllAudioCompleted;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.CategoryListCallback;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * This class is not used in project. Written to measure
 * -number of audio files
 * -number of invalid categories
 * -number of all categories
 * -arithmetic avg. of size of files
 * -80. percentile of all files
 * on server.
 */

public class Measurement implements CategoryListCallback,AllAudioCompleted, AudioInfoCallbak{
    private String LOG_TAG = Measurement.class.getName();
    private AllAudioCompleted callback;  //will be set on all categories checked
    private int numberOfAllCategories;
    private List<Integer> sizesOfAudios;  //List of size of audios will be used to find 80.percentile
    private HashMap<String,Integer> validCategories;  //Maps valid categories to number of audios in them
    private int invalidCategoryCounter = 0;  //counter for empty categories
    private int audioFileCounter = 0;  //counter for audio files
    private boolean end;
    private boolean detailledMode;

    public Measurement(){
        end = false;
        sizesOfAudios = new ArrayList<>();
        validCategories = new HashMap<>();
        callback = this;

        //prepareReport();
    }
    public void prepareReport(boolean detailledMode){
        this.detailledMode = detailledMode;
        Log.d(LOG_TAG,"Report is preparing, please wait");
        DataUtils.getCategoryList(new HashSet<String>(),"",this);
    }

    /**
     * onSuccess of AudioInfoCallbak
     * @param audioFile selected audio file object
     * @param sender callback type class
     */
    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {
        if(sender == AudioInfoCallbak.class){
            if(audioFile!=null){//if category is valid
                sizesOfAudios.add(audioFile.getSize());
                validCategories.put(audioFile.getCategory(),(validCategories.containsKey(audioFile.getCategory()) ? validCategories.get(audioFile.getCategory())+1:0));
                audioFileCounter++;
            }else{//null audio returned, means category is empty
                invalidCategoryCounter++;
            }
            if(invalidCategoryCounter +validCategories.size() >= numberOfAllCategories && !end){//TODO find a way to count last category too
                //means last category is checking
                end=!end;
                callback.onSuccess(AllAudioCompleted.class);
            }
            //Log.d(LOG_TAG,"valid categories:"+validCategories.size()+", invalid categories:"+invalidCategoryCounter+", total categories:"+numberOfAllCategories);
        }
    }

    /**
     * onSuccess of CategoryListCallback
     * @param categoryList all possible audio including categories
     */
    @Override
    public void onSuccess(HashSet<String> categoryList) {
        Log.d(LOG_TAG,"onSuccess");
        /*for (String s : categoryList){
            Log.d(LOG_TAG,"all categories"+s);
        }*/
        numberOfAllCategories = categoryList.size();
        Log.d(LOG_TAG,categoryList.size()+"");
        DataUtils.getAllAudioFiles(categoryList,this);
    }


    /**
     * onSuccess of AllAudioCompleted
     * @param sender callback type class
     */
    @Override
    public void onSuccess(Class sender) {
        Collections.sort(sizesOfAudios);
        Log.d(LOG_TAG,"report: \n" +
                "audio file counter:" +audioFileCounter+"\n" +
                "size of sizesOfAudios set:" + sizesOfAudios.size()+"\n" +
                "percentile 80:"+ sizesOfAudios.get((int) sizesOfAudios.size()/100 * 80)+"\n" +
                "percentile 95:"+ sizesOfAudios.get((int) sizesOfAudios.size()/100 * 95)+"\n" +
                "invalid category counter: "+ invalidCategoryCounter +"\n" +
                "valid category counter "+ validCategories.size()+"\n" +
                "aritmetic avg: "+ getAvg(sizesOfAudios, audioFileCounter));
        if(detailledMode){
            displayCategoryAudioMap();
        }
    }
    private void displayCategoryAudioMap(){
        for (String category: validCategories.keySet()){
            Log.d(LOG_TAG,"category:"+category+" has "+validCategories.get(category)+" number of audios");
        }
    }

    /**
     * onError of AudioInfoCallbak and CategoryListCallback
     * @param sender Sender callback object to decide callback source
     */
    @Override
    public void onError(Class sender) {

    }

    private double getAvg(List<Integer> list, int num){
        double total=0;
        for(Integer d : list){
            total+=d;
        }
        return total/num;
    }
}
