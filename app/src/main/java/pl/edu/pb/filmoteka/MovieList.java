package pl.edu.pb.filmoteka;

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
import pl.edu.pb.filmoteka.Models.Movie;
import pl.edu.pb.filmoteka.Models.MovieCredits;
import pl.edu.pb.filmoteka.Models.MovieDetails;
import pl.edu.pb.filmoteka.Models.MovieResult;
import pl.edu.pb.filmoteka.Models.PersonDetails;
import pl.edu.pb.filmoteka.Models.PersonMovieCredits;
import pl.edu.pb.filmoteka.Models.TVShow;
import pl.edu.pb.filmoteka.Models.TVShowResult;
import pl.edu.pb.filmoteka.Models.Video;
import pl.edu.pb.filmoteka.Models.VideoResult;

public class MovieList {
    private static String language;
    private static String region="pl";

    public static void setLanguageAndRegion(Context context) {
        Resources resources = context.getResources();
        language = resources.getString(R.string.poland);

    }
    public static void setRegion(String name) {

        region = name;
    }

    public static void searchMovies(AppDatabase appDatabase, String query, String accessToken, OnMoviesFetchedListener listener) {
        new FetchSearchMoviesTask(appDatabase, query, listener).execute(accessToken);
    }
    private static class FetchSearchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
        private final OnMoviesFetchedListener listener;
        private final AppDatabase appDatabase;
        private final String query;

        FetchSearchMoviesTask(AppDatabase appDatabase, String query, OnMoviesFetchedListener listener) {
            this.appDatabase = appDatabase;
            this.query = query;
            this.listener = listener;
        }

        @Override
        protected List<Movie> doInBackground(String... tokens) {
            String accessToken = tokens[0];

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();

            List<Movie> movies = new ArrayList<>();

            // Construct the API URL for searching movies
            String apiUrl = "https://api.themoviedb.org/3/search/movie?include_adult=false&query=" + query + "&language=" + language + "&region=" + region;

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
                    movies = movieResponse.getResults();
                } else {
                    // Handle error
                    Log.e("MovieList", "Error fetching search results");
                }
            } catch (IOException e) {
                e.printStackTrace();
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

    public static void getMovieVideosDefault(String accessToken, int movieId, OnVideosFetchedListener listener) {
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
                    TypeToken<VideoResult> token = new TypeToken<VideoResult>() {};
                    VideoResult videoResponse = gson.fromJson(response.body().string(), token.getType());

                    List<Video> videos = videoResponse.getResults();

                    if (videos != null && !videos.isEmpty()) {
                        return videos;
                    } else {
                        apiUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?language=en-US";
                        request = new Request.Builder()
                                .url(apiUrl)
                                .header("Authorization", "Bearer " + accessToken)
                                .header("accept", "application/json")
                                .build();

                        response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            videoResponse = gson.fromJson(response.body().string(), token.getType());
                            return videoResponse.getResults();
                        } else {
                            Log.e("MovieList", "Error fetching movie videos");
                        }
                    }
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







    public static void getRandomTopMovies(String accessToken, int totalPages, OnMoviesFetchedListener listener) {

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
            String apiUrl = "https://api.themoviedb.org/3/movie/popular?include_adult=false&language=" + language + "&region=" + region + "&page=" + page;

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
    public interface OnCreditsFetchedListener {
        void onCreditsFetched(MovieCredits movieCredits);
    }
    public static void getMovieCredits(String accessToken, int movieId, OnCreditsFetchedListener listener) {
        new FetchMovieCreditsTask(listener).execute(accessToken, String.valueOf(movieId));
    }

    private static class FetchMovieCreditsTask extends AsyncTask<String, Void, MovieCredits> {
        private final OnCreditsFetchedListener listener;

        FetchMovieCreditsTask(OnCreditsFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected MovieCredits doInBackground(String... tokens) {
            String accessToken = tokens[0];
            int movieId = Integer.parseInt(tokens[1]);
            String apiUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/credits";

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
                    TypeToken<MovieCredits> token = new TypeToken<MovieCredits>() {};
                    return gson.fromJson(response.body().string(), token.getType());
                } else {
                    // Handle error
                    Log.e("MovieCredits", "Error fetching movie credits");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(MovieCredits movieCredits) {
            super.onPostExecute(movieCredits);

            if (movieCredits != null) {
                listener.onCreditsFetched(movieCredits);
            }
        }
    }

    public static void getRecommendationsForMovie(String accessToken, int movieId, OnMoviesFetchedListener listener) {
        new FetchRecommendationsTask(listener).execute(accessToken, String.valueOf(movieId));
    }

    private static class FetchRecommendationsTask extends AsyncTask<String, Void, List<Movie>> {
        private final OnMoviesFetchedListener listener;

        FetchRecommendationsTask(OnMoviesFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Movie> doInBackground(String... tokens) {
            String accessToken = tokens[0];
            int movieId = Integer.parseInt(tokens[1]);

            String apiUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/recommendations?include_adult=false&language=" + language + "&page=1";

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
                    Log.e("MovieList", "Error fetching movie recommendations");
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
    public static void getPersonDetails(String accessToken, int personId, OnPersonFetchedListener listener) {
        new FetchPersonDetailsTask(listener).execute(accessToken, String.valueOf(personId));
    }

    private static class FetchPersonDetailsTask extends AsyncTask<String, Void, PersonDetails> {
        private final OnPersonFetchedListener listener;

        FetchPersonDetailsTask(OnPersonFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected PersonDetails doInBackground(String... tokens) {
            String accessToken = tokens[0];
            int personId = Integer.parseInt(tokens[1]);

            String apiUrl = "https://api.themoviedb.org/3/person/" + personId + "?language=" + language;

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
                    return gson.fromJson(response.body().string(), PersonDetails.class);
                } else {
                    // Handle error
                    Log.e("PersonDetails", "Error fetching person details");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(PersonDetails personDetails) {
            super.onPostExecute(personDetails);

            if (personDetails != null) {
                listener.onPersonFetched(personDetails);
            }
        }
    }
    public interface OnPersonFetchedListener {
        void onPersonFetched(PersonDetails personDetails);

        void onFetchError(String errorMessage);
    }
    public static void getPersonDetailsEng(String accessToken, int personId, OnPersonFetchedListener listener) {
        new FetchPersonDetailsEngTask(listener).execute(accessToken, String.valueOf(personId));
    }

    private static class FetchPersonDetailsEngTask extends AsyncTask<String, Void, PersonDetails> {
        private final OnPersonFetchedListener listener;

        FetchPersonDetailsEngTask(OnPersonFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected PersonDetails doInBackground(String... tokens) {
            String accessToken = tokens[0];
            int personId = Integer.parseInt(tokens[1]);

            String apiUrl = "https://api.themoviedb.org/3/person/" + personId + "?language=en-US";

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
                    return gson.fromJson(response.body().string(), PersonDetails.class);
                } else {

                    Log.e("PersonDetails", "Error fetching person details");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(PersonDetails personDetails) {
            super.onPostExecute(personDetails);

            if (personDetails != null) {
                listener.onPersonFetched(personDetails);
            }
        }
    }
    public static void getPersonMovieCredits(String accessToken, int personId, OnMovieCreditsFetchedListener listener) {
        new FetchPersonMovieCreditsTask(listener).execute(accessToken, String.valueOf(personId));
    }

    private static class FetchPersonMovieCreditsTask extends AsyncTask<String, Void, PersonMovieCredits> {
        private final OnMovieCreditsFetchedListener listener;

        FetchPersonMovieCreditsTask(OnMovieCreditsFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected PersonMovieCredits doInBackground(String... tokens) {
            String accessToken = tokens[0];
            int personId = Integer.parseInt(tokens[1]);

            String apiUrl = "https://api.themoviedb.org/3/person/" + personId + "/movie_credits?language=" + language;

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
                    return gson.fromJson(response.body().string(), PersonMovieCredits.class);
                } else {
                    // Handle error
                    Log.e("MovieCredits", "Error fetching movie credits");
                    listener.onFetchError("Error fetching movie credits");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(PersonMovieCredits personMovieCredits) {
            super.onPostExecute(personMovieCredits);

            if (personMovieCredits != null) {
                listener.onMovieCreditsFetched(personMovieCredits);
            }
        }
    }

    public interface OnMovieCreditsFetchedListener {
        void onMovieCreditsFetched(PersonMovieCredits personMovieCredits);

        void onFetchError(String errorMessage);
    }

    public static void getTopRatedTVShows(String accessToken, OnTVShowsFetchedListener listener) {
        new FetchTopRatedTVShowListTask(listener).execute(accessToken);
    }

    private static class FetchTopRatedTVShowListTask extends AsyncTask<String, Void, List<TVShow>> {
        private final OnTVShowsFetchedListener listener;

        FetchTopRatedTVShowListTask(OnTVShowsFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<TVShow> doInBackground(String... tokens) {
            String accessToken = tokens[0];
            String apiUrl = "https://api.themoviedb.org/3/tv/top_rated?language=" + language + "&page=1";

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
                    TypeToken<TVShowResult> token = new TypeToken<TVShowResult>() {};
                    TVShowResult tvShowResponse = gson.fromJson(response.body().string(), token.getType());
                    return tvShowResponse.getResults();
                } else {
                    // Handle error
                    Log.e("TVShowList", "Error fetching top-rated TV show list");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<TVShow> tvShows) {
            super.onPostExecute(tvShows);

            if (tvShows != null) {
                listener.onTVShowsFetched(tvShows);
            }
        }
    }
    public static void getPopularTVShows(String accessToken, OnTVShowsFetchedListener listener) {
        new FetchPopularTVShowListTask(listener).execute(accessToken);
    }

    private static class FetchPopularTVShowListTask extends AsyncTask<String, Void, List<TVShow>> {
        private final OnTVShowsFetchedListener listener;

        FetchPopularTVShowListTask(OnTVShowsFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<TVShow> doInBackground(String... tokens) {
            String accessToken = tokens[0];
            String apiUrl = "https://api.themoviedb.org/3/tv/popular?language=" + language +"&region="+ region + "&page=1";

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
                    TypeToken<TVShowResult> token = new TypeToken<TVShowResult>() {};
                    TVShowResult tvShowResponse = gson.fromJson(response.body().string(), token.getType());
                    return tvShowResponse.getResults();
                } else {
                    // Handle error
                    Log.e("TVShowList", "Error fetching popular TV show list");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<TVShow> tvShows) {
            super.onPostExecute(tvShows);

            if (tvShows != null) {
                listener.onTVShowsFetched(tvShows);
            }
        }
    }
    public static void getAiringTodayTVShows(String accessToken, OnTVShowsFetchedListener listener) {
        new FetchAiringTodayTVShowListTask(listener).execute(accessToken);
    }

    private static class FetchAiringTodayTVShowListTask extends AsyncTask<String, Void, List<TVShow>> {
        private final OnTVShowsFetchedListener listener;

        FetchAiringTodayTVShowListTask(OnTVShowsFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<TVShow> doInBackground(String... tokens) {
            String accessToken = tokens[0];
            String apiUrl = "https://api.themoviedb.org/3/tv/airing_today?language=" + language +"&region="+ region + "&page=1";

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
                    TypeToken<TVShowResult> token = new TypeToken<TVShowResult>() {};
                    TVShowResult tvShowResponse = gson.fromJson(response.body().string(), token.getType());
                    return tvShowResponse.getResults();
                } else {
                    // Handle error
                    Log.e("TVShowList", "Error fetching airing today TV show list");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<TVShow> tvShows) {
            super.onPostExecute(tvShows);

            if (tvShows != null) {
                listener.onTVShowsFetched(tvShows);
            }
        }
    }

    public static void getOnTheAirTVShows(String accessToken, OnTVShowsFetchedListener listener) {
        new FetchOnTheAirTVShowListTask(listener).execute(accessToken);
    }

    private static class FetchOnTheAirTVShowListTask extends AsyncTask<String, Void, List<TVShow>> {
        private final OnTVShowsFetchedListener listener;

        FetchOnTheAirTVShowListTask(OnTVShowsFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<TVShow> doInBackground(String... tokens) {
            String accessToken = tokens[0];
            String apiUrl = "https://api.themoviedb.org/3/tv/on_the_air?language=" + language + "&region="+region+ "&page=1";

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
                    TypeToken<TVShowResult> token = new TypeToken<TVShowResult>() {};
                    TVShowResult tvShowResponse = gson.fromJson(response.body().string(), token.getType());
                    return tvShowResponse.getResults();
                } else {
                    // Handle error
                    Log.e("TVShowList", "Error fetching on the air TV show list");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<TVShow> tvShows) {
            super.onPostExecute(tvShows);

            if (tvShows != null) {
                listener.onTVShowsFetched(tvShows);
            }
        }
    }


    public interface OnTVShowsFetchedListener {
        void onTVShowsFetched(List<TVShow> tvShows);
        void onError(String errorMessage);
    }




}