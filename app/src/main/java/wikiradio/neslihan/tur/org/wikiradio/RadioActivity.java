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
import java.util.Random;

import wikiradio.neslihan.tur.org.wikiradio.data.DataUtils;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerCallback;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.SingleMediaPlayer;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.notification.MusicIntentReceiver;
import wikiradio.neslihan.tur.org.wikiradio.notification.NotificationService;
import wikiradio.neslihan.tur.org.wikiradio.proxy.App;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallback;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheController2;
import wikiradio.neslihan.tur.org.wikiradio.ttscache.TTSCacheController;

public class RadioActivity extends AppCompatActivity implements MediaPlayerCallback, CacheListener, AudioInfoCallbak {
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
    //public static AudioFile nowPlaying;
    public static Context context;
    private boolean isAudioFile; //true if last played audio is an audio file from commons, false if it is a TTS file
    //StreamProxy streamProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        MediaPlayerController.delegateActivity = this;
        //MediaPlayerController.delegateService =
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
                    ButtonListener.playOrPause(context);
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
                    ButtonListener.nextSong(context);
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
        Constant.proxy.registerCacheListener((CacheListener) context, Constant.nowPlaying.getUrl());
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
    private void playOrPause() throws IOException {
        lock();
        if(CacheController2.getCurrentURL()==null){
            nextSong();
        }else{
            MediaPlayerController.playOrPause(CacheController2.getCurrentURL());
        }
    }
    /*private void playSong(String proxyURL) throws IOException {

        MediaPlayerController.play(proxyURL);
        AudioFile audioFile = CacheController2.getCurrentAudio();
        textView.setText("Audio Title: "+audioFile.getTitle()+"\n Category:"+audioFile.getCategory());
        App.getProxy(this).registerCacheListener(this, audioFile.getUrl());
    }*/
    private void playSong(AudioFile audioFile) throws IOException {
        Log.d(LOG_TAG,"playSong method started");
        MediaPlayerController.play(audioFile.getProxyUrl());
        //nowPlaying = audioFile;

        //App.getProxy(this).registerCacheListener(this, audioFile.getProxyUrl());

    }
    private void playSong(FileDescriptor fileDescriptor) throws IOException {
        Log.d(LOG_TAG,"playSong method started");
        MediaPlayerController.play(fileDescriptor);
        //nowPlaying = audioFile;

        //App.getProxy(this).registerCacheListener(this, audioFile.getProxyUrl());

    }
    private void nextSong() throws IOException {
        Log.d(LOG_TAG,"next song requested");
        lock();
        App.getProxy(this).unregisterCacheListener(this);
        // there is a file that is played previously
        //TODO:
        if(getFromAudioCache()){
            isAudioFile= true;
            if(CacheController2.getCurrentAudio()!=null){
                Log.d(LOG_TAG,"current audio is null");
                cacheControlCallback.onFileConsumed();
            }
            Log.d(LOG_TAG,"call onNextFileRequested()");
            cacheControlCallback.onNextFileRequested();
            AudioFile newAudioFile = CacheController2.getCurrentAudio();
            if(newAudioFile==null){
                Log.d(LOG_TAG,"newAudioFile is null");
                DataUtils.getRandomAudio(Constant.categorySet,this);
            }else{
                Log.d(LOG_TAG,"newAudioFile is NOT null");
                newAudioFile.setProxyUrl(App.getProxy(this).getProxyUrl(newAudioFile.getUrl()));
                MediaPlayerController.changeSong(newAudioFile.getProxyUrl());
                seekBar.setSecondaryProgress(seekBar.getMax());
                playSong(newAudioFile);
            }
        }else{
            isAudioFile = false;
            if(TTSCacheController.getCurrentFile()!=null){
                Log.d(LOG_TAG,"current audio is null");
                ttsCacheControlCallback.onFileConsumed();
            }
            ttsCacheControlCallback.onNextFileRequested();
            FileDescriptor fileDescriptor = TTSCacheController.getCurrentFile();
            if(fileDescriptor==null){
                Log.d(LOG_TAG,"newAudioFile is null");
                DataUtils.getRandomAudio(Constant.categorySet,this);
            }else{
                MediaPlayerController.changeSong(fileDescriptor);
                seekBar.setSecondaryProgress(seekBar.getMax());
                playSong(fileDescriptor);
            }
        }

    }

    boolean getFromAudioCache(){
        return true;
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
        if(Constant.nowPlaying!=null)
            textView.setText("Audio Title: "+Constant.nowPlaying.getTitle()+"\n Category:"+Constant.nowPlaying.getCategory());
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

    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {
        ButtonListener.onSuccess(audioFile, this);
    }

    @Override
    public void onError(Class sender) {

    }
}
