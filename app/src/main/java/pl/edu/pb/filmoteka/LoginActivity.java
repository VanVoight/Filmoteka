package pl.edu.pb.filmoteka;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import pl.edu.pb.filmoteka.DB.AppDatabase;

public class LoginActivity extends AppCompatActivity {
	EditText username, password;
	Button signin, signup;

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
				if(enteredUsername.isEmpty()||enteredPassword.isEmpty()){
					Toast.makeText(LoginActivity.this, "@string/toast_fields", Toast.LENGTH_SHORT).show();
				}
				else{
					AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
				}
			}
		});

		signup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
	}
}
