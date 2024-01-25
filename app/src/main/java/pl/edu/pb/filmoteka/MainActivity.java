package pl.edu.pb.filmoteka;


import static android.provider.Settings.System.getString;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.Role;


public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";
    public static String region;
    private AppDatabase appDatabase;
    Button signin, signup;
    ImageView imgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin = findViewById(R.id.btnsignin);
        signup = findViewById(R.id.btnsignup);
        imgv = findViewById(R.id.imageView3);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        checkLocationPermission();
        setDailyNotificationAlarm();
    }


    private void setDailyNotificationAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
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
            // Sprawdź, czy role już istnieją w bazie danych
            List<Role> roles = appDatabase.roleDao().getAllRoles();
            if (roles == null || roles.isEmpty()) {
                // Dodaj dwie role: User i Recenzent
                Role userRole = new Role(1,"User");
                Role recenzentRole = new Role(2,"Recenzent");

                // Wstaw role do bazy danych
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

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    private void showPermissionToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeLocation();
            } else {
                String permissionMessage = getString(R.string.localization_permission);
                showPermissionToast(permissionMessage); }
        }
    }

    private void initializeLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    determineRegionFromLocation(latitude, longitude);

                }
            }
        });
    }

    private void determineRegionFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                String countryCode = address.getCountryCode();
                setRegionBasedOnCountryCode(countryCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void setRegionBasedOnCountryCode(String countryCode) {
        Log.d(TAG, "setting region ");
        if ("PL".equals(countryCode)) {
            region = getString(R.string.poland);
        } else {
            region = getString(R.string.US);
        }
    }

}