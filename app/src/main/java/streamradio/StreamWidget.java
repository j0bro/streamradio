package streamradio;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import static android.app.PendingIntent.getBroadcast;
import static streamradio.Stream.togglePlayingAudio;

public class StreamWidget extends AppWidgetProvider {

    static final String ACTION_CLICK = "streamwidget.CLICK";

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stream_widget);
        views.setOnClickPendingIntent(R.id.appwidget_text, getPendingSelfIntent(context, ACTION_CLICK));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        final Intent intent = new Intent(context, getClass());
        intent.setAction(action);

        return getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_CLICK.equals(intent.getAction())) {
            togglePlayingAudio(context);
        }
    }
}

