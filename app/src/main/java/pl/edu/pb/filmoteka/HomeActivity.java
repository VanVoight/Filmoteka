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
        addMovieListFragment();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_item_icon_view);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_category) {

                } else if (itemId == R.id.menu_home) {
                    addMovieListFragment();
                } else if (itemId == R.id.menu_profile) {
                    // Obsługa kliknięcia na "Profile"
                }
                return true;
            }
        });
    }
    private void addMovieListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Tworzymy nową instancję MovieListFragment
        MovieListFragment movieListFragment = new MovieListFragment();

        // Dodajemy fragment do kontenera, na przykład do FrameLayout o identyfikatorze "container"
        fragmentTransaction.replace(R.id.fragment_container, movieListFragment);

        // Commitujemy transakcję
        fragmentTransaction.commit();
    }
}
