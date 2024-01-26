package pl.edu.pb.filmoteka;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.MyListMovies;

public class MyListFragment extends Fragment {

    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    private long userId;
    private long userRoleId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_list, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getLong("userId", 0);
            userRoleId = bundle.getLong("userRoleId", 0);
        }
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        movieAdapter = new MovieAdapter(userId, appDatabase, userRoleId);
        recyclerView.setAdapter(movieAdapter);

        MovieList.setLanguageAndRegion(getContext());
        MovieList.getMyMovies(appDatabase, userId, "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", movies -> {
            movieList = movies;
            movieAdapter.setMovies(movieList);
            movieAdapter.notifyDataSetChanged();
        });
        return view;
    }
}