package wikiradio.neslihan.tur.org.wikiradio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import retrofit2.Call;
import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.CategoryListCallback;
import wikiradio.neslihan.tur.org.wikiradio.data.interfaces.MwAPIInterface;
import wikiradio.neslihan.tur.org.wikiradio.data.pojo.MwJsonObject;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheController;

/**
 * Created by nesli on 09.01.2017.
 */

public class SplashActivity extends Activity implements CategoryListCallback{
    private static String LOG_TAG = SplashActivity.class.getName();
    private HashSet<String> categorySet;
    private Toast toast;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        if(!sharedPref.contains("firstRun")){
            replaceToast("it is first run");
            categorySet = new HashSet<>();
            DataUtils.getCategoryList(categorySet,Constant.EMPTY_STRING,this);

        }else{
            replaceToast("not first run");
            Constant.categorySet = readFromFile(this);
            this.categorySet = Constant.categorySet;
            startOrganizerService();
            startApplication();
        }


        //DataUtils.getRandomSummary(this);

    }

    public void startApplication(){
        Intent intent = new Intent(SplashActivity.this, RadioActivity.class);
        intent.putExtra("category_set", categorySet);
        startActivity(intent);
        finish();
    }
    public void startOrganizerService(){
        Log.d(LOG_TAG,"service is started");
        Intent intent = new Intent(SplashActivity.this, CacheController.class);
        intent.putExtra("category_set", categorySet);
        this.startService(intent);
        //finish();
    }
    private void replaceToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    private void saveToFile(HashSet<String> categorySet, String path){
        try {
        BufferedWriter out = new BufferedWriter(new FileWriter(this.getFilesDir()+path));
        Iterator it = categorySet.iterator();
        while(it.hasNext()) {
            out.write(it.next()+"\n");
        }
        out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private HashSet<String> readFromFile(Context context) {
        replaceToast("reads from file");
        Scanner file = null;
        try {
            File f = new File(this.getFilesDir()+"categoryset.txt");
            //f.createNewFile(); //if file didnt exist, it will create. Otherwise does nothing
            file = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashSet<String> categorySet = new HashSet<>();
        // For each word in the input
        while (file.hasNext()) {
            categorySet.add(file.nextLine());
        }
        return categorySet;
    }

    @Override
    public void onSuccess(HashSet<String> categorySet) {
        this.categorySet = categorySet;
        Constant.categorySet = categorySet;
        saveToFile(categorySet, "categoryset.txt");
        startOrganizerService();
        startApplication();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("firstRun", false);
        editor.commit();
    }

    @Override
    public void onError(Class sender) {

    }
}
