package pl.edu.pb.filmoteka.Activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.edu.pb.filmoteka.AlarmReceiver;
import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.Role;
import pl.edu.pb.filmoteka.Models.ApiResponse;
import pl.edu.pb.filmoteka.MovieList;
import pl.edu.pb.filmoteka.R;


public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "MYCHANNEL";
    private static final int REQUEST_LOCATION_PERMISSION = 10;
    public static String region;
    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private AppDatabase appDatabase;
    Button signin, signup;
    ImageView imgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (isUserLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        Configuration configuration = getResources().getConfiguration();

        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            setContentView(R.layout.activity_main);
        } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            setContentView(R.layout.activity_main_land);
        }

        signin = findViewById(R.id.btnsignin);
        signup = findViewById(R.id.btnsignup);
        imgv = findViewById(R.id.imageView3);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Log.e("MainA","Kliknięto zaloguj");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        Stetho.initializeWithDefaults(this);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my-database")
                .allowMainThreadQueries()
                .build();
        new MyAsyncTask().execute();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        createNotificationChannel();
        startAlarmBroadcastReceiver(this);


        getLocation();
    }
    private boolean isUserLoggedIn() {

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
    public static void startAlarmBroadcastReceiver(Context context) {
        Intent _intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, _intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 29);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("MainActivity-orientacja","ustawiam poziomą");

            setContentView(R.layout.activity_main_land);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("MainActivity-orientacja","ustawiam pionową");


            setContentView(R.layout.activity_main);
        }
        signin = findViewById(R.id.btnsignin);
        signup = findViewById(R.id.btnsignup);

       // signin.setEnabled(isButtonEnabled);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("MainA", "Kliknięto zaloguj");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("FILMOTEKA zaprasza")
                .setContentText("Wybierz coś do obejrzenia wieczorem")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }





    private class MyAsyncTask extends AsyncTask<Void, Void, ApiResponse> {

        @Override
        protected ApiResponse doInBackground(Void... params) {
            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .addNetworkInterceptor(new StethoInterceptor())
                        .build();

                Request request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/movie/popular?include_adult=false&include_video=false&language=en-us&sort_by=created_at.asc")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA")
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();

                // Sprawdź, czy odpowiedź zawiera oczekiwane dane
                if (response.isSuccessful()) {
                    return new ApiResponse(true, responseBody);
                } else {
                    return new ApiResponse(false, null, "HTTP Error: " + response.code());
                }
            } catch (Exception e) {
                Log.e("MainActivity", "Error during API call", e);
                return new ApiResponse(false, null, "Error: " + e.getMessage());
            }
        }
        private void addRolesToDatabase() {

            List<Role> roles = appDatabase.roleDao().getAllRoles();
            if (roles == null || roles.isEmpty()) {

                Role userRole = new Role(1,"User");
                Role recenzentRole = new Role(2,"Recenzent");

                appDatabase.roleDao().insertRole(userRole);
                appDatabase.roleDao().insertRole(recenzentRole);
            }
        }
        @Override
        protected void onPostExecute(ApiResponse result) {
            // Tutaj możesz obsłużyć wynik zapytania do API
            if (result != null) {
                if (result.isSuccess()) {
                    Log.d("MainActivity", "Response: " + result.getResponseData());
                    // Dodaj kod obsługi połączenia udanego
                } else {
                    Log.e("MainActivity", "Error: " + result.getErrorMessage());
                    // Dodaj kod obsługi błędu połączenia
                }
            }
        }
    }


    private void getLocation(){
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        !=PackageManager.PERMISSION_GRANTED){
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_PERMISSION);

        }else{
            Log.d("lokalizacja", "getLocation: permission granted");
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(
                    location -> {
                        if (location != null) {
                            lastLocation = location;
                            getAddressFromLocation(location);
                        } else {
                            Toast.makeText(this,R.string.no_location,Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1
            );
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                region = address.getCountryCode();
                MovieList.setRegion(region);
                Log.d("lokalizacja", "Region: " + region);
                Log.d("lokalizacja", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this,
                            R.string.localization_permission,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }




}