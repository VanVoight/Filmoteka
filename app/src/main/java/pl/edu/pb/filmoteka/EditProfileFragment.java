package pl.edu.pb.filmoteka;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import pl.edu.pb.filmoteka.DB.User;
import pl.edu.pb.filmoteka.DB.UserDao;

public class EditProfileFragment extends Fragment {
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private Button saveChangesButton;
    private User user;
    private UserDao userDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        saveChangesButton = view.findViewById(R.id.saveChangesButton);

        Bundle bundle = getArguments();
        if (bundle != null) {
            long userId = bundle.getLong("userId", -1);
            fetchUserData(userId);
        }
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        return view;
    }

    private void fetchUserData(long userId) {
        user = userDao.getUserById(userId);

        if (user != null) {
            editTextFirstName.setText(user.getUserFirstName());
            editTextLastName.setText(user.getUserLastName());
            editTextEmail.setText(user.getEmail());
        }
    }

    private void saveChanges() {
        user.setFirstName(editTextFirstName.getText().toString());
        user.setLastName(editTextLastName.getText().toString());
        user.setEmail(editTextEmail.getText().toString());

        userDao.updateUser(user); // Aktualizacja danych w bazie danych

        Toast.makeText(getActivity(), "Zapisano zmiany", Toast.LENGTH_SHORT).show();


        getActivity().getSupportFragmentManager().popBackStack();
    }
}