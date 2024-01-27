package pl.edu.pb.filmoteka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import pl.edu.pb.filmoteka.DB.MyListMovies;


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

    private long userId;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        avatarImageView = view.findViewById(R.id.avatar_img);
        userNameTextView = view.findViewById(R.id.nameTextView);
        searchFilm = view.findViewById(R.id.inputFindFilm);
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
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        return view;
    }

}
