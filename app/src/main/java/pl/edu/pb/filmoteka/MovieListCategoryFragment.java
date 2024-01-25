package pl.edu.pb.filmoteka;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.edu.pb.filmoteka.DB.AppDatabase;

public class MovieListCategoryFragment extends Fragment {

	private RecyclerView recyclerView;
	private MovieAdapter movieAdapter;

	private AppDatabase appDatabase;
	private TextView genreName;
	private List<Movie> movieList;
	private long userId;

	private long userRoleId;
	private int categoryId;
	public MovieListCategoryFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		appDatabase = AppDatabase.getInstance(requireContext());
		View view = inflater.inflate(R.layout.category_list, container, false);
		genreName = view.findViewById(R.id.textView);
		Bundle bundle = getArguments();
		if (bundle != null) {
			categoryId = bundle.getInt("categoryId", 0);
			userId = bundle.getLong("userId",0);
			userRoleId = bundle.getLong("userRoleId",0);
			String categoryName = bundle.getString("categoryName", "");

			genreName.setText(categoryName);
		}
		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		movieAdapter = new MovieAdapter(userId,appDatabase,userRoleId);
		recyclerView.setAdapter(movieAdapter);


		MovieList.getMoviesByGenre("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA",categoryId, new MovieList.OnMoviesFetchedListener() {
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
