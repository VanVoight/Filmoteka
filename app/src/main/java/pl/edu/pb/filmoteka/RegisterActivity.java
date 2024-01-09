package pl.edu.pb.filmoteka;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {
	EditText username, password, repassword;
	Button signup, signin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_form);

		username = findViewById(R.id.username);
		password = findViewById(R.id.password);
		repassword = findViewById(R.id.repeat_password);
		signup = findViewById(R.id.button_signup);
		signin = findViewById(R.id.button_signin);

		signup.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){

			}
		});

		signin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view){

			}
		});
	}
}
