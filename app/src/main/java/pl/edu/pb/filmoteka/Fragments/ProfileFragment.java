package pl.edu.pb.filmoteka.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import pl.edu.pb.filmoteka.CustomPopup;
import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.User;
import pl.edu.pb.filmoteka.Activities.LoginActivity;
import pl.edu.pb.filmoteka.R;


public class ProfileFragment extends Fragment {

    private ImageView avatarImageView;
    private TextView userNameTextView;
    private EditText searchFilm;
    private Button favFilmsButton;
    private Button seenFilmsButton;
    private Button toSeeFilmsButton;
    private Button randFilmsButton;
    private Button editProfileButton;
    private Button delProfileButton;
    private String userName;
    private AppDatabase appDatabase;
    private long userId;
    private long userRoleId;
    private SearchFilmFragment searchFilmFragment;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        appDatabase = AppDatabase.getInstance(requireContext());
        avatarImageView = view.findViewById(R.id.avatar_img);
        userNameTextView = view.findViewById(R.id.nameTextView);
        searchFilm = view.findViewById(R.id.inputSearchFilm);
        favFilmsButton = view.findViewById(R.id.fav_films_button);
        seenFilmsButton = view.findViewById(R.id.seen_films_button);
        toSeeFilmsButton = view.findViewById(R.id.to_see_films_button);
        randFilmsButton = view.findViewById(R.id.rand_films_button);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        delProfileButton = view.findViewById(R.id.del_profile_button);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userName = bundle.getString("userName", "");
            userNameTextView.setText(userName);
            userId = bundle.getLong("userId");
        }
        searchFilm = view.findViewById(R.id.inputSearchFilm);
        Button searchButton = view.findViewById(R.id.searchButton); // Assuming you have a search button

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchFilmFragment != null) {
                    searchFilmFragment.updateSearchQuery(searchFilm.getText().toString());
                }

                // Create or replace the SearchFilmFragment when search button is clicked
                if (searchFilmFragment == null) {
                    searchFilmFragment = new SearchFilmFragment();
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId", userId);
                    bundle.putLong("userRoleId", userRoleId);
                    bundle.putString("search", searchFilm.getText().toString());
                    searchFilmFragment.setArguments(bundle);
                }

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, searchFilmFragment); // Update the ID as per your layout
                fragmentTransaction.commit();
            }
        });

        randFilmsButton = view.findViewById(R.id.rand_films_button);

        favFilmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FavouriteListFragment favouriteListFragment = new FavouriteListFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("userId", userId);
                favouriteListFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, favouriteListFragment);
                fragmentTransaction.commit();
            }
        });
        seenFilmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                WatchedListFragment watchedListFragment = new WatchedListFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("userId", userId);
                watchedListFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, watchedListFragment);
                fragmentTransaction.commit();
            }
        });
        toSeeFilmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MyListFragment myListMoviesFragment = new MyListFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("userId", userId);
                myListMoviesFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, myListMoviesFragment);
                fragmentTransaction.commit();
            }
        });


        randFilmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomPopup.showRandomFilmPopup(requireContext());
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("userId", userId);
                editProfileFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, editProfileFragment);
                fragmentTransaction.commit();
            }
        });
        delProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLoggedInUser();
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        new LoadProfilePictureTask().execute(userId);
        return view;
    }
    private void clearLoggedInUser() {

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    private class LoadProfilePictureTask extends AsyncTask<Long, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Long... params) {
            User user = appDatabase.userDao().getUserById(params[0]);

            if (user != null && user.getProfileImage() != null) {
                return BitmapFactory.decodeByteArray(user.getProfileImage(), 0, user.getProfileImage().length);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap profileBitmap) {
            super.onPostExecute(profileBitmap);

            if (profileBitmap != null) {

                avatarImageView.setImageBitmap(getCircularBitmap(profileBitmap));
            } else {

                avatarImageView.setImageResource(R.drawable.ic_profile);
            }
        }
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);


        canvas.drawCircle(width / 2, height / 2, width / 2, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return outputBitmap;
    }



}
