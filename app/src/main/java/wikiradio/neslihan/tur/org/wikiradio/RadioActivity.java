package wikiradio.neslihan.tur.org.wikiradio;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.danikula.videocache.CacheListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import wikiradio.neslihan.tur.org.wikiradio.action.AudioSourceSelector;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerCallback;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.SingleMediaPlayer;
import wikiradio.neslihan.tur.org.wikiradio.notification.NotificationService;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallback;


public class RadioActivity extends AppCompatActivity implements MediaPlayerCallback, CacheListener {
    private String LOG_TAG = RadioActivity.class.getName();
    private FloatingActionButton playButton;
    private FloatingActionButton nextButton;
    private static SeekBar seekBar;
    private TextView textView;
    private int duration;
    private int amoungToupdate = -1;
    private Handler handler;
    private Runnable runnable;
    public static CacheControlCallback cacheControlCallback;
    public static CacheControlCallback ttsCacheControlCallback;
    private int prevPosition;
    //public static AudioFile nowPlayingAudio;
    public static Context context;

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
    }

    public void setListeners(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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
                    lock();
                    AudioSourceSelector.operate(Constant.ACTION.NEXT_ACTION,context);
                } catch (IOException e) {
                    e.printStackTrace();
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
        Constant.proxy.registerCacheListener((CacheListener) context, Constant.nowPlayingAudio.getUrl());
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
        if(Constant.nowPlayingAudio !=null)
            textView.setText("Audio Title: "+Constant.nowPlayingAudio.getTitle()+"\n Category:"+Constant.nowPlayingAudio.getCategory());
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
            cacheControlCallback.onCurrentFileCached();
        }
        seekBar.setSecondaryProgress(seekBar.getMax()*percentsAvailable/100);
    }
}
