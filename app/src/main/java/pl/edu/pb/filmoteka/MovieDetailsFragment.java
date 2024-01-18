package pl.edu.pb.filmoteka;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetailsFragment extends Fragment {

	private ImageView moviePosterImageView;
	private View circle;
	private TextView titleTextView;
	private TextView releaseDateTextView;
	private TextView overviewTextView;
	private TextView voteAverageTextView;

	private int highVoteColor;
	private int mediumVoteColor;
	private int lowVoteColor;
	public MovieDetailsFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

		moviePosterImageView = view.findViewById(R.id.moviePosterImageView);
		circle = view.findViewById(R.id.circleView);
		titleTextView = view.findViewById(R.id.titleTextView);
		releaseDateTextView = view.findViewById(R.id.releaseDateTextView);
		overviewTextView = view.findViewById(R.id.overviewTextView);
		voteAverageTextView = view.findViewById(R.id.voteAverageTextView);
		highVoteColor = ContextCompat.getColor(requireContext(), R.color.high_vote_color);
		mediumVoteColor = ContextCompat.getColor(requireContext(), R.color.medium_vote_color);
		lowVoteColor = ContextCompat.getColor(requireContext(), R.color.low_vote_color);


		// Set up your data or perform any additional operations here
		new FetchMovieDetailsTask().execute();

		return view;
	}
	private class FetchMovieDetailsTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... voids) {
			// Perform the network request to get movie details from TMDB API
			String apiUrl = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";
			String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA";

			OkHttpClient client = new OkHttpClient.Builder()
					.addNetworkInterceptor(new StethoInterceptor())
					.build();

			Request request = new Request.Builder()
					.url(apiUrl)
					.header("Authorization", "Bearer " + accessToken)
					.header("accept", "application/json")
					.build();

			try {
				Response response = client.newCall(request).execute();
				if (response.isSuccessful()) {
					return response.body().string();
				} else {
					// Handle error
					Log.e("MovieDetailsFragment", "Error fetching movie details");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result != null) {
				// Parse the JSON response and update UI with movie details
				updateUIWithMovieDetails(result);
			}
		}
	}

	private void updateUIWithMovieDetails(String jsonResult) {

		Gson gson = new Gson();
		TypeToken<MovieResult> token = new TypeToken<MovieResult>() {};
		MovieResult movieResponse = gson.fromJson(jsonResult, token.getType());

		if (movieResponse != null && movieResponse.getResults() != null && !movieResponse.getResults().isEmpty()) {
			// Assuming you want details of the first movie in the list
			Movie movie = movieResponse.getResults().get(0);

			titleTextView.setText(movie.getTitle());
			releaseDateTextView.setText(movie.getReleaseDate());
			overviewTextView.setText(movie.getOverview());
			voteAverageTextView.setText(String.valueOf(movie.getVoteAverage()));

			int circleStrokeColor = getCircleStrokeColorBasedOnVoteAverage(movie.getVoteAverage());
			circle.getBackground().setColorFilter(circleStrokeColor, PorterDuff.Mode.SRC_ATOP);

			new LoadImageTask(moviePosterImageView).execute(movie.getPosterUrl());

		}
	}
	private int getCircleStrokeColorBasedOnVoteAverage(double voteAverage) {
		if (voteAverage >= 8.0) {
			return highVoteColor;
		} else if (voteAverage >= 6.0) {
			return mediumVoteColor;
		} else {
			return lowVoteColor;
		}
	}
	private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;

		LoadImageTask(ImageView imageView) {
			imageViewReference = new WeakReference<>(imageView);

		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			try {
				InputStream inputStream = new URL(urls[0]).openStream();
				return BitmapFactory.decodeStream(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);

			ImageView imageView = imageViewReference.get();


			if (imageView != null && bitmap != null) {

				imageView.setImageBitmap(bitmap);
			}
		}

	}

}
