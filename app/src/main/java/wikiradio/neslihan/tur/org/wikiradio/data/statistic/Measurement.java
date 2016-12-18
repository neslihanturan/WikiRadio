package wikiradio.neslihan.tur.org.wikiradio.data.statistic;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wikiradio.neslihan.tur.org.wikiradio.RadioActivity;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AllAudioCompleted;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AllAudioInfo;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.CategoryListCallback;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;

/**
 * Created by nesli on 18.12.2016.
 */

public class Measurement implements AllAudioInfo,CategoryListCallback,AllAudioCompleted{
    private String LOG_TAG = Measurement.class.getName();
    private List<Double> sizeOfAudios = new ArrayList<>();
    private Set<String> selectedCategories = new HashSet<>();
    private int emptyCategoryCounter = 0;
    private int selectedAudioCounter = 0;

    public Measurement(){
        DataUtils.getRandomCategory(new ArrayList<String>(),"",this);
    }

    /**
     * onSuccess of AudioInfoCallbak and AllAudioInfo
     * @param audioFile selected audio file object
     * @param sender callback type class
     */
    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {
        if(sender == AllAudioInfo.class){
            Log.d(LOG_TAG,"All audio info onsuccess");
            if(audioFile!=null){
                sizeOfAudios.add(audioFile.getSize());
                selectedCategories.add(audioFile.getCategory());
                selectedAudioCounter++;

            }else{
                emptyCategoryCounter++;

            }
        }
    }

    /**
     * onSuccess of CategoryListCallback
     * @param categoryList all possible audio including categories
     */
    @Override
    public void onSuccess(ArrayList<String> categoryList) {
        Log.d(LOG_TAG,"onSuccess");
        for (String s : categoryList){
            Log.d(LOG_TAG,"all categories"+s);
        }
        Log.d(LOG_TAG,categoryList.size()+"");

        DataUtils.getAllAudioFiles(categoryList,this,this);
    }


    /**
     * onSuccess of AllAudioCompleted
     * @param sender callback type class
     */
    @Override
    public void onSuccess(Class sender) {
        Collections.sort(sizeOfAudios);
        Log.d(LOG_TAG,"report: \n" +
                "selected audio counter:" +selectedAudioCounter+"\n" +
                "size of sizeofaudios:" +sizeOfAudios.size()+"\n" +
                "percentile 80:"+sizeOfAudios.get((int)sizeOfAudios.size()/100 * 80)+"\n" +
                "percentile 95:"+sizeOfAudios.get((int)sizeOfAudios.size()/100 * 95));
        Log.d(LOG_TAG,"report: \n" +
                "empty category counter: "+emptyCategoryCounter+"\n" +
                "selected category counter "+selectedCategories.size()+"\n" +
                "aritmetic avg: "+ getAvg(sizeOfAudios, selectedAudioCounter));
    }

    /**
     * onError of AudioInfoCallbak and CategoryListCallback
     * @param sender Sender callback object to decide callback source
     */
    @Override
    public void onError(Class sender) {

    }

    private double getAvg(List<Double> list, int num){
        double total=0;
        for(Double d : list){
            total+=d;
        }
        return total/num;
    }
}
