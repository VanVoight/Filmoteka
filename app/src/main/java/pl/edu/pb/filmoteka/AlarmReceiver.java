package pl.edu.pb.filmoteka;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import pl.edu.pb.filmoteka.Activities.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

	private static final int NOTIFICATION_ID = 2;
	private static final String CHANNEL_ID = "MYCHANNEL";

	@Override
	public void onReceive(Context context, Intent intent) {
		showNotification(context);

	}

	void showNotification(Context context) {
		String CHANNEL_ID = "MYCHANNEL";// The id of the channel.
		CharSequence name = context.getResources().getString(R.string.app_name);// The user-visible name of the channel.
		NotificationCompat.Builder mBuilder;
		Intent notificationIntent = new Intent(context, MainActivity.class);
		Bundle bundle = new Bundle();
		notificationIntent.putExtras(bundle);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_IMMUTABLE);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		if (android.os.Build.VERSION.SDK_INT >= 26) {
			NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
			mNotificationManager.createNotificationChannel(mChannel);
			mBuilder = new NotificationCompat.Builder(context)
					.setSmallIcon(R.drawable.ic_launcher_foreground)
					.setLights(Color.RED, 300, 300)
					.setChannelId(CHANNEL_ID)
					.setContentTitle("FILMOTEKA");
		} else {
			mBuilder = new NotificationCompat.Builder(context)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setPriority(Notification.PRIORITY_HIGH)
					.setContentTitle("FILMOTEKA ZAPRASZA");
		}

		mBuilder.setContentIntent(contentIntent);
		mBuilder.setContentText("Wybierz film na wieczór razem ze mną");
		mBuilder.setAutoCancel(true);
		mNotificationManager.notify(1, mBuilder.build());
	}
}
