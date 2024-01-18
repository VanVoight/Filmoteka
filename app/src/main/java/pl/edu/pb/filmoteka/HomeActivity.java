package pl.edu.pb.filmoteka;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        displayMovieDetailsFragment();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_item_icon_view);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_category) {
                    // Obsługa kliknięcia na "Category"
                } else if (itemId == R.id.menu_home) {
                    // Obsługa kliknięcia na "Home"
                } else if (itemId == R.id.menu_profile) {
                    // Obsługa kliknięcia na "Profile"
                }
                return true;
            }
        });
    }
    private void displayMovieDetailsFragment() {
        // Create an instance of MovieDetailsFragment
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();

        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin the FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the fragment_container with MovieDetailsFragment
        fragmentTransaction.replace(R.id.fragment_container, movieDetailsFragment);

        // Commit the transaction
        fragmentTransaction.commit();
    }
}
