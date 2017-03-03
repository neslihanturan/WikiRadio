package wikiradio.neslihan.tur.org.wikiradio.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import wikiradio.neslihan.tur.org.wikiradio.Constant;
import wikiradio.neslihan.tur.org.wikiradio.R;

/**
 * Created by nesli on 26.12.2016.
 */

public class RadioNotification{
    private String LOG_TAG = RadioNotification.class.getName();
    private Context context;
    public int notificationID;
    public NotificationManager notificationManager;
    private Notification notification;

    public RadioNotification(Context context, String playingState){
        this.notificationID = Constant.NOTIFICATION_ID;
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        if(playingState.equals(Constant.ISPLAYING.PLAYING)){
            remoteViews.setImageViewResource(R.id.playButton, android.R.drawable.ic_media_pause);
        }else if(playingState.equals(Constant.ISPLAYING.PAUSED)){
            remoteViews.setImageViewResource(R.id.playButton, android.R.drawable.ic_media_play);
        }

        notification = builder.setContentTitle("Music Player")
                .setTicker("Music Player")
                .setContentText("Black in Black")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .build();

        notification.contentView = remoteViews;

        //set the button listeners
        setListeners(remoteViews);

        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        CharSequence contentTitle = "From Shortcuts";
        notificationManager.notify(notificationID, notification);
    }
    private void setListeners(RemoteViews remoteViews){

        if(Constant.nowPlayingAudio !=null){
            remoteViews.setTextViewText(R.id.textView, Constant.nowPlayingAudio.getTitle());
        }
        //Play intent
        Intent playIntent = new Intent();
        playIntent.setAction(Constant.ACTION.PLAY_ACTION);
        PendingIntent playPendingIntent =  PendingIntent.getBroadcast(context, 12345, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.playButton, playPendingIntent);

        //Next intent
        Intent nextIntent = new Intent();
        nextIntent.setAction(Constant.ACTION.NEXT_ACTION);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 12345, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.nextButton, nextPendingIntent);
    }

    public void updateNotification(String playingState){
        new RadioNotification(this.context, playingState);
    }
}
