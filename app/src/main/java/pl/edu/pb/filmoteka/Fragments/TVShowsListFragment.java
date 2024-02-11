package pl.edu.pb.filmoteka.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.edu.pb.filmoteka.Adapters.MovieAdapter;
import pl.edu.pb.filmoteka.Adapters.TVShowAdapter;
import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.Models.Movie;
import pl.edu.pb.filmoteka.Models.TVShow;
import pl.edu.pb.filmoteka.MovieList;
import pl.edu.pb.filmoteka.R;

public class TVShowsListFragment extends Fragment {

	private AppDatabase appDatabase;
	private RecyclerView recyclerView;
	private RecyclerView topRatedRecyclerView;
	private RecyclerView recyclerNowPlaying;
	private RecyclerView recyclerViewUpcoming;
	private TVShowAdapter topRatedMovieAdapter;
	private TVShowAdapter nowPlayingMovieAdapter;
	private TVShowAdapter movieAdapter;
	private TVShowAdapter upcomingMovieAdapter;
	private List<TVShow> movieList;
	private List<TVShow> topRatedMovieList;
	private List<TVShow> nowPlayingMovieList;
	private List<TVShow> upcomingMovieList;
	private long userId;
	private TextView nowplaying;
	private long userRoleId;

	public TVShowsListFragment() {

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
		nowplaying = view.findViewById(R.id.textNowPlaying);
		appDatabase = AppDatabase.getInstance(requireContext());
		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		movieAdapter = new TVShowAdapter(userId,appDatabase,userRoleId);
		recyclerView.setAdapter(movieAdapter);
		nowplaying.setText(R.string.airing_today);
		MovieList.setLanguageAndRegion(getContext());
		MovieList.getPopularTVShows("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", new MovieList.OnTVShowsFetchedListener() {
			@Override
			public void onTVShowsFetched(List<TVShow> tvShows) {
				movieList = tvShows;
				movieAdapter.setMovies(movieList);
				movieAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(String errorMessage) {

			}
		});
		topRatedRecyclerView = view.findViewById(R.id.recyclerViewTopRated);
		topRatedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		topRatedMovieAdapter = new TVShowAdapter(userId,appDatabase,userRoleId);
		topRatedRecyclerView.setAdapter(topRatedMovieAdapter);


		MovieList.getTopRatedTVShows("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", new MovieList.OnTVShowsFetchedListener() {
			@Override
			public void onTVShowsFetched(List<TVShow> tvShows) {
				topRatedMovieList = tvShows;
				topRatedMovieAdapter.setMovies(topRatedMovieList);
				topRatedMovieAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(String errorMessage) {

			}

		});
		recyclerNowPlaying = view.findViewById(R.id.recyclerNowPlaying);
		recyclerNowPlaying.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		nowPlayingMovieAdapter = new TVShowAdapter(userId,appDatabase,userRoleId);
		recyclerNowPlaying.setAdapter(nowPlayingMovieAdapter);
		MovieList.getAiringTodayTVShows("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", new MovieList.OnTVShowsFetchedListener() {
			@Override
			public void onTVShowsFetched(List<TVShow> tvShows) {
				nowPlayingMovieList = tvShows;
				nowPlayingMovieAdapter.setMovies(nowPlayingMovieList);
				nowPlayingMovieAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(String errorMessage) {

			}
		});
		recyclerViewUpcoming = view.findViewById(R.id.recyclerViewUpcoming);
		recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		upcomingMovieAdapter = new TVShowAdapter(userId,appDatabase,userRoleId);
		recyclerViewUpcoming.setAdapter(upcomingMovieAdapter);
		MovieList.getOnTheAirTVShows("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", new MovieList.OnTVShowsFetchedListener() {
			@Override
			public void onTVShowsFetched(List<TVShow> tvShows) {
				upcomingMovieList = tvShows;
				upcomingMovieAdapter.setMovies(upcomingMovieList);
				upcomingMovieAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(String errorMessage) {

			}
		});
		return view;
	}
}
