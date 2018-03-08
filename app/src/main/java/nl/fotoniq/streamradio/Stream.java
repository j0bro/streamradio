package nl.fotoniq.streamradio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;

import static android.content.Context.BIND_AUTO_CREATE;

class Stream {
    private static final String URL_NPO_RADIO2 = "http://icecast.omroep.nl/radio2-bb-mp3";

    private static final ServiceConnection SERVICE_CONNECTION = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            StreamService.LocalBinder localBinder = (StreamService.LocalBinder) binder;

            service = localBinder.getService();
            service.play(URL_NPO_RADIO2);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
        }
    };

    private static StreamService service;

    static void startBackgroundService(final @NonNull Context context) {
        final Intent i = new Intent(context, StreamService.class);

        context.bindService(i, SERVICE_CONNECTION, BIND_AUTO_CREATE);
    }

    static void togglePlayingAudio(final @NonNull Context context) {
        if (service == null) {
            startBackgroundService(context);

            return;
        }

        if (service.isPlayingAudio()) {
            service.stop();
        } else {
            service.play(URL_NPO_RADIO2);
        }
    }
}
