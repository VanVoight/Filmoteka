package pl.edu.pb.filmoteka.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.Fragments.CategoryFragment;
import pl.edu.pb.filmoteka.Fragments.MovieListCategoryFragment;
import pl.edu.pb.filmoteka.Fragments.MovieListFragment;
import pl.edu.pb.filmoteka.Fragments.ProfileFragment;
import pl.edu.pb.filmoteka.Fragments.TVShowsListFragment;
import pl.edu.pb.filmoteka.Models.Category;
import pl.edu.pb.filmoteka.Models.SharedViewModel;
import pl.edu.pb.filmoteka.R;

public class HomeActivity extends AppCompatActivity implements CategoryFragment.OnCategoryClickListener {
    private static final String KEY_CURRENT_INDEX = "currentIndex";

    private AppDatabase appDatabase;
    private SharedViewModel sharedViewModel;
    private String userName;
    private long userId;
    private long userRoleId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Switch mySwitch = findViewById(R.id.mySwitch);
        TextView option1Description = findViewById(R.id.option1_description);
        TextView option2Description = findViewById(R.id.option2_description);
        if (mySwitch.isChecked()) {
            option1Description.setVisibility(View.VISIBLE);
            option2Description.setVisibility(View.GONE);
        } else {
            option1Description.setVisibility(View.GONE);
            option2Description.setVisibility(View.VISIBLE);
        }
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    option1Description.setVisibility(View.VISIBLE);
                    option2Description.setVisibility(View.GONE);
                } else {

                    option1Description.setVisibility(View.GONE);
                    option2Description.setVisibility(View.VISIBLE);
                }
            }
        });
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my-database")
                .build();
        Intent intent = getIntent();
        userName = getLoggedInUsername();
        userRoleId = getLoggedInRoleId();
        userId = getLoggedInId();
        Log.d("puty w shared preferences","Username:" + userName+" UserId:"+ userId + " roleID: "+ userRoleId);

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
                    if (mySwitch.isChecked()) {

                        addMovieListFragment();
                    } else {

                        addTVShowListFragment();
                    }
                } else if (itemId == R.id.menu_profile) {
                    addProfileFragment();
                }
                return true;
            }
        });
    }
    private String getLoggedInUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("loggedInUsername", "");
    }
    private long getLoggedInId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getLong("loggedInId", 1);
    }
    private long getLoggedInRoleId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getLong("loggedInRoleId", 1);
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
        Bundle bundle = new Bundle();
        bundle.putLong("userId", userId);
        bundle.putLong("userRoleId",userRoleId);
        movieListFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, movieListFragment);
        fragmentTransaction.commit();
        sharedViewModel.setCurrentIndex(0);
    }
    private void addTVShowListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TVShowsListFragment tvShowListFragment = new TVShowsListFragment(); // Załóżmy, że masz fragment o nazwie TVShowListFragment
        Bundle bundle = new Bundle();
        bundle.putLong("userId", userId);
        bundle.putLong("userRoleId", userRoleId);
        tvShowListFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, tvShowListFragment);
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
        bundle.putLong("userRoleId",userRoleId);
        bundle.putLong("userId", userId);
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
        bundle.putLong("userId", userId);
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
