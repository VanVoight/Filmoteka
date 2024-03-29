package pl.edu.pb.filmoteka.Adapters;

import static pl.edu.pb.filmoteka.MovieList.getMovieVideosDefault;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import pl.edu.pb.filmoteka.Activities.DetailsActivity;
import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.FavouriteMovies;
import pl.edu.pb.filmoteka.DB.MyListMovies;
import pl.edu.pb.filmoteka.DB.Rating;
import pl.edu.pb.filmoteka.DB.Review;
import pl.edu.pb.filmoteka.DB.WatchedMovies;
import pl.edu.pb.filmoteka.Models.Movie;
import pl.edu.pb.filmoteka.Models.TVShow;
import pl.edu.pb.filmoteka.Models.Video;
import pl.edu.pb.filmoteka.MovieList;
import pl.edu.pb.filmoteka.R;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder> {


    private int orientation = Configuration.ORIENTATION_PORTRAIT;


    public void setOrientation(int orientation) {
        this.orientation = orientation;
        notifyDataSetChanged();
    }

    private List<TVShow> movies;
    private static long userId;
    private static long userRoleId;
    private static AppDatabase appDatabase;

    public void setMovies(List<TVShow> movies) {
        this.movies = movies;
    }

    public TVShowAdapter(long id, AppDatabase appDatabase, long roleId) {
        this.userId = id;
        this.appDatabase = appDatabase;
        this.userRoleId = roleId;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (parent.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            Log.e("MovieAdapter","ustawiam widok poziomy");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_movie_details_land, parent, false);
        } else {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_movie_details, parent, false);
            Log.e("MovieAdapter","ustawiam widok pionowy");
        }
        return new TVShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {

        TVShow movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    public static class TVShowViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private ImageView moviePosterImageView;
        private ImageView topRightIconImageView;

        private View circle;
        private List<TVShow> movieList;
        private TextView releaseDateTextView;
        private int movieId;
        private TextView voteAverageTextView;

        private int highVoteColor;
        private int mediumVoteColor;
        private int lowVoteColor;
        private String overview;
        private String key;

        private void showPopupMenu(View view, Context context) {
            PopupMenu popupMenu = new PopupMenu(context, view);
            MenuInflater inflater = popupMenu.getMenuInflater();

            if (userRoleId == 1) {
                inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
            } else {
                inflater.inflate(R.menu.popup_menu_reviewer, popupMenu.getMenu());
            }
            MenuItem watchedMenuItem = popupMenu.getMenu().findItem(R.id.menu_watched);

            if (isMovieWatched()) {
                watchedMenuItem.setTitle(R.string.rem_seen);
            } else {
                watchedMenuItem.setTitle(R.string.add_seen);
            }
            MenuItem favoriteMenuItem = popupMenu.getMenu().findItem(R.id.menu_favorite);

            if (isMovieInFavorites()) {
                favoriteMenuItem.setTitle(R.string.rem_fav);
            } else {
                favoriteMenuItem.setTitle(R.string.add_fav);
            }
            MenuItem myListMenuItem = popupMenu.getMenu().findItem(R.id.menu_rate);

            if (isMovieInMyList()) {
                myListMenuItem.setTitle(R.string.rem_to_watch);
            } else {
                myListMenuItem.setTitle(R.string.add_to_watch);
            }

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menu_favorite) {
                        if (isMovieInFavorites()) {
                            removeFromFavorites();
                        } else {
                            addToFavorites();
                        }
                        return true;
                    } else if (item.getItemId() == R.id.menu_watched) {
                        if (isMovieWatched()) {
                            removeFromWatched();
                        } else {
                            addToWatched();
                        }
                        return true;
                    } else if (item.getItemId() == R.id.menu_rate) {
                        if (isMovieInMyList()) {
                            removeFromMyList();
                        } else {
                            addToMyList();
                        }
                        return true;
                    } else if (item.getItemId() == R.id.menu_review) {
                        showReviewEditorDialog(itemView.getContext());
                        return true;
                    } else if (item.getItemId() == R.id.menu_star_rating) {

                        showRatingDialog(itemView.getContext());
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            popupMenu.show();
        }
        private void showReviewEditorDialog(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.review_editor_layout, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
            builder.setView(dialogView);
            builder.setTitle(context.getString(R.string.write_review));


            EditText reviewEditText = dialogView.findViewById(R.id.rtEditText);
            reviewEditText.setTextColor(ContextCompat.getColor(context, R.color.gold));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String reviewText = reviewEditText.getText().toString();
                    saveReviewToDatabase(reviewText);
                    Toast.makeText(itemView.getContext(), R.string.review_submitted, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        private void saveReviewToDatabase(String reviewText) {

            Review review = new Review();
            review.userId=userId;
            review.movieId=movieId;
            review.content=reviewText;

            new SaveReviewTask(itemView).execute(review);
        }


        private class SaveReviewTask extends AsyncTask<Review, Void, Integer> {
            private WeakReference<View> itemViewReference;

            public SaveReviewTask(View itemView) {
                itemViewReference = new WeakReference<>(itemView);
            }

            @Override
            protected Integer doInBackground(Review... reviews) {
                int existingCount = appDatabase.reviewDao().checkIfReviewExists(userId, movieId);

                if (existingCount == 0) {
                    appDatabase.reviewDao().insertReview(reviews[0]);
                }

                return existingCount;
            }


            @Override
            protected void onPostExecute(Integer existingCount) {
                View itemView = itemViewReference.get();
                String toastMessage = itemView.getContext().getString(R.string.review_already_exists);
                String toastMessage2 = itemView.getContext().getString(R.string.review_submitted);
                if (itemView != null) {
                    if (existingCount == 0) {
                        Toast.makeText(itemView.getContext(), toastMessage2, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(itemView.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        private void showRatingDialog(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.dialog_layout, null);

            RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
            float savedRating = getSavedRatingFromDatabase();
            ratingBar.setRating(savedRating);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
            builder.setView(dialogView);
            builder.setTitle(context.getString(R.string.rate));

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    float rating = ratingBar.getRating();
                    saveRatingToDatabase(rating);
                    String message = itemView.getContext().getString(R.string.rated_movie_as) + ": " + rating;
                    Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            });

            builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        private void saveRatingToDatabase(float rating) {
            Rating movieRating = new Rating();
            movieRating.userId = userId;
            movieRating.movieId = movieId;
            movieRating.rating = rating;

            new SaveRatingTask(itemView).execute(movieRating);
        }


        private float getSavedRatingFromDatabase() {
            try {
                return new GetRatingTask(movieId).execute().get();
            } catch (Exception e) {
                e.printStackTrace();
                return 0.0f;
            }
        }


        private class SaveRatingTask extends AsyncTask<Rating, Void, Void> {
            private WeakReference<View> itemViewReference;

            public SaveRatingTask(View itemView) {
                itemViewReference = new WeakReference<>(itemView);
            }

            @Override
            protected Void doInBackground(Rating... ratings) {
                int existingCount = appDatabase.ratingDao().checkIfRatingExists(userId, movieId);

                if (existingCount > 0) {
                    appDatabase.ratingDao().updateRatings(userId,movieId,ratings[0].rating);

                } else {
                    appDatabase.ratingDao().insertRating(ratings[0]);

                }

                return null;
            }
        }


        private static class GetRatingTask extends AsyncTask<Void, Void, Float> {
            private int movieId;

            public GetRatingTask(int movieId) {
                this.movieId = movieId;
            }

            @Override
            protected Float doInBackground(Void... voids) {
                return appDatabase.ratingDao().getRatingForMovie(userId, movieId);
            }
        }
        private void removeFromFavorites() {

            new RemoveFromFavoritesTask(itemView).execute();
        }

        private class RemoveFromFavoritesTask extends AsyncTask<Void, Void, Void> {
            private WeakReference<View> itemViewReference;

            public RemoveFromFavoritesTask(View itemView) {
                itemViewReference = new WeakReference<>(itemView);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                appDatabase.favouriteMoviesDao().deleteFavouriteMovie(userId, movieId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                View itemView = itemViewReference.get();
                if (itemView != null) {

                    Toast.makeText(itemView.getContext(), R.string.toast_remove, Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void addToFavorites() {

            FavouriteMovies favouriteMovie = new FavouriteMovies();
            favouriteMovie.userId = userId;
            favouriteMovie.movieDbId = movieId;

            new AddToFavoritesTask(itemView).execute(favouriteMovie);
        }

        private class AddToFavoritesTask extends AsyncTask<FavouriteMovies, Void, Integer> {
            private WeakReference<View> itemViewReference;

            public AddToFavoritesTask(View itemView) {
                itemViewReference = new WeakReference<>(itemView);
            }

            @Override
            protected Integer doInBackground(FavouriteMovies... favouriteMovies) {
                int existingCount = appDatabase.favouriteMoviesDao().checkIfFavouriteMovieExists(userId, movieId);

                if (existingCount == 0) {
                    appDatabase.favouriteMoviesDao().insertFavouriteMovie(favouriteMovies[0]);
                }

                return existingCount;
            }

            @Override
            protected void onPostExecute(Integer existingCount) {
                View itemView = itemViewReference.get();
                String toastMessage = itemView.getContext().getString(R.string.toast_already_in);
                String toastMessage2 = itemView.getContext().getString(R.string.toast_added_to_fav);
                if (itemView != null) {
                    if (existingCount == 0) {
                        Toast.makeText(itemView.getContext(), toastMessage2, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(itemView.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        private void removeFromWatched() {

            new RemoveFromWatchedTask(itemView).execute();
        }

        private class RemoveFromWatchedTask extends AsyncTask<Void, Void, Void> {
            private WeakReference<View> itemViewReference;

            public RemoveFromWatchedTask(View itemView) {
                itemViewReference = new WeakReference<>(itemView);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                appDatabase.watchedMoviesDao().deleteWatchedMovies(userId, movieId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                View itemView = itemViewReference.get();
                if (itemView != null) {

                    Toast.makeText(itemView.getContext(), R.string.toast_remove_watched, Toast.LENGTH_SHORT).show();
                }
            }
        }
        private void addToWatched() {

            WatchedMovies watchedMovies = new WatchedMovies();
            watchedMovies.userId = userId;
            watchedMovies.movieDbId = movieId;

            new AddToWatchedTask(itemView).execute(watchedMovies);
        }

        private class AddToWatchedTask extends AsyncTask<WatchedMovies, Void, Integer> {
            private WeakReference<View> itemViewReference;

            public AddToWatchedTask(View itemView) {
                itemViewReference = new WeakReference<>(itemView);
            }

            @Override
            protected Integer doInBackground(WatchedMovies... watchedMovies) {
                int existingCount = appDatabase.watchedMoviesDao().checkIfWatchedMovieExists(userId, movieId);

                if (existingCount == 0) {
                    appDatabase.watchedMoviesDao().insertWatchedMovie(watchedMovies[0]);
                }

                return existingCount;
            }

            @Override
            protected void onPostExecute(Integer existingCount) {
                View itemView = itemViewReference.get();
                String toastMessage = itemView.getContext().getString(R.string.toast_already_in_watch);
                String toastMessage2 = itemView.getContext().getString(R.string.toast_added_to_watch);
                if (itemView != null) {
                    if (existingCount == 0) {
                        Toast.makeText(itemView.getContext(), toastMessage2, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(itemView.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        private void removeFromMyList() {

            new RemoveFromMyListTask(itemView).execute();
        }

        private class RemoveFromMyListTask extends AsyncTask<Void, Void, Void> {
            private WeakReference<View> itemViewReference;

            public RemoveFromMyListTask(View itemView) {
                itemViewReference = new WeakReference<>(itemView);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                appDatabase.myListMoviesDao().deleteMyListMovies(userId, movieId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                View itemView = itemViewReference.get();
                if (itemView != null) {

                    Toast.makeText(itemView.getContext(), R.string.toast_remove_list, Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void addToMyList() {

            MyListMovies myListMovies = new MyListMovies();
            myListMovies.userId = userId;
            myListMovies.movieDbId = movieId;

            new AddToMyListTask(itemView).execute(myListMovies);
        }

        private class AddToMyListTask extends AsyncTask<MyListMovies, Void, Integer> {
            private WeakReference<View> itemViewReference;

            public AddToMyListTask(View itemView) {
                itemViewReference = new WeakReference<>(itemView);
            }

            @Override
            protected Integer doInBackground(MyListMovies... myListMovies) {
                int existingCount = appDatabase.myListMoviesDao().checkIfMyListMovieExists(userId, movieId);

                if (existingCount == 0) {
                    appDatabase.myListMoviesDao().insertMyListMovie(myListMovies[0]);
                }

                return existingCount;
            }

            @Override
            protected void onPostExecute(Integer existingCount) {
                View itemView = itemViewReference.get();
                String toastMessage = itemView.getContext().getString(R.string.toast_already_in_ml);
                String toastMessage2 = itemView.getContext().getString(R.string.toast_added_to_ml);
                if (itemView != null) {
                    if (existingCount == 0) {
                        Toast.makeText(itemView.getContext(), toastMessage2, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(itemView.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        public TVShowViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            moviePosterImageView = itemView.findViewById(R.id.moviePosterImageView);
            moviePosterImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    showAdditionalInfoDialog();
                    return true;
                }
            });
            moviePosterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), DetailsActivity.class);

                    intent.putExtra("userId", userId);
                    intent.putExtra("movieId", movieId);
                    intent.putExtra("roleId",userRoleId);


                    v.getContext().startActivity(intent);
                }
            });
            topRightIconImageView = itemView.findViewById(R.id.topRightIconImageView);

            topRightIconImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            topRightIconImageView.setImageResource(R.drawable.img_dots_menu_down);
                            showPopupMenu(topRightIconImageView, itemView.getContext());
                            return true;
                        case MotionEvent.ACTION_UP:
                            topRightIconImageView.setImageResource(R.drawable.img_dots_menu);
                            return true;
                        default:
                            return false;
                    }
                }
            });

            circle = itemView.findViewById(R.id.circleView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);

            voteAverageTextView = itemView.findViewById(R.id.voteAverageTextView);
            highVoteColor = ContextCompat.getColor(itemView.getContext(), R.color.high_vote_color);
            mediumVoteColor = ContextCompat.getColor(itemView.getContext(), R.color.medium_vote_color);
            lowVoteColor = ContextCompat.getColor(itemView.getContext(), R.color.low_vote_color);

        }
        private boolean isMovieInFavorites() {

            try {
                return new CheckFavoritesTask(movieId).execute().get();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        private static class CheckFavoritesTask extends AsyncTask<Void, Void, Boolean> {
            private int movieId;
            public CheckFavoritesTask(int movieId) {
                this.movieId = movieId;
            }
            @Override
            protected Boolean doInBackground(Void... voids) {

                return appDatabase.favouriteMoviesDao().checkIfFavouriteMovieExists(userId, movieId) > 0;
            }
        }
        private boolean isMovieWatched() {
            try {
                return new CheckWatchedTask(movieId).execute().get();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        private static class CheckWatchedTask extends AsyncTask<Void, Void, Boolean> {
            private int movieId;

            public CheckWatchedTask(int movieId) {
                this.movieId = movieId;
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                return appDatabase.watchedMoviesDao().checkIfWatchedMovieExists(userId, movieId) > 0;
            }
        }
        private boolean isMovieInMyList() {
            try {
                return new CheckMyListTask(movieId).execute().get();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        private static class CheckMyListTask extends AsyncTask<Void, Void, Boolean> {
            private int movieId;

            public CheckMyListTask(int movieId) {
                this.movieId = movieId;
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                return appDatabase.myListMoviesDao().checkIfMyListMovieExists(userId, movieId) > 0;
            }
        }
        public void bind(TVShow movie) {
            titleTextView.setText(movie.getName());
            releaseDateTextView.setText(movie.getFirstAirDate());
            overview = movie.getOverview();
            movieId = movie.getId();
            double voteAverage = movie.getVoteAverage();
            DecimalFormat decimalFormat = new DecimalFormat("#.0");
            String formattedVoteAverage = decimalFormat.format(voteAverage);
            voteAverageTextView.setText(formattedVoteAverage);

            getMovieVideosDefault("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", movie.getId(), new MovieList.OnVideosFetchedListener() {
                @Override
                public void onVideosFetched(List<Video> videos) {
                    if (videos != null && !videos.isEmpty()) {
                        for (Video video : videos) {

                            if ("Trailer".equals(video.getType()) && video.isOfficial()) {

                                key = video.getKey();
                                break;
                            }
                        }
                    }

                }
            });
            int circleStrokeColor = getCircleStrokeColorBasedOnVoteAverage(movie.getVoteAverage());
            circle.getBackground().setColorFilter(circleStrokeColor, PorterDuff.Mode.SRC_ATOP);

            new LoadImageTask(moviePosterImageView).execute(movie.getPosterPath());
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
                    Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);


                    Bitmap roundedBitmap = getRoundedCornerBitmap(originalBitmap, 50);

                    return roundedBitmap;
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


            private Bitmap getRoundedCornerBitmap(Bitmap bitmap, int radius) {
                Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);

                final Paint paint = new Paint();
                final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                final RectF rectF = new RectF(rect);

                paint.setAntiAlias(true);
                canvas.drawRoundRect(rectF, radius, radius, paint);

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(bitmap, rect, rect, paint);

                return output;
            }
        }

        private void showAdditionalInfoDialog() {
            Context context = itemView.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
            builder.setTitle(context.getString(R.string.description_title));
            String message = overview;
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.gold));
            SpannableString spannableString = new SpannableString(message);
            spannableString.setSpan(colorSpan, 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            builder.setMessage(spannableString);


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            builder.setNeutralButton(context.getString(R.string.trailer_yt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openYouTubeVideo();
                }
            });

            builder.show();
        }

        private void openYouTubeVideo() {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + key));


            if (intent.resolveActivity(itemView.getContext().getPackageManager()) != null) {
                itemView.getContext().startActivity(intent);
            } else {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + key));
                itemView.getContext().startActivity(browserIntent);
            }
        }


    }
}
