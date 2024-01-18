package pl.edu.pb.filmoteka;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieList {

	public interface OnMoviesFetchedListener {
		void onMoviesFetched(List<Movie> movies);
	}

	public static void getPopularMovies(String accessToken, OnMoviesFetchedListener listener) {
		new FetchMovieListTask(listener).execute(accessToken);
	}

	private static class FetchMovieListTask extends AsyncTask<String, Void, List<Movie>> {
		private final OnMoviesFetchedListener listener;

		FetchMovieListTask(OnMoviesFetchedListener listener) {
			this.listener = listener;
		}

		@Override
		protected List<Movie> doInBackground(String... tokens) {
			String accessToken = tokens[0];
			String apiUrl = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=pl&page=1&sort_by=popularity.desc";

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
					Gson gson = new Gson();
					TypeToken<MovieResult> token = new TypeToken<MovieResult>() {};
					MovieResult movieResponse = gson.fromJson(response.body().string(), token.getType());
					return movieResponse.getResults();
				} else {
					// Handle error
					Log.e("MovieList", "Error fetching movie list");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Movie> movies) {
			super.onPostExecute(movies);

			if (movies != null) {
				listener.onMoviesFetched(movies);
			}
		}

	}
	public static void getTopRatedMovies(String accessToken, OnMoviesFetchedListener listener) {
		new FetchTopRatedMovieListTask(listener).execute(accessToken);
	}
	private static class FetchTopRatedMovieListTask extends AsyncTask<String, Void, List<Movie>> {
		private final OnMoviesFetchedListener listener;

		FetchTopRatedMovieListTask(OnMoviesFetchedListener listener) {
			this.listener = listener;
		}

		@Override
		protected List<Movie> doInBackground(String... tokens) {
			String accessToken = tokens[0];
			String apiUrl = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=pl&page=1&sort_by=vote_average.desc&without_genres=99,10755&vote_count.gte=200";

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
					Gson gson = new Gson();
					TypeToken<MovieResult> token = new TypeToken<MovieResult>() {};
					MovieResult movieResponse = gson.fromJson(response.body().string(), token.getType());
					return movieResponse.getResults();
				} else {
					// Handle error
					Log.e("MovieList", "Error fetching top-rated movie list");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Movie> movies) {
			super.onPostExecute(movies);

			if (movies != null) {
				listener.onMoviesFetched(movies);
			}
		}
	}
	public static void getReleasedMovies(String accessToken, String minDate, String maxDate, OnMoviesFetchedListener listener) {
		new FetchReleasedMovieListTask(listener, minDate, maxDate).execute(accessToken);
	}

	private static class FetchReleasedMovieListTask extends AsyncTask<String, Void, List<Movie>> {
		private final OnMoviesFetchedListener listener;
		private final String minDate;
		private final String maxDate;

		FetchReleasedMovieListTask(OnMoviesFetchedListener listener, String minDate, String maxDate) {
			this.listener = listener;
			this.minDate = minDate;
			this.maxDate = maxDate;
		}

		@Override
		protected List<Movie> doInBackground(String... tokens) {
			String accessToken = tokens[0];
			String apiUrl = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=pl&page=1&sort_by=popularity.desc&with_release_type=2|3&release_date.gte=" + minDate + "&release_date.lte=" + maxDate;

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
					Gson gson = new Gson();
					TypeToken<MovieResult> token = new TypeToken<MovieResult>() {};
					MovieResult movieResponse = gson.fromJson(response.body().string(), token.getType());
					return movieResponse.getResults();
				} else {
					// Handle error
					Log.e("MovieList", "Error fetching released movie list");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Movie> movies) {
			super.onPostExecute(movies);

			if (movies != null) {
				listener.onMoviesFetched(movies);
			}
		}
	}
	public static void getCustomReleaseMovies(String accessToken, String minDate, String maxDate, OnMoviesFetchedListener listener) {
		new FetchCustomReleaseMovieListTask(listener, minDate, maxDate).execute(accessToken);
	}

	private static class FetchCustomReleaseMovieListTask extends AsyncTask<String, Void, List<Movie>> {
		private final OnMoviesFetchedListener listener;
		private final String minDate;
		private final String maxDate;

		FetchCustomReleaseMovieListTask(OnMoviesFetchedListener listener, String minDate, String maxDate) {
			this.listener = listener;
			this.minDate = minDate;
			this.maxDate = maxDate;
		}

		@Override
		protected List<Movie> doInBackground(String... tokens) {
			String accessToken = tokens[0];

			String apiUrl = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=pl&page=1&sort_by=popularity.desc&with_release_type=2|3&release_date.gte=" + minDate + "&release_date.lte=" + maxDate;

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
					Gson gson = new Gson();
					TypeToken<MovieResult> token = new TypeToken<MovieResult>() {};
					MovieResult movieResponse = gson.fromJson(response.body().string(), token.getType());
					return movieResponse.getResults();
				} else {
					// Handle error
					Log.e("MovieList", "Error fetching custom release movie list");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Movie> movies) {
			super.onPostExecute(movies);

			if (movies != null) {
				listener.onMoviesFetched(movies);
			}
		}
	}

}
