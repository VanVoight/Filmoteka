package pl.edu.pb.filmoteka;

import android.content.Context;
import android.content.res.Resources;
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
	private static String language;
	private static String region;
	public static void setLanguageAndRegion(Context context) {
		Resources resources = context.getResources();
		language = resources.getString(R.string.poland);
		region = resources.getString(R.string.poland);
	}
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
			String apiUrl = "https://api.themoviedb.org/3/movie/popular?include_adult=false&include_video=false&language="+language+"&page=1&region="+region+"&sort_by=popularity.desc";

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
			String apiUrl = "https://api.themoviedb.org/3/movie/top_rated?include_adult=false&include_video=false&language="+language+"&page=1&region="+region+"&sort_by=vote_average.desc&without_genres=99,10755&vote_count.gte=200";

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
	public static void getReleasedMovies(String accessToken, OnMoviesFetchedListener listener) {
		new FetchReleasedMovieListTask(listener).execute(accessToken);
	}

	private static class FetchReleasedMovieListTask extends AsyncTask<String, Void, List<Movie>> {
		private final OnMoviesFetchedListener listener;


		FetchReleasedMovieListTask(OnMoviesFetchedListener listener) {
			this.listener = listener;

		}

		@Override
		protected List<Movie> doInBackground(String... tokens) {
			String accessToken = tokens[0];
			String apiUrl = "https://api.themoviedb.org/3/movie/now_playing?include_adult=false&include_video=false&language="+language+"&page=1&region="+region+"&sort_by=popularity.desc";

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
	public static void getCustomReleaseMovies(String accessToken, OnMoviesFetchedListener listener) {
		new FetchCustomReleaseMovieListTask(listener).execute(accessToken);
	}

	private static class FetchCustomReleaseMovieListTask extends AsyncTask<String, Void, List<Movie>> {
		private final OnMoviesFetchedListener listener;


		FetchCustomReleaseMovieListTask(OnMoviesFetchedListener listener) {
			this.listener = listener;

		}

		@Override
		protected List<Movie> doInBackground(String... tokens) {
			String accessToken = tokens[0];

			String apiUrl = "https://api.themoviedb.org/3/movie/upcoming?include_adult=false&include_video=false&language="+language+"&page=1&region="+region+"&sort_by=popularity.desc";

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
	public static void getMovieVideos(String accessToken, int movieId, OnVideosFetchedListener listener) {
		new FetchMovieVideosTask(listener).execute(accessToken, String.valueOf(movieId));
	}

	private static class FetchMovieVideosTask extends AsyncTask<String, Void, List<Video>> {
		private final OnVideosFetchedListener listener;

		FetchMovieVideosTask(OnVideosFetchedListener listener) {
			this.listener = listener;
		}

		@Override
		protected List<Video> doInBackground(String... tokens) {
			String accessToken = tokens[0];
			int movieId = Integer.parseInt(tokens[1]);
			String apiUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?language="+language;

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
					TypeToken<VideoResult> token = new TypeToken<VideoResult>() {};
					VideoResult videoResponse = gson.fromJson(response.body().string(), token.getType());
					return videoResponse.getResults();
				} else {
					// Handle error
					Log.e("MovieList", "Error fetching movie videos");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Video> videos) {
			super.onPostExecute(videos);

			if (videos != null) {
				listener.onVideosFetched(videos);
			}
		}
	}

	public interface OnVideosFetchedListener {
		void onVideosFetched(List<Video> videos);
	}

}
