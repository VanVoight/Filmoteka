package pl.edu.pb.filmoteka;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.User;
import pl.edu.pb.filmoteka.DB.UserDao;

public class LoginActivity extends AppCompatActivity {
	EditText username, password;
	Button signin, signup;
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	private String savedUsername;
	private String savedPassword;
	private static final String TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_form);

		username = findViewById(R.id.username);
		password = findViewById(R.id.password);
		signin = findViewById(R.id.button_signin);
		signup = findViewById(R.id.button_signup);

		if (savedInstanceState != null) {
			savedUsername = savedInstanceState.getString(KEY_USERNAME);
			savedPassword = savedInstanceState.getString(KEY_PASSWORD);
			username.setText(savedUsername);
			password.setText(savedPassword);
		}
		signin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String enteredUsername = username.getText().toString();
				String enteredPassword = password.getText().toString();

				savedUsername = enteredUsername;
				savedPassword = enteredPassword;

				Log.d(TAG, "onClick: Username: " + enteredUsername + ", Password: " + enteredPassword);

				if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
					Toast.makeText(LoginActivity.this, getString(R.string.toast_fields), Toast.LENGTH_SHORT).show();
				} else {

					new DatabaseAsyncTask().execute(enteredUsername, enteredPassword);
				}
			}
		});

		signup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
	}
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(KEY_USERNAME, savedUsername);
		outState.putString(KEY_PASSWORD, savedPassword);
	}
	private class DatabaseAsyncTask extends AsyncTask<String, Void, User> {
		@Override
		protected User doInBackground(String... params) {
			String username = params[0];
			String password = params[1];

			// Operacje bazodanowe
			AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my-database")
					.build();
			UserDao userDao = appDatabase.userDao();
			return userDao.getUserByUsernameAndPassword(username, password);
		}

		@Override
		protected void onPostExecute(User user) {
			String succes_message = getResources().getString(R.string.login_succesful);
			String fail_message = getResources().getString(R.string.login_not_valid);
			Log.d(TAG, "onPostExecute: User: " + user);
			if (user != null) {

				Toast.makeText(LoginActivity.this, succes_message, Toast.LENGTH_SHORT).show();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.d(TAG, "onPostExecute: Starting HomeActivity");
						Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
						intent.putExtra("userId", user.userId);
						Log.d("Logowanie","ID TAKIE O MA BYĆ:"+user.userId);
						intent.putExtra("userName", username.getText().toString());
						startActivity(intent);
					}
				});
			} else {

				Toast.makeText(LoginActivity.this, fail_message, Toast.LENGTH_SHORT).show();
				Log.d(TAG, "onPostExecute: Invalid username or password");
				Toast.makeText(LoginActivity.this, fail_message, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
