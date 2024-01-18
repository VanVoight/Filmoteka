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
	private RecyclerView topRatedRecyclerView;
	private RecyclerView recyclerNowPlaying;
	private RecyclerView recyclerViewUpcoming;
	private MovieAdapter topRatedMovieAdapter;
	private MovieAdapter nowPlayingMovieAdapter;
	private MovieAdapter movieAdapter;
	private MovieAdapter upcomingMovieAdapter;
	private List<Movie> movieList;
	private List<Movie> topRatedMovieList;
	private List<Movie> nowPlayingMovieList;
	private List<Movie> upcomingMovieList;

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
		topRatedRecyclerView = view.findViewById(R.id.recyclerViewTopRated);
		topRatedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		topRatedMovieAdapter = new MovieAdapter();
		topRatedRecyclerView.setAdapter(topRatedMovieAdapter);

		MovieList.getTopRatedMovies("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", new MovieList.OnMoviesFetchedListener() {
			@Override
			public void onMoviesFetched(List<Movie> movies) {
				topRatedMovieList = movies;
				topRatedMovieAdapter.setMovies(topRatedMovieList);
				topRatedMovieAdapter.notifyDataSetChanged();
			}
		});
		recyclerNowPlaying = view.findViewById(R.id.recyclerNowPlaying);
		recyclerNowPlaying.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		nowPlayingMovieAdapter = new MovieAdapter();
		recyclerNowPlaying.setAdapter(nowPlayingMovieAdapter);
		MovieList.getReleasedMovies("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", "2023-12-13", "2024-01-25", new MovieList.OnMoviesFetchedListener() {
			@Override
			public void onMoviesFetched(List<Movie> movies) {
				nowPlayingMovieList = movies;
				nowPlayingMovieAdapter.setMovies(nowPlayingMovieList);
				nowPlayingMovieAdapter.notifyDataSetChanged();
			}
		});
		recyclerViewUpcoming = view.findViewById(R.id.recyclerViewUpcoming);
		recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		upcomingMovieAdapter = new MovieAdapter();
		recyclerViewUpcoming.setAdapter(upcomingMovieAdapter);
		MovieList.getCustomReleaseMovies("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", "2024-02-24", "2024-03-30", new MovieList.OnMoviesFetchedListener() {
			@Override
			public void onMoviesFetched(List<Movie> movies) {
				upcomingMovieList = movies;
				upcomingMovieAdapter.setMovies(upcomingMovieList);
				upcomingMovieAdapter.notifyDataSetChanged();
			}
		});
		return view;
	}
}
