package pl.edu.pb.filmoteka.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.edu.pb.filmoteka.Adapters.MovieAdapter;
import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.Models.Movie;
import pl.edu.pb.filmoteka.MovieList;
import pl.edu.pb.filmoteka.R;

public class SearchFilmFragment extends Fragment {

    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    private long userId;
    private long userRoleId;
    private String searchQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_film, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getLong("userId", 0);
            userRoleId = bundle.getLong("userRoleId", 0);
            searchQuery = bundle.getString("search", "Lord of the rings");
        }
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.searchRecyclerView); // Update the ID as per your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        movieAdapter = new MovieAdapter(userId, appDatabase, userRoleId);
        recyclerView.setAdapter(movieAdapter);

        performSearch(appDatabase);

        return view;
    }

    // Method to perform search based on the query
    private void performSearch(AppDatabase appDatabase) {
        MovieList.setLanguageAndRegion(getContext());
        MovieList.searchMovies(appDatabase, searchQuery, "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", movies -> {
            movieList = movies;
            movieAdapter.setMovies(movieList);
            movieAdapter.notifyDataSetChanged();
        });
    }

    // Additional method to update search query and perform search
    public void updateSearchQuery(String newQuery) {
        searchQuery = newQuery;
        performSearch(AppDatabase.getInstance(requireContext()));
    }
}
