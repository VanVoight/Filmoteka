package pl.edu.pb.filmoteka;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentTransaction;


public class ProfileFragment extends Fragment   {

    private ImageView avatarImageView;
    private TextView userNameTextView;
    private EditText searchFilm;
    private Button favFilmsButton ;
    private Button seenFilmsButton ;
    private Button randFilmsButton ;
    private Button editProfileButton;
    private Button delProfileButton ;
    private String userName;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        avatarImageView = view.findViewById(R.id.avatar_img);
        userNameTextView = view.findViewById(R.id.nameTextView);
        searchFilm = view.findViewById(R.id.inputFindFilm);
        favFilmsButton = view.findViewById(R.id.fav_films_button);
        seenFilmsButton = view.findViewById(R.id.seen_films_button);
        randFilmsButton = view.findViewById(R.id.rand_films_button);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        delProfileButton = view.findViewById(R.id.del_profile_button);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userName = bundle.getString("userName", "");
            userNameTextView.setText(userName);
        }

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obsługa kliknięcia przycisku "Edytuj profil"
                openEditProfileFragment();
            }
        });

        return view;
    }


    private void openEditProfileFragment() {
        // Przygotuj dane do przekazania do EditFragmentProfile
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);

        // Tworzymy nowy fragment
        EditProfileFragment editFragment = new EditProfileFragment();
        editFragment.setArguments(bundle);

        // Otwieramy fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, editFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
