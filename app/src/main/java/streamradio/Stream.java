package streamradio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;

import static android.content.Context.BIND_AUTO_CREATE;

class Stream {
    private static final String URL_NPO_RADIO2 = "http://icecast.omroep.nl/radio2-bb-mp3";

    private static StreamService service;

    private static final ServiceConnection serviceConnection = new ServiceConnection() {
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

    static void togglePlayingAudio(final @NonNull Context context) {
        if (service == null) {
            context.bindService(new Intent(context, StreamService.class), serviceConnection, BIND_AUTO_CREATE);

            return;
        }

        if (service.isPlayingAudio()) {
            service.stop();
        } else {
            service.play(URL_NPO_RADIO2);
        }
    }
}
