package pl.edu.pb.filmoteka;

import static pl.edu.pb.filmoteka.MovieList.getMovieVideos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.PopupMenu;
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

import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.FavouriteMovies;
import pl.edu.pb.filmoteka.DB.MyListMovies;
import pl.edu.pb.filmoteka.DB.MyListMoviesDao;
import pl.edu.pb.filmoteka.DB.WatchedMovies;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

	private List<Movie> movies;
	private static long userId;

	private static long userRoleId;
	private static AppDatabase appDatabase;
	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}
	public MovieAdapter(long id, AppDatabase appDatabase, long roleId){
		this.userId=id;
		this.appDatabase=appDatabase;
		this.userRoleId=roleId;
	}
	@NonNull
	@Override
	public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_movie_details, parent, false);
		return new MovieViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

		Movie movie = movies.get(position);
		holder.bind(movie);
	}

	@Override
	public int getItemCount() {
		return movies != null ? movies.size() : 0;
	}

	public static class MovieViewHolder extends RecyclerView.ViewHolder {
		private TextView titleTextView;
		private ImageView moviePosterImageView;
		private ImageView topRightIconImageView;

		private View circle;
		private List<Movie> movieList;
		private TextView releaseDateTextView;
		private static int movieId;
		private TextView voteAverageTextView;

		private int highVoteColor;
		private int mediumVoteColor;
		private int lowVoteColor;
		private String overview;
		private String key;
		private void showPopupMenu(View view, Context context) {
			PopupMenu popupMenu = new PopupMenu(context, view);
			MenuInflater inflater = popupMenu.getMenuInflater();
			if(userRoleId==1) {
				inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
				popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						if (item.getItemId() == R.id.menu_favorite) {
							Log.d("Logowanie","Oby było git"+userId);
							addToFavorites();

							return true;
						} else if (item.getItemId() == R.id.menu_watched) {
							addToWatched();
							return true;
						} else if (item.getItemId() == R.id.menu_rate) {
							addToMyList();
							return true;
						} else {
							return false;
						}
					}
				});
				popupMenu.show();
			}
			else{
				inflater.inflate(R.menu.popup_menu_reviewer, popupMenu.getMenu());
				popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						if (item.getItemId() == R.id.menu_favorite) {
							Log.d("Logowanie","Oby było git"+userId);
							addToFavorites();

							return true;
						} else if (item.getItemId() == R.id.menu_watched) {
							addToWatched();
							return true;
						} else if (item.getItemId() == R.id.menu_rate) {
							addToMyList();
							return true;
						} else if (item.getItemId() == R.id.menu_review) {
							addToMyList();
							return true;
						}else if (item.getItemId() == R.id.menu_star_rating) {
							addToMyList();
							return true;
						}else {
							return false;
						}
					}
				});
				popupMenu.show();
			}



		}
		private void addToFavorites() {

			FavouriteMovies favouriteMovie = new FavouriteMovies();
			favouriteMovie.userId = userId;
			favouriteMovie.movieDbId = movieId;

			new AddToFavoritesTask(itemView).execute(favouriteMovie);
		}

		private static class AddToFavoritesTask extends AsyncTask<FavouriteMovies, Void, Integer> {
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
		private void addToWatched() {

			WatchedMovies watchedMovies = new WatchedMovies();
			watchedMovies.userId = userId;
			watchedMovies.movieDbId = movieId;

			new AddToWatchedTask(itemView).execute(watchedMovies);
		}

		private static class AddToWatchedTask extends AsyncTask<WatchedMovies, Void, Integer> {
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
		private void addToMyList() {

			MyListMovies myListMovies = new MyListMovies();
			myListMovies.userId = userId;
			myListMovies.movieDbId = movieId;

			new AddToMyListTask(itemView).execute(myListMovies);
		}

		private static class AddToMyListTask extends AsyncTask<MyListMovies, Void, Integer> {
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
		public MovieViewHolder(@NonNull View itemView) {
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
			topRightIconImageView = itemView.findViewById(R.id.topRightIconImageView);

			topRightIconImageView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							topRightIconImageView.setImageResource(R.drawable.img_dots_menu_down);
							showPopupMenu(v, itemView.getContext());
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

		public void bind(Movie movie) {
			titleTextView.setText(movie.getTitle());
			titleTextView.setText(movie.getTitle());
			releaseDateTextView.setText(movie.getReleaseDate());
			overview = movie.getOverview();
			movieId=movie.getId();
			double voteAverage = movie.getVoteAverage();
			DecimalFormat decimalFormat = new DecimalFormat("#.0");
			String formattedVoteAverage = decimalFormat.format(voteAverage);
			voteAverageTextView.setText(formattedVoteAverage);

			getMovieVideos("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", movie.getId(), new MovieList.OnVideosFetchedListener() {
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

			new LoadImageTask(moviePosterImageView).execute(movie.getPosterUrl());
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
			String message = overview ;
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
