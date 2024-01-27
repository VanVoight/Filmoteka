package pl.edu.pb.filmoteka.Fragments;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import pl.edu.pb.filmoteka.Models.Movie;
import pl.edu.pb.filmoteka.MovieList;
import pl.edu.pb.filmoteka.R;

public class MovieDetailsFragment extends Fragment {

	private ImageView moviePosterImageView;
	private View circle;
	private List<Movie> movieList;
	private TextView titleTextView;
	private TextView releaseDateTextView;

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

		voteAverageTextView = view.findViewById(R.id.voteAverageTextView);
		highVoteColor = ContextCompat.getColor(requireContext(), R.color.high_vote_color);
		mediumVoteColor = ContextCompat.getColor(requireContext(), R.color.medium_vote_color);
		lowVoteColor = ContextCompat.getColor(requireContext(), R.color.low_vote_color);


		MovieList.getPopularMovies("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", new MovieList.OnMoviesFetchedListener() {
			@Override
			public void onMoviesFetched(List<Movie> movies) {
				movieList = movies;
				updateUIWithMovieDetails();
			}
		});

		return view;
	}



	private void updateUIWithMovieDetails() {
		if (movieList != null && !movieList.isEmpty()) {
			Movie movie = movieList.get(0);

			titleTextView.setText(movie.getTitle());
			releaseDateTextView.setText(movie.getReleaseDate());

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
