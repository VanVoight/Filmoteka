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

public class MovieListFragment extends Fragment {

	private AppDatabase appDatabase;
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
	private long userId;
	private long userRoleId;

	public MovieListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
		Bundle bundle = getArguments();
		if (bundle != null) {
			userId = bundle.getLong("userId", 0);
			userRoleId = bundle.getLong("userRoleId",0);
		}
		appDatabase = AppDatabase.getInstance(requireContext());
		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		movieAdapter = new MovieAdapter(userId,appDatabase,userRoleId);
		recyclerView.setAdapter(movieAdapter);

		MovieList.setLanguageAndRegion(getContext());
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
		topRatedMovieAdapter = new MovieAdapter(userId,appDatabase,userRoleId);
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
		nowPlayingMovieAdapter = new MovieAdapter(userId,appDatabase,userRoleId);
		recyclerNowPlaying.setAdapter(nowPlayingMovieAdapter);
		MovieList.getReleasedMovies("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA",  new MovieList.OnMoviesFetchedListener() {
			@Override
			public void onMoviesFetched(List<Movie> movies) {
				nowPlayingMovieList = movies;
				nowPlayingMovieAdapter.setMovies(nowPlayingMovieList);
				nowPlayingMovieAdapter.notifyDataSetChanged();
			}
		});
		recyclerViewUpcoming = view.findViewById(R.id.recyclerViewUpcoming);
		recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		upcomingMovieAdapter = new MovieAdapter(userId,appDatabase,userRoleId);
		recyclerViewUpcoming.setAdapter(upcomingMovieAdapter);
		MovieList.getCustomReleaseMovies("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA",  new MovieList.OnMoviesFetchedListener() {
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
