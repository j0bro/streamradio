package streamradio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

import static android.media.AudioManager.STREAM_MUSIC;

public class StreamService extends Service {

    static final String ACTION_PLAY = "streamservice.PLAY";
    static final String STREAM_URL = "streamservice.URL";
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "streamService";
    private static final String CHANNEL_NAME = "StreamRadio";
    private MediaPlayer mediaPlayer;
    private IBinder binder = new LocalBinder();

    @Override
    public void onDestroy() {
        super.onDestroy();

        stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_PLAY.equals(intent.getAction())) {
            play(intent.getStringExtra(STREAM_URL));
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    void stop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopForeground(true);
        stopSelf();
    }

    void play(final String url) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();

            makePersistent();
        } catch (IOException ignored) {
        }
    }

    boolean isPlayingAudio() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    private void makePersistent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(notificationChannel);
            }

            final Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("").setContentText("").setSmallIcon(android.R.drawable.ic_media_play).build();

            startForeground(NOTIFICATION_ID, notification);
        }
    }

    class LocalBinder extends Binder {
        StreamService getService() {
            return StreamService.this;
        }
    }
}
