package pl.edu.pb.filmoteka.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import pl.edu.pb.filmoteka.DB.User;
import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.UserDao;
import pl.edu.pb.filmoteka.R;

public class RegisterActivity extends AppCompatActivity {
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_REPASSWORD = "repassword";
	private static final String KEY_NAME = "name";
	private static final String KEY_SURNAME = "surname";
	private static final String KEY_EMAIL = "email";
	private static final int DEFAULT_PROFILE_IMAGE_RESOURCE = R.drawable.ic_profile;
	private static final String KEY_CHECKBOX_STATE = "checkbox_state";

	private String savedUsername;
	private String savedPassword;
	private String savedRepassword;
	private String savedName;
	private String savedSurname;
	private String savedEmail;
	private boolean isCheckBoxChecked;
	EditText username, password, repassword,name,surname,email;
	Button signup, signin;
	CheckBox checkBox;
	private AppDatabase appDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_form);
		Log.d("RegisterActivity", "onCreate method called");

		username = findViewById(R.id.username);
		password = findViewById(R.id.password);
		name = findViewById(R.id.name);
		surname = findViewById(R.id.surname);
		email = findViewById(R.id.email);
		repassword = findViewById(R.id.repeat_password);
		signup = findViewById(R.id.button_signup);
		signin = findViewById(R.id.button_signin);
		checkBox = findViewById(R.id.agree_terms_checkbox);
		checkBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				isCheckBoxChecked = !isCheckBoxChecked;
			}
		});
		if (savedInstanceState != null) {

			savedUsername = savedInstanceState.getString(KEY_USERNAME);
			savedPassword = savedInstanceState.getString(KEY_PASSWORD);
			savedRepassword = savedInstanceState.getString(KEY_REPASSWORD);
			savedName = savedInstanceState.getString(KEY_NAME);
			savedSurname = savedInstanceState.getString(KEY_SURNAME);
			savedEmail = savedInstanceState.getString(KEY_EMAIL);
			isCheckBoxChecked = savedInstanceState.getBoolean(KEY_CHECKBOX_STATE);

			username.setText(savedUsername);
			password.setText(savedPassword);
			repassword.setText(savedRepassword);
			name.setText(savedName);
			surname.setText(savedSurname);
			email.setText(savedEmail);

		}
		appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my-database")
				.build();
		signup.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				Log.d("RegisterActivity", "Zarejestruj się button clicked");
				String toastfieldsMessage = getResources().getString(R.string.toast_fields);
				String toastpasswordsMessage = getResources().getString(R.string.toast_passwords);

				String enteredUsername = username.getText().toString();
				String enteredPassword = password.getText().toString();
				String enteredRepassword = repassword.getText().toString();
				String eneteredName = name.getText().toString();
				String enteredSurname = surname.getText().toString();
				String enteredEmail = email.getText().toString();

				if(enteredUsername.isEmpty()||enteredEmail.isEmpty()||enteredPassword.isEmpty()||enteredRepassword.isEmpty()|| enteredSurname.isEmpty()||enteredEmail.isEmpty()||eneteredName.isEmpty()){
					Toast.makeText(RegisterActivity.this, toastfieldsMessage, Toast.LENGTH_SHORT).show();
				}
				if (!enteredPassword.equals(enteredRepassword)) {
					Toast.makeText(RegisterActivity.this, toastpasswordsMessage, Toast.LENGTH_SHORT).show();
					return;
				}
				User newUser = new User();
				newUser.setUserRoleId(isCheckBoxChecked ? 2 : 1);
				newUser.setUserName(enteredUsername);
				newUser.setPassword(enteredPassword);
				newUser.setLastName(enteredSurname);
				newUser.setFirstName(eneteredName);
				newUser.setEmail(enteredEmail);
				Bitmap defaultProfileBitmap = BitmapFactory.decodeResource(getResources(), DEFAULT_PROFILE_IMAGE_RESOURCE);
				byte[] defaultProfileImage = convertBitmapToByteArray(defaultProfileBitmap);
				newUser.setProfileImage(defaultProfileImage);

				Log.d("sprawdzamy role","RoleID:"+newUser.userRoleId);
				new InsertUserAsyncTask().execute(newUser);
			}
		});
		signin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view){
				Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_USERNAME, username.getText().toString());
		outState.putString(KEY_PASSWORD, password.getText().toString());
		outState.putString(KEY_REPASSWORD, repassword.getText().toString());
		outState.putString(KEY_NAME, name.getText().toString());
		outState.putString(KEY_SURNAME, surname.getText().toString());
		outState.putString(KEY_EMAIL, email.getText().toString());
		outState.putBoolean(KEY_CHECKBOX_STATE, isCheckBoxChecked);
	}
	private byte[] convertBitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}
	private class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
		@Override
		protected Void doInBackground(User... users) {
			Log.d("InsertUserAsyncTask", "doInBackground started");
			try {
				UserDao userDao = appDatabase.userDao();
				userDao.insert(users[0]); // Insert the user into the database
			} catch (Exception e) {
				Log.e("InsertUserAsyncTask", "Error inserting user", e);
			}
			Log.d("InsertUserAsyncTask", "doInBackground finished");
			return null;
		}
		@Override
		protected void onPostExecute(Void aVoid) {
			Log.d("InsertUserAsyncTask", "onPostExecute");
			String toastregisterMessage = getResources().getString(R.string.toast_register);

			Toast.makeText(RegisterActivity.this,toastregisterMessage, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
			startActivity(intent);
		}
	}
}
