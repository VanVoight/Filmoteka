package pl.edu.pb.filmoteka;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Tutaj umieść kod do tworzenia powiadomienia
        showNotification(context, "Codzienne powiadomienie", "Zbliża się wieczór, spędź go przy swoim ulubionym filmie.");
        Log.d("ALERT", "setDailyNotificationAlarm: ");
    }

    private void showNotification(Context context, String title, String content) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Utwórz kanał powiadomień (wymagane dla Androida 8.0 i nowszych)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Utwórz intencję do otwarcia aktywności po kliknięciu w powiadomienie
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Utwórz obiekt powiadomienia
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);  // Aby powiadomienie zniknęło po kliknięciu

        // Wyślij powiadomienie
        notificationManager.notify(1, builder.build());
        Log.d("ALERT", "setDailyNotificationAlarm: ");
    }
}
