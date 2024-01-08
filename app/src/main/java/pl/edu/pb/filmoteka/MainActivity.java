package pl.edu.pb.filmoteka;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MyAsyncTask().execute();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, ApiResponse> {

        @Override
        protected ApiResponse doInBackground(Void... params) {
            try {
                OkHttpClient client = new OkHttpClient();

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