package pl.edu.pb.filmoteka;

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

import pl.edu.pb.filmoteka.DB.User;
import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.UserDao;
public class RegisterActivity extends AppCompatActivity {
	EditText username, password, repassword,name,surname,email;
	Button signup, signin;
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
				newUser.setUserRoleId(2);
				newUser.setUserName(enteredUsername);
				newUser.setPassword(enteredPassword);
				newUser.setLastName(enteredSurname);
				newUser.setFirstName(eneteredName);
				newUser.setEmail(enteredEmail);

				new InsertUserAsyncTask().execute(newUser);
			}
		});
		signin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view){
				Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
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
