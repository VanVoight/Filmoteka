package pl.edu.pb.filmoteka;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovieListFragment extends Fragment {

	private RecyclerView recyclerView;
	private MovieAdapter movieAdapter;
	private List<Movie> movieList;

	public MovieListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		movieAdapter = new MovieAdapter();
		recyclerView.setAdapter(movieAdapter);


		MovieList.getPopularMovies("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", new MovieList.OnMoviesFetchedListener() {
			@Override
			public void onMoviesFetched(List<Movie> movies) {
				movieList = movies;
				movieAdapter.setMovies(movieList);
				movieAdapter.notifyDataSetChanged();
			}
		});

		return view;
	}
}
