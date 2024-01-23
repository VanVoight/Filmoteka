package pl.edu.pb.filmoteka;
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

public class ProfileFragment extends Fragment   {

    private ImageView avatarImageView;
    private TextView userNames;
    private EditText searchFilm;
    private Button favFilmsButton ;
    private Button seenFilmsButton ;
    private Button randFilmsButton ;
    private Button editProfileButton;
    private Button delProfileButton ;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        avatarImageView = view.findViewById(R.id.avatar_img);
        userNames = view.findViewById(R.id.nameTextView);
        searchFilm = view.findViewById(R.id.inputFindFilm);
        favFilmsButton = view.findViewById(R.id.fav_films_button);
        seenFilmsButton = view.findViewById(R.id.seen_films_button);
        randFilmsButton = view.findViewById(R.id.rand_films_button);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        delProfileButton = view.findViewById(R.id.del_profile_button);

        return view;
    }
}
