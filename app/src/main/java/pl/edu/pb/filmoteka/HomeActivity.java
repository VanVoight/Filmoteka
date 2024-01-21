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

public class HomeActivity extends AppCompatActivity implements CategoryFragment.OnCategoryClickListener {


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
                    addCategoryFragment();

                } else if (itemId == R.id.menu_home) {
                    addMovieListFragment();
                } else if (itemId == R.id.menu_profile) {
                    // Obsługa kliknięcia na "Profile"
                }
                return true;
            }
        });
    }
    @Override
    public void onCategoryClick(Category category) {
        addCategoryMovies(category.getId(),category.getName());
    }
    private void addMovieListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieListFragment movieListFragment = new MovieListFragment();
        fragmentTransaction.replace(R.id.fragment_container, movieListFragment);
        fragmentTransaction.commit();
    }
    private void addCategoryFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CategoryFragment categoryFragment = new CategoryFragment();
        fragmentTransaction.replace(R.id.fragment_container, categoryFragment);
        fragmentTransaction.commit();
    }
    private void addCategoryMovies(int categoryId, String name) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieListCategoryFragment movieListFragment = new MovieListCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("categoryId", categoryId);
        bundle.putString("categoryName", name);
        movieListFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, movieListFragment);
        fragmentTransaction.commit();
    }
}
