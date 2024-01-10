package pl.edu.pb.filmoteka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.Role;


public class MainActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    Button signin, signup;
    ImageView imgv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin = findViewById(R.id.btnsignin);
        signup = findViewById(R.id.btnsignup);
        imgv = findViewById(R.id.imageView3);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my-database")
                .allowMainThreadQueries() // Uwaga: Ta opcja pozwala na wykonywanie operacji bazodanowych na wątku głównym, ale nie jest zalecana w produkcji.
                .build();
        new MyAsyncTask().execute();
    }


    private class MyAsyncTask extends AsyncTask<Void, Void, ApiResponse> {

        @Override
        protected ApiResponse doInBackground(Void... params) {
            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .addNetworkInterceptor(new StethoInterceptor())
                        .build();

                Request request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/account/20889746/favorite/movies?language=en-US&page=1&sort_by=created_at.asc")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA")
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();

                // Sprawdź, czy odpowiedź zawiera oczekiwane dane
                if (response.isSuccessful()) {
                    List<Role> roles = new ArrayList<>();
                    roles.add(new Role(1, "Administrator"));
                    roles.add(new Role(2, "Użytkownik"));
                    roles.add(new Role(3, "Recenzent"));
                    appDatabase.roleDao().insertRoles(roles);
                    return new ApiResponse(true, responseBody);
                } else {
                    return new ApiResponse(false, null, "HTTP Error: " + response.code());
                }
            } catch (Exception e) {
                Log.e("MainActivity", "Error during API call", e);
                return new ApiResponse(false, null, "Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(ApiResponse result) {
            // Tutaj możesz obsłużyć wynik zapytania do API
            if (result != null) {
                if (result.isSuccess()) {
                    Log.d("MainActivity", "Response: " + result.getResponseData());
                    // Dodaj kod obsługi połączenia udanego
                } else {
                    Log.e("MainActivity", "Error: " + result.getErrorMessage());
                    // Dodaj kod obsługi błędu połączenia
                }
            }
        }
    }
}