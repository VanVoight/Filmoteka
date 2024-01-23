package pl.edu.pb.filmoteka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements CategoryFragment.OnCategoryClickListener {
    private static final String KEY_CURRENT_INDEX = "currentIndex";

    private SharedViewModel sharedViewModel;
    private String userName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        if(intent != null){
            userName = intent.getStringExtra("userName");
        }



        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        if (savedInstanceState == null) {

            addMovieListFragment();
        } else {

            int currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
            restoreFragment(currentIndex);
        }

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
                    addProfileFragment();
                }
                return true;
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_CURRENT_INDEX, sharedViewModel.getCurrentIndex());
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
        sharedViewModel.setCurrentIndex(0);
    }
    private void addCategoryFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CategoryFragment categoryFragment = new CategoryFragment();
        fragmentTransaction.replace(R.id.fragment_container, categoryFragment);
        fragmentTransaction.commit();
        sharedViewModel.setCurrentIndex(1);
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
        sharedViewModel.setCurrentIndex(2);
        sharedViewModel.setCurrentCategoryId(categoryId);
        sharedViewModel.setCurrentCategoryName(name);
    }
    private void addProfileFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        profileFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, profileFragment);
        fragmentTransaction.commit();
        sharedViewModel.setCurrentIndex(3);
    }
    private void restoreFragment(int index) {
        switch (index) {
            case 0:
                addMovieListFragment();
                break;
            case 1:
                addCategoryFragment();
                break;
            case 2:
                int categoryId = sharedViewModel.getCurrentCategoryId();
                String categoryName = sharedViewModel.getCurrentCategoryName();
                addCategoryMovies(categoryId, categoryName);
                break;
            case 3:
                addProfileFragment();
                break;


        }
    }
}
