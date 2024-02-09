package pl.edu.pb.filmoteka.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.User;
import pl.edu.pb.filmoteka.DB.UserDao;
import pl.edu.pb.filmoteka.R;

public class LoginActivity extends AppCompatActivity {
	EditText username, password;
	Button signin, signup;
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";

	private long userId;
	private long roleId;
	private String savedUsername;
	private String savedPassword;
	private static final String TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.login_form);
		Configuration configuration = getResources().getConfiguration();

		if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
			// Bieżąca orientacja to pionowa
			setContentView(R.layout.login_form);
		} else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Bieżąca orientacja to pozioma
			setContentView(R.layout.login_form_land);
		}

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
				String enteredUsername = username.getText().toString().trim();
				String enteredPassword = password.getText().toString().trim();

				savedUsername = enteredUsername;
				savedPassword = enteredPassword;

				Log.d(TAG, "onClick: Username: " + enteredUsername + ", Password: " + enteredPassword);

				if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
					Toast.makeText(LoginActivity.this, getString(R.string.toast_fields), Toast.LENGTH_SHORT).show();
				} else {
					String hashedPassword = hashPassword(enteredPassword);
					new DatabaseAsyncTask().execute(enteredUsername, hashedPassword);
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
	private String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashedBytes = digest.digest(password.getBytes());


			StringBuilder builder = new StringBuilder();
			for (byte b : hashedBytes) {
				builder.append(String.format("%02x", b));
			}

			return builder.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Sprawdź, czy zmieniła się orientacja ekranu
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.e("LoginActivity-orientacja","ustawiam poziomą");
			// Zmiana na orientację poziomą
			setContentView(R.layout.login_form_land);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.e("LoginActivity-orientacja","ustawiam pionową");
			// Zmiana na orientację pionową
			setContentView(R.layout.login_form);
		}

		username = findViewById(R.id.username);
		password = findViewById(R.id.password);
		signin = findViewById(R.id.button_signin);
		signup = findViewById(R.id.button_signup);

		if (savedUsername != null && savedPassword != null) {
			Log.e("Dane","Ustawiam login spowrotem");
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
				userId = user.userId;
				roleId = user.userRoleId;
				saveLoginStatus(true);
				Toast.makeText(LoginActivity.this, succes_message, Toast.LENGTH_SHORT).show();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.d(TAG, "onPostExecute: Starting HomeActivity");
						Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
						startActivity(intent);
					}
				});
			} else {
				saveLoginStatus(false);
				Toast.makeText(LoginActivity.this, fail_message, Toast.LENGTH_SHORT).show();
				Log.d(TAG, "onPostExecute: Invalid username or password");
				Toast.makeText(LoginActivity.this, fail_message, Toast.LENGTH_SHORT).show();
			}
		}
		private void saveLoginStatus(boolean isLoggedIn) {
			SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("loggedInUsername", username.getText().toString().trim());
			editor.putLong("loggedInId",userId);
			editor.putLong("loggedInRoleId",roleId);
			editor.putBoolean("isLoggedIn", isLoggedIn);
			Log.d("puty w shared preferences","Username:" + username.getText().toString().trim()+" UserId:"+ userId + " roleID: "+ roleId);
			editor.apply();
		}
	}
}
