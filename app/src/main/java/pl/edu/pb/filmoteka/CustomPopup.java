package pl.edu.pb.filmoteka;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.os.Handler;
import android.os.Looper;
import android.hardware.SensorManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import pl.edu.pb.filmoteka.Activities.HomeActivity;
import pl.edu.pb.filmoteka.Fragments.RandomFilmFragment;

public class CustomPopup {

    private static SensorManager sensorManager;
    private static Sensor accelerometer;
    private static ShakeDetector shakeDetector;

    public static void showRandomFilmPopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);


        TextView textView = new TextView(context);
        textView.setText(context.getString(R.string.rand_film_popup));

        Resources resources = context.getResources();
        int color = resources.getColor(R.color.colorPrimary, null);

        textView.setTextSize(24);
        textView.setTextColor(color);
        textView.setGravity(Gravity.CENTER);

        linearLayout.addView(textView);

        Space space = new Space(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                50));
        linearLayout.addView(space);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.mobile_phone);
        int imageSize = dpToPx(context, 100);
        int padding = dpToPx(context, 16);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
        imageView.setLayoutParams(params);
        imageView.setPadding(padding, padding, padding, padding);

        linearLayout.addView(imageView);

        builder.setView(linearLayout);

        builder.setPositiveButton((context.getString(R.string.back)), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        Animation animation = new RotateAnimation(0, 30, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(500);

        imageView.startAnimation(animation);

        //wywołanie metody inicjalizacji sensorów
        initializeSensors(context);

        //obsługa potrząśnięcia
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                Log.d("CustomPopup", "Device shaken - opening RandomFilmFragment");

                goToRandomFilmFragment(context, dialog);
            }
        });

        dialog.show();
    }
    private static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    //funkcja inicjalizująca sensory
    private static void initializeSensors(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            shakeDetector = new ShakeDetector();

            if (accelerometer != null) {
                sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Log.e("CustomPopup", "Accelerometer not available");
            }
        } else {
            Log.e("CustomPopup", "SensorManager not available");
        }
    }

    private static void goToRandomFilmFragment(final Context context,  final AlertDialog dialog) {
        // Przeniesienie do nowego fragmentu po potrząśnięciu
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (context instanceof HomeActivity) {
                    // Tworzenie nowego fragmentu
                    Fragment randomFilmFragment = new RandomFilmFragment();

                    // Rozpoczęcie transakcji fragmentu
                    FragmentTransaction transaction = ((HomeActivity) context).getSupportFragmentManager().beginTransaction();

                    // Zamiana fragmentów
                    transaction.replace(R.id.fragment_container, randomFilmFragment);
                    transaction.addToBackStack(null);

                    // Zatwierdzenie transakcji
                    transaction.commit();
                    sensorManager.unregisterListener(shakeDetector);

                    Log.d("CustomPopup", "Navigating to RandomFilmFragment");

                    //Zamknięcie okeienka popup po otworzeniu fragmetnu z wylosowanym filmem
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } else {
                    Log.e("CustomPopup", "Context is not MainActivity");
                }
            }
        });
    }

}