package pl.edu.pb.filmoteka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_form);

		username = findViewById(R.id.username);
		password = findViewById(R.id.password);
		name = findViewById(R.id.name);
		surname = findViewById(R.id.surname);
		email = findViewById(R.id.email);
		repassword = findViewById(R.id.repeat_password);
		signup = findViewById(R.id.button_signup);
		signin = findViewById(R.id.button_signin);

		signup.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				String enteredUsername = username.getText().toString();
				String enteredPassword = password.getText().toString();
				String enteredRepassword = repassword.getText().toString();
				String eneteredName = name.getText().toString();
				String enteredSurname = surname.getText().toString();
				String enteredEmail = email.getText().toString();
				if(enteredUsername.isEmpty()||enteredEmail.isEmpty()||enteredPassword.isEmpty()||enteredRepassword.isEmpty()|| enteredSurname.isEmpty()||enteredEmail.isEmpty()||eneteredName.isEmpty()){
					Toast.makeText(RegisterActivity.this, "@string/toast_fields", Toast.LENGTH_SHORT).show();
				}
				if (!enteredPassword.equals(enteredRepassword)) {
					Toast.makeText(RegisterActivity.this, "@string/toast_passwords", Toast.LENGTH_SHORT).show();
					return;
				}


				User newUser = new User();
				newUser.setUserName(enteredUsername);
				newUser.setPassword(enteredPassword);
				newUser.setLastName(enteredSurname);
				newUser.setFirstName(eneteredName);
				newUser.setEmail(enteredEmail);


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


		/*@Override
		protected void onPostExecute(Void aVoid) {

			Toast.makeText(context, "Pomyślnie zarejestrowano", Toast.LENGTH_SHORT).show();
		}*/
}
