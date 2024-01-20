package pl.edu.pb.filmoteka;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class YourFusedLocationProviderClass {
    private FusedLocationProviderClient fusedLocationClient;

    public YourFusedLocationProviderClass(Context context) {
        // Inicjalizacja FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        // Pobieranie ostatniej znanej lokalizacji
        getLastKnownLocation();
    }

    private void getLastKnownLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                }
            }
        });
    }
}
