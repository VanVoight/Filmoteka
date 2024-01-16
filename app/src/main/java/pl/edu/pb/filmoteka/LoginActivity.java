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

	private static final String TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_form);

		username = findViewById(R.id.username);
		password = findViewById(R.id.password);
		signin = findViewById(R.id.button_signin);
		signup = findViewById(R.id.button_signup);

		signin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String enteredUsername = username.getText().toString();
				String enteredPassword = password.getText().toString();

				Log.d(TAG, "onClick: Username: " + enteredUsername + ", Password: " + enteredPassword);

				if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
					Toast.makeText(LoginActivity.this, getString(R.string.toast_fields), Toast.LENGTH_SHORT).show();
				} else {
					// Utwórz instancję AsyncTask i uruchom operacje bazodanowe w osobnym wątku
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

	private class DatabaseAsyncTask extends AsyncTask<String, Void, User> {
		@Override
		protected User doInBackground(String... params) {
			String username = params[0];
			String password = params[1];

			// Operacje bazodanowe
			AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my-database").build();
			UserDao userDao = appDatabase.userDao();
			return userDao.getUserByUsernameAndPassword(username, password);
		}

		@Override
		protected void onPostExecute(User user) {
			if (user != null) {
				// Użytkownik istnieje - dodaj kod obsługi zalogowanego użytkownika
				// np. przechodzenie do nowego ekranu lub wyświetlanie komunikatu
				Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
			} else {
				// Użytkownik nie istnieje - wyświetl komunikat
				Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
