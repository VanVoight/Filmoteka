package pl.edu.pb.filmoteka;
import static pl.edu.pb.filmoteka.MovieList.getMovieVideos;

import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

	private List<Movie> movies;

	public void setMovies(List<Movie> movies) {
		this.movies = movies;
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
		private final TextView titleTextView;
		private ImageView moviePosterImageView;
		private View circle;
		private List<Movie> movieList;
		private TextView releaseDateTextView;

		private TextView voteAverageTextView;

		private int highVoteColor;
		private int mediumVoteColor;
		private int lowVoteColor;
		private String overview;
		private String key;

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
			voteAverageTextView.setText(String.valueOf(movie.getVoteAverage()));
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
			AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
			builder.setTitle("Opis:");
			builder.setMessage(overview + "\nZwiastun:\n" + "https://www.youtube.com/watch?v=" + key);


			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});


			builder.setNeutralButton("Otwórz w YouTube", new DialogInterface.OnClickListener() {
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
