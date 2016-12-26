package wikiradio.neslihan.tur.org.wikiradio;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerCallback;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.notification.NotificationService;

public class RadioActivity extends AppCompatActivity implements MediaPlayerCallback{
    private Button playButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        MediaPlayerController.delegate = this;
        startService(new Intent(RadioActivity.this, NotificationService.class));
        initViews();
        setListeners();

        /*try {
            MediaPlayerController.play("https://upload.wikimedia.org/wikipedia/commons/b/ba/Aboun.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public void initViews(){
        playButton = (Button) findViewById(R.id.playButton);
        nextButton = (Button)findViewById(R.id.nextButton);
    }
    public void setListeners(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MediaPlayerController.playOrPause("https://upload.wikimedia.org/wikipedia/commons/b/ba/Aboun.ogg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        nextButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MediaPlayerController.playOrPause("https://upload.wikimedia.org/wikipedia/commons/b/ba/Aboun.ogg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    public void onMediaPlayerPaused() {

    }

    @Override
    public void onMediaPlayerPlaying() {

    }
}
