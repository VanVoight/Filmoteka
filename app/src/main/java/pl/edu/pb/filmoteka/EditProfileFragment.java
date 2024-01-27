package pl.edu.pb.filmoteka;

import static android.app.Activity.RESULT_OK;
import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.User;
import pl.edu.pb.filmoteka.DB.UserDao;

public class EditProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private Button saveChangesButton;
    private Button changeProfilePic;
    private User user;
    private AppDatabase appDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        saveChangesButton = view.findViewById(R.id.saveChangesButton);
        changeProfilePic = view.findViewById(R.id.zmien);
        appDatabase = AppDatabase.getInstance(requireContext());
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
        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        return view;
    }
    private void fetchUserData(long userId) {
        new FetchUserDataTask().execute(userId);
    }
    private class FetchUserDataTask extends AsyncTask<Long, Void, User> {
        @Override
        protected User doInBackground(Long... params) {
            long userId = params[0];
            return appDatabase.userDao().getUserById(userId);
        }

        @Override
        protected void onPostExecute(User result) {
            if (result != null) {
                user = result;
                updateUI();
            } else {
                Toast.makeText(requireContext(), "Nie udało się pobrać danych użytkownika", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUI() {
        if (user != null) {
            editTextFirstName.setText(user.firstName);
            editTextLastName.setText(user.lastName);
            editTextEmail.setText(user.getEmail());
        }
    }
    private void saveChanges() {
        if (user != null) {
            String newFirstName = editTextFirstName.getText().toString();
            String newLastName = editTextLastName.getText().toString();
            String newEmail = editTextEmail.getText().toString();

            new UpdateUserDetailsTask().execute(user.userId, newFirstName, newLastName, newEmail);
            Toast.makeText(requireContext(), "Zmiany zapisane", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Błąd podczas zapisywania zmian", Toast.LENGTH_SHORT).show();
        }
    }

    private class UpdateUserDetailsTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            long userId = (long) params[0];
            String newFirstName = (String) params[1];
            String newLastName = (String) params[2];
            String newEmail = (String) params[3];

            appDatabase.userDao().updateUserDetails(userId, newFirstName, newLastName, newEmail);

            return null;
        }
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (inputStream != null) {
                    inputStream.close();
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                updateProfileImage(byteArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProfileImage(byte[] byteArray) {
        if (user != null) {
            new UpdateProfileImageTask().execute(user.userId, byteArray);
            Toast.makeText(requireContext(), "Zaktualizowano zdjęcie profilowe", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Błąd podczas aktualizacji zdjęcia profilowego", Toast.LENGTH_SHORT).show();
        }
    }

    private class UpdateProfileImageTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            long userId = (long) params[0];
            byte[] byteArray = (byte[]) params[1];

            appDatabase.userDao().updateProfileImage(userId, byteArray);

            return null;
        }
    }


}