package wikiradio.neslihan.tur.org.wikiradio;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.SeekBar;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerCallback;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.SingleMediaPlayer;
import wikiradio.neslihan.tur.org.wikiradio.notification.NotificationService;
import wikiradio.neslihan.tur.org.wikiradio.proxy.App;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallback;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheController;
import wikiradio.neslihan.tur.org.wikiradio.ui.SeekBarController;

public class RadioActivity extends AppCompatActivity implements MediaPlayerCallback, CacheListener{
    private String LOG_TAG = RadioActivity.class.getName();
    private FloatingActionButton playButton;
    private FloatingActionButton nextButton;
    private SeekBar seekBar;
    private int duration;
    private int amoungToupdate = -1;
    private Handler handler;
    private Runnable runnable;
    public static CacheControlCallback cacheControlCallback;
    private int prevPosition;
    //StreamProxy streamProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        MediaPlayerController.delegate = this;
        startService(new Intent(RadioActivity.this, NotificationService.class));
        Log.d(LOG_TAG,"created on thread:"+Thread.currentThread());
        initViews();
        setListeners();

        /*streamProxy = new StreamProxy(this);
        streamProxy.start(this);
*/
        //ProxyCacheServer proxyCacheServer = new ProxyCacheServer();

        //final SeekBar mSeelBar = new SeekBar(this);

        /*try {
            MediaPlayerController.play("https://upload.wikimedia.org/wikipedia/commons/b/ba/Aboun.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public void initViews(){
        playButton = (FloatingActionButton) findViewById(R.id.playButton);
        nextButton = (FloatingActionButton)findViewById(R.id.nextButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
    }
    public void setListeners(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    playOrPause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        nextButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nextSong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run() {

                Log.d(LOG_TAG,"timer running");
                int targetPosition =  SingleMediaPlayer.getInstance().getCurrentPosition();
                //if (!(amoungToupdate * seekBar.getProgress() >= duration)) {

                        //int p = seekBar.getProgress();
                        //p += 1;
                /*while(prevPosition != targetPosition){
                    prevPosition++;
                    seekBar.setProgress(prevPosition);
                    Log.d(LOG_TAG, "Inner Handler, target pos:"+targetPosition+" prevpos:"+prevPosition);
                }*/

                seekBar.setProgress(targetPosition);

                //}

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




/*
        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.d(LOG_TAG,"timer running");
                        if (!(amoungToupdate * seekBar.getProgress() >= duration)) {
                            int p = seekBar.getProgress();
                            p += 1;
                            seekBar.setProgress(p);
                        }
                    }
                });
            }
        },amoungToupdate);*/
        //seekBar.setOnSeekBarChangeListener(new SeekBarListener());
        /*
        ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, "progress", SingleMediaPlayer.getInstance().getDuration());
        animation.setDuration(500); // 0.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        */
        /*
        handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                seekBar.setMax(SingleMediaPlayer.getInstance().getDuration());
                seekBar.setProgress(SingleMediaPlayer.getInstance().getCurrentPosition());
                handler.postDelayed(this, 200);

            }
        };
        handler.postDelayed(r, 200);
        */
    }
    private void lock(){
        playButton.setEnabled(false);
        nextButton.setEnabled(false);

    }
    private void unlock(){
        playButton.setEnabled(true);
        nextButton.setEnabled(true);

    }
    private void playOrPause() throws IOException {
        lock();
        if(CacheController.getCurrentURL()==null){
            nextSong();
        }else{
            MediaPlayerController.playOrPause(CacheController.getCurrentURL());
        }
    }
    private void playSong(String proxyURL) throws IOException {
        MediaPlayerController.play(proxyURL);
        App.getProxy(this).registerCacheListener(this, CacheController.getCurrentAudio().getUrl());
    }
    private void nextSong() throws IOException {
        lock();
        App.getProxy(this).unregisterCacheListener(this);
        if(CacheController.getCurrentURL()!=null){
            cacheControlCallback.onFileConsumed();
        }
        cacheControlCallback.onNextFileRequested();
        String newURL = CacheController.getCurrentURL();
        MediaPlayerController.changeSong(newURL);
        playSong(newURL);
        cacheControlCallback.onProcessCompleted();
    }

    /*private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        private int smoothnessFactor = 10;
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progress = Math.round(progress / smoothnessFactor);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            seekBar.setProgress(Math.round((seekBar.getProgress() + (smoothnessFactor / 2)) / smoothnessFactor) * smoothnessFactor);
        }
    }*/





    @Override
    public void onMediaPlayerPaused() {
        unlock();
        stopSeekBar();
    }

    @Override
    public void onMediaPlayerPlaying() {
        unlock();
        startSeekBar();
        //CacheController.getCurrentURL();

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
        seekBar.setSecondaryProgress(seekBar.getMax()*percentsAvailable/100);
    }
}
