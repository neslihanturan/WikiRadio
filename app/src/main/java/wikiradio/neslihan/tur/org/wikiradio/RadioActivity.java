package wikiradio.neslihan.tur.org.wikiradio;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danikula.videocache.CacheListener;

import java.io.File;
import java.io.IOException;

import wikiradio.neslihan.tur.org.wikiradio.action.AudioFileButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.action.AudioSourceSelector;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerCallback;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.SingleMediaPlayer;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.notification.NotificationService;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallback;
import wikiradio.neslihan.tur.org.wikiradio.ttscache.TTSCacheController;
import wikiradio.neslihan.tur.org.wikiradio.ttscache.TTSCacheStatusCallback;


public class RadioActivity extends AppCompatActivity implements MediaPlayerCallback, CacheListener,AudioInfoCallbak{
    private String LOG_TAG = RadioActivity.class.getName();
    private FloatingActionButton playButton;
    private FloatingActionButton nextButton;
    private static SeekBar seekBar;
    private TextView textView;
    private Button webButton;
    private int duration;
    private int amoungToupdate = -1;
    private Handler handler;
    private Runnable runnable;
    private static Toast toast;
    public static CacheControlCallback cacheControlCallback;
    public static CacheControlCallback ttsCacheControlCallback;
    private int prevPosition;
    //public static AudioFile nowPlayingAudio;
    public static Context context;
    private boolean isActionWaits;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            // do something here
            Intent intent = new Intent(this, UserSettingsActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        MediaPlayerController.delegateActivity = this;
        context = this;
        startService(new Intent(RadioActivity.this, NotificationService.class));
        Log.d(LOG_TAG,"created on thread:"+Thread.currentThread());
        initViews();
        setListeners();
    }

    public void initViews(){
        playButton = (FloatingActionButton) findViewById(R.id.playButton);
        nextButton = (FloatingActionButton)findViewById(R.id.nextButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.textView);
        webButton = (Button) findViewById(R.id.webButton);
    }

    public void setListeners(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(LOG_TAG,"playButton.OnClickListener");
                    lock();
                    AudioSourceSelector.operate(Constant.ACTION.PLAY_ACTION,context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        nextButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(LOG_TAG,"nextButton.OnClickListener");
                    lock();
                    AudioSourceSelector.operate(Constant.ACTION.NEXT_ACTION,context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.isAudioPlaying){
                    if(Constant.nowPlayingAudio!=null){
                        Log.d(LOG_TAG,Constant.nowPlayingAudio.getTitle());
                        Log.d(LOG_TAG,Constant.nowPlayingAudio.getPageUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.nowPlayingAudio.getPageUrl()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }



                }else{
                    if(Constant.nowPlayingFile!=null){
                        Log.d(LOG_TAG,Constant.nowPlayingFile.getTitle());
                        Log.d(LOG_TAG,Constant.nowPlayingFile.getPageUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.nowPlayingFile.getPageUrl()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    //webButton.setText("Dont click:)");
                }

            }
        });

        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run() {
                int targetPosition =  SingleMediaPlayer.getInstance().getCurrentPosition();
                seekBar.setProgress(targetPosition);
                prevPosition = targetPosition;
                if(amoungToupdate != Constant.SEEKBAR.STOP_SEEKBAR){
                    handler.postDelayed(this, amoungToupdate);
                }
                else{
                    handler.removeCallbacks(this);
                    Log.d("Runnable","ok");
                }

            }
        };

    }

    public static void unregisterCacheListener(){
        Constant.proxy.unregisterCacheListener((CacheListener) context);
    }
    public static void registerCacheListener(){
        Constant.proxy.registerCacheListener((CacheListener) context, Constant.nowPlayingAudio.getAudioUrl());
    }

    public static void setSecondarySeekbarMax(){
        seekBar.setSecondaryProgress(seekBar.getMax());
    }

    private void lock(){
        playButton.setEnabled(false);
        nextButton.setEnabled(false);

    }
    private void unlock(){
        playButton.setEnabled(true);
        nextButton.setEnabled(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TTSCacheController.destroyTTS();

    }

    @Override
    public void onMediaPlayerPaused() {
        unlock();
        updateText();
        setPausedView();
        stopSeekBar();
    }

    @Override
    public void onMediaPlayerPlaying() {
        unlock();
        updateText();
        setPlayingView();
        startSeekBar();
    }

    private void updateText(){
        /*String audioTitle = "";
        String fileTitle = "";
        String audioCategory = "";
*/
        if(Constant.isAudioPlaying){ //means audio file is playing
            textView.setText("Source: commons.wikimedia.org"+"\nAudio Title: "+Constant.nowPlayingAudio.getTitle()+"\n Category:"+Constant.nowPlayingAudio.getCategory());
        }else {
            textView.setText("Source: en.wikipedia.org"+"\nAudio Title: "+Constant.nowPlayingFile.getTitle());
        }

        /*if(Constant.nowPlayingAudio !=null){
            audioTitle = Constant.nowPlayingAudio.getTitle();
            audioCategory = Constant.nowPlayingAudio.getCategory();
        }
        if(Constant.nowPlayingFile != null){
            fileTitle = Constant.nowPlayingFile.getTitle();
        }
        textView.setText("Audio Title: "+audioTitle+"\n Category:"+audioCategory
                +"\n File Title:"+fileTitle);
*/
    }

    private void setPausedView(){
        playButton.setImageResource(android.R.drawable.ic_media_play);
    }

    private void setPlayingView(){
        playButton.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void stopSeekBar(){
        amoungToupdate = Constant.SEEKBAR.STOP_SEEKBAR;
    }
    private void startSeekBar(){
        duration = SingleMediaPlayer.getInstance().getDuration();
        seekBar.setMax(duration);
        Log.d(LOG_TAG,"onMediaPlayerPlaying");
        amoungToupdate = duration / 1000;
        Log.d(LOG_TAG,"duration: "+duration+"\n" +
                "amoungToupdate: "+amoungToupdate);
        handler.postDelayed(runnable, amoungToupdate);
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        Log.d(LOG_TAG, String.format("onCacheAvailable. percents: %d, file: %s", percentsAvailable, cacheFile));
        if(percentsAvailable==100){
            //cacheControlCallback.onCurrentFileCached();
        }
        seekBar.setSecondaryProgress(seekBar.getMax()*percentsAvailable/100);
    }

    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {
        AudioFileButtonListener.onSuccess(audioFile, this);
    }

    @Override
    public void onError(Class sender) {

    }

    public static void waitAnimation(){
        //replaceToast("Waiting");
    }

    public static void replaceToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }


}
