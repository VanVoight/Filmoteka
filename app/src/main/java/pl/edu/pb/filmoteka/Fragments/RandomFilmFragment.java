package pl.edu.pb.filmoteka.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import pl.edu.pb.filmoteka.Adapters.MovieAdapter;
import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.Models.Movie;
import pl.edu.pb.filmoteka.MovieList;
import pl.edu.pb.filmoteka.R;

public class RandomFilmFragment extends Fragment {

    private TextView filmNameTextView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    private String accessToken="eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA";
    private long userId = 1;
    private long userRoleId = 1;
    private int randomMovieId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //sprawdzamy czy weszlismy do tego fragmentu
        Log.e("RandFragment", "We are in RandFragment");

       // View view = inflater.inflate(R.layout.fragment_random_film, container, false);
        View view = inflater.inflate(R.layout.fragment_random_film, container, false); // Domyślnie ustawiamy widok dla orientacji pionowej

        Configuration configuration = getResources().getConfiguration();

        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("Drawn","Ustawiam poziomy widok");
            view = inflater.inflate(R.layout.fragment_random_film_land, container, false); // Zmiana widoku dla orientacji poziomej
        }

        filmNameTextView = view.findViewById(R.id.textViewRandom);

        AppDatabase appDatabase = AppDatabase.getInstance(requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        movieAdapter = new MovieAdapter(userId, appDatabase, userRoleId);
        recyclerView.setAdapter(movieAdapter);

        randomMovieId = getRandomMovieId();

        MovieList.getRandomTopMovies(accessToken, 10, new MovieList.OnMoviesFetchedListener() {
            @Override
            public void onMoviesFetched(List<Movie> movies) {
                if (movies != null && !movies.isEmpty()) {
                   /* // Przetwórz pobrane filmy, np. wyświetl nazwę wylosowanego filmu
                    String randomMovieName = movies.get(0).getTitle();
                    filmNameTextView.setText(randomMovieName);*/
                    movieList = movies;
                    movieAdapter.setMovies(movieList);
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
   /* public void onStop() {
        super.onStop();
        CustomPopup.stopSensorsListening();
    }*/

    private int getRandomMovieId() {
        Random random = new Random();
        // Zakres identyfikatorów filmów dostępnych w bazie TMDB (możesz dostosować ten zakres do własnych potrzeb)
        int minMovieId = 3580;
        int maxMovieId = 1236146;
        return random.nextInt(maxMovieId - minMovieId + 1) + minMovieId;
    }
}