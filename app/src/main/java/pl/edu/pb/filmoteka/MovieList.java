package pl.edu.pb.filmoteka;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.FavouriteMovies;
import pl.edu.pb.filmoteka.DB.WatchedMovies;
import pl.edu.pb.filmoteka.DB.MyListMovies;

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

    public static void getFavouriteMovies(AppDatabase appDatabase, long userId, String accessToken, OnMoviesFetchedListener listener) {
        new FetchFavouriteListTask(appDatabase, userId, listener).execute(accessToken);
    }

    private static class FetchFavouriteListTask extends AsyncTask<String, Void, List<Movie>> {
        private final OnMoviesFetchedListener listener;
        private final AppDatabase appDatabase;

        private final long userId;

        FetchFavouriteListTask(AppDatabase appDatabase, long userId, OnMoviesFetchedListener listener) {
            this.appDatabase = appDatabase;
            this.listener = listener;
            this.userId = userId;
        }

        @Override
        protected List<Movie> doInBackground(String... tokens) {
            String accessToken = tokens[0];

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();

            List<Movie> movies = new ArrayList<>();

            List<FavouriteMovies> favouriteMovies = appDatabase.favouriteMoviesDao().getFavouriteMovies(userId);


            // Loop through each movie ID and fetch details
            for (FavouriteMovies favouriteMovie : favouriteMovies) {
                String apiUrl = "https://api.themoviedb.org/3/movie/" + favouriteMovie.movieDbId + "?language=" + language + "&region=" + region;

                Request request = new Request.Builder()
                        .url(apiUrl)
                        .header("Authorization", "Bearer " + accessToken)
                        .header("accept", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Movie movie = gson.fromJson(response.body().string(), Movie.class);
                        movies.add(movie);
                    } else {
                        // Handle error
                        Log.e("MovieList", "Error fetching movie with ID: " + favouriteMovie.movieDbId);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);

            if (movies != null) {
                listener.onMoviesFetched(movies);
            }
        }

    }

    public static void getWatchedMovies(AppDatabase appDatabase, long userId, String accessToken, OnMoviesFetchedListener listener) {
        new FetchWatchedListTask(appDatabase, userId, listener).execute(accessToken);
    }

    private static class FetchWatchedListTask extends AsyncTask<String, Void, List<Movie>> {
        private final OnMoviesFetchedListener listener;
        private final AppDatabase appDatabase;

        private final long userId;

        FetchWatchedListTask(AppDatabase appDatabase, long userId, OnMoviesFetchedListener listener) {
            this.appDatabase = appDatabase;
            this.listener = listener;
            this.userId = userId;
        }

        @Override
        protected List<Movie> doInBackground(String... tokens) {
            String accessToken = tokens[0];

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();

            List<Movie> movies = new ArrayList<>();

            List<WatchedMovies> watchedMovies = appDatabase.watchedMoviesDao().getAllWatchedMovies(userId);

            // Loop through each movie ID and fetch details
            for (WatchedMovies watchedMovie : watchedMovies) {
                String apiUrl = "https://api.themoviedb.org/3/movie/" + watchedMovie.movieDbId + "?language=" + language + "&region=" + region;

                Request request = new Request.Builder()
                        .url(apiUrl)
                        .header("Authorization", "Bearer " + accessToken)
                        .header("accept", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Movie movie = gson.fromJson(response.body().string(), Movie.class);
                        movies.add(movie);
                    } else {
                        System.out.println("Error fetching movie with ID: " + watchedMovie.movieDbId);
                        // Handle error
                        Log.e("MovieList", "Error fetching movie with ID: " + watchedMovie.movieDbId);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);

            if (movies != null) {
                listener.onMoviesFetched(movies);
            }
        }
    }

    public static void getMyMovies(AppDatabase appDatabase, long userId, String accessToken, OnMoviesFetchedListener listener) {
        new FetchMyListTask(appDatabase, userId, listener).execute(accessToken);
    }

    private static class FetchMyListTask extends AsyncTask<String, Void, List<Movie>> {
        private final OnMoviesFetchedListener listener;
        private final AppDatabase appDatabase;

        private final long userId;

        FetchMyListTask(AppDatabase appDatabase, long userId, OnMoviesFetchedListener listener) {
            this.appDatabase = appDatabase;
            this.listener = listener;
            this.userId = userId;
        }

        @Override
        protected List<Movie> doInBackground(String... tokens) {
            String accessToken = tokens[0];

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();

            List<Movie> movies = new ArrayList<>();

            List<MyListMovies> myListMovies = appDatabase.myListMoviesDao().getAllMyListMovies(userId);

            // Loop through each movie ID and fetch details
            for (MyListMovies myListMovie : myListMovies) {
                String apiUrl = "https://api.themoviedb.org/3/movie/" + myListMovie.movieDbId + "?language=" + language + "&region=" + region;

                Request request = new Request.Builder()
                        .url(apiUrl)
                        .header("Authorization", "Bearer " + accessToken)
                        .header("accept", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Movie movie = gson.fromJson(response.body().string(), Movie.class);
                        movies.add(movie);
                    } else {
                        System.out.println("Error fetching movie with ID: " + myListMovie.movieDbId);
                        // Handle error
                        Log.e("MovieList", "Error fetching movie with ID: " + myListMovie.movieDbId);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);

            if (movies != null) {
                listener.onMoviesFetched(movies);
            }
        }
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
            String apiUrl = "https://api.themoviedb.org/3/movie/popular?include_adult=false&include_video=false&language=" + language + "&page=1&region=" + region + "&sort_by=popularity.desc";

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
                    TypeToken<MovieResult> token = new TypeToken<MovieResult>() {
                    };
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
            String apiUrl = "https://api.themoviedb.org/3/movie/top_rated?include_adult=false&include_video=false&language=" + language + "&page=1&region=" + region + "&sort_by=vote_average.desc&without_genres=99,10755&vote_count.gte=200";

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
                    TypeToken<MovieResult> token = new TypeToken<MovieResult>() {
                    };
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
            String apiUrl = "https://api.themoviedb.org/3/movie/now_playing?include_adult=false&include_video=false&language=" + language + "&page=1&region=" + region + "&sort_by=popularity.desc";

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
                    TypeToken<MovieResult> token = new TypeToken<MovieResult>() {
                    };
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

            String apiUrl = "https://api.themoviedb.org/3/movie/upcoming?include_adult=false&include_video=false&language=" + language + "&page=1&region=" + region + "&sort_by=popularity.desc";

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
                    TypeToken<MovieResult> token = new TypeToken<MovieResult>() {
                    };
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
            String apiUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?language=" + language;

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
                    TypeToken<VideoResult> token = new TypeToken<VideoResult>() {
                    };
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

    public static void getMoviesByGenre(String accessToken, int genreId, OnMoviesFetchedListener listener) {
        new FetchMoviesByGenreTask(listener).execute(accessToken, String.valueOf(genreId));
    }

    private static class FetchMoviesByGenreTask extends AsyncTask<String, Void, List<Movie>> {
        private final OnMoviesFetchedListener listener;

        FetchMoviesByGenreTask(OnMoviesFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Movie> doInBackground(String... tokens) {
            String accessToken = tokens[0];
            int genreId = Integer.parseInt(tokens[1]);
            String apiUrl = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=" + language + "&page=1&region=" + region + "&sort_by=popularity.desc&with_genres=" + genreId;

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
                    TypeToken<MovieResult> token = new TypeToken<MovieResult>() {
                    };
                    MovieResult movieResponse = gson.fromJson(response.body().string(), token.getType());
                    return movieResponse.getResults();
                } else {
                    // Handle error
                    Log.e("MovieList", "Error fetching movies by genre");
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

    public static void getRandomMovie(String accessToken,int movieId, OnMoviesFetchedListener listener) {
        new FetchRandomMovieTask(listener).execute(accessToken, String.valueOf(movieId));
    }

    private static class FetchRandomMovieTask extends AsyncTask<String, Void, Movie> {
        private final OnMoviesFetchedListener listener;

        FetchRandomMovieTask(OnMoviesFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected Movie doInBackground(String... tokens) {
            String accessToken = tokens[0];
            int randomMovieId = Integer.parseInt(tokens[1]);
            String apiUrl = "https://api.themoviedb.org/3/movie/" + randomMovieId + "?language=" + language + "&region=" + region;

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
                    Movie movie = gson.fromJson(response.body().string(), Movie.class);
                    return movie;
                } else {
                    // Handle error
                    Log.e("MovieList", "Error fetching random movie");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);

            if (movie != null) {
                List<Movie> movies = new ArrayList<>();
                movies.add(movie);
                listener.onMoviesFetched(movies);
            }
        }



    }
    public static void getRandomTopMovies(String accessToken, int totalPages, OnMoviesFetchedListener listener) {
        // Losuj liczbę od 1 do totalPages (włącznie)
        int randomPage = new Random().nextInt(totalPages) + 1;
        new FetchRandomTopMoviesTask(listener).execute(accessToken, String.valueOf(randomPage));
    }

    private static class FetchRandomTopMoviesTask extends AsyncTask<String, Void, List<Movie>> {
        private final OnMoviesFetchedListener listener;

        FetchRandomTopMoviesTask(OnMoviesFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Movie> doInBackground(String... tokens) {
            String accessToken = tokens[0];
            int page = Integer.parseInt(tokens[1]);
            String apiUrl = "https://api.themoviedb.org/3/movie/top_rated?language=" + language + "&region=" + region + "&page=" + page;

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
                    MovieResult discoverMoviesResponse = gson.fromJson(response.body().string(), MovieResult.class);
                    return discoverMoviesResponse.getResults();
                } else {
                    // Handle error
                    Log.e("MovieList", "Error fetching random top rated movies");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);

            if (movies != null && !movies.isEmpty()) {

                int randomIndex = new Random().nextInt(movies.size());
                Movie randomMovie = movies.get(randomIndex);


                List<Movie> randomMovieList = new ArrayList<>();
                randomMovieList.add(randomMovie);


                listener.onMoviesFetched(randomMovieList);
            }
        }
    }
    public static void getMovieDetails(String accessToken, int movieId, OnMoviesFetchedDetailsListener listener) {
        new FetchMovieDetailsTask(listener).execute(accessToken, String.valueOf(movieId));
    }
    public interface OnMoviesFetchedDetailsListener {
        void onMoviesFetched(List<MovieDetails> movies);
    }

    private static class FetchMovieDetailsTask extends AsyncTask<String, Void, MovieDetails> {
        private final OnMoviesFetchedDetailsListener listener;

        FetchMovieDetailsTask(OnMoviesFetchedDetailsListener listener) {
            this.listener = listener;
        }

        @Override
        protected MovieDetails doInBackground(String... tokens) {
            String accessToken = tokens[0];
            int movieId = Integer.parseInt(tokens[1]);
            String apiUrl = "https://api.themoviedb.org/3/movie/" + movieId + "?language=" + language;

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
                    MovieDetails movie = gson.fromJson(response.body().string(), MovieDetails.class);
                    return movie;
                } else {
                    // Handle error
                    Log.e("MovieList", "Error fetching movie details");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(MovieDetails movie) {
            super.onPostExecute(movie);

            if (movie != null) {
                List<MovieDetails> movies = new ArrayList<>();
                movies.add(movie);
                listener.onMoviesFetched(movies);
            }
        }
    }



}