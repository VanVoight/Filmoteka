package pl.edu.pb.filmoteka;

import static pl.edu.pb.filmoteka.MovieList.getMovieDetails;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;


public class DetailsActivity extends AppCompatActivity{


	private ImageView imageViewBackdrop;
	private ImageView imageViewPoster;
	private TextView textViewTitleYear;
	private TextView textViewReleaseDate;
	private TextView textViewGenres;
	private TextView textViewVoteAverage;
	private TextView textViewOverview;
	private TextView tagline;
	private CardView cardView;
	private ImageButton imageButton;
	private TextView ytTextView;
	private int movieId;
	private MovieDetails movieDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_layout);
		if (getIntent() != null && getIntent().hasExtra("movieId")) {
			movieId = getIntent().getIntExtra("movieId", 0); // Zakładając, że domyślną wartością jest 0
		} else {
			// Obsłuż brak przekazanego movieId, na przykład wyświetl komunikat błędu lub zakończ aktywność
			Toast.makeText(this, "Brak informacji o movieId", Toast.LENGTH_SHORT).show();
			finish(); // Zakończ aktywność
			return;
		}

		imageViewPoster = findViewById(R.id.imageViewPoster);
		textViewTitleYear = findViewById(R.id.textViewTitleYear);
		textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
		textViewGenres = findViewById(R.id.textViewGenres);
		textViewVoteAverage = findViewById(R.id.textViewVoteAverage);
		textViewOverview = findViewById(R.id.textViewOverview);
		tagline = findViewById(R.id.TagLine);
		cardView = findViewById(R.id.cardView);
		imageButton = findViewById(R.id.imageButtonPlayTrailer);
		ytTextView = findViewById(R.id.ytTextView);


		String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA";
		getMovieDetails(accessToken, movieId, new MovieList.OnMoviesFetchedDetailsListener() {
			@Override
			public void onMoviesFetched(List<MovieDetails> movies) {

				if (!movies.isEmpty()) {
					MovieDetails movie = movies.get(0);
					updateUI(movie);
				} else {

				}
			}
		});


	}



	private void updateUI(MovieDetails movie) {
		textViewTitleYear.setText(movie.getTitle()+" ("+movie.getReleaseYear()+")");
		textViewReleaseDate.setText(getString(R.string.released) + movie.getReleaseDate());
		if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
			StringBuilder genresBuilder = new StringBuilder();
			List<MovieDetails.Genre> genresList = movie.getGenres();
			for (int i = 0; i < genresList.size(); i++) {
				MovieDetails.Genre genre = genresList.get(i);
				genresBuilder.append(genre.getName());
				if (i < genresList.size() - 1) {
					genresBuilder.append(", ");
				}
			}
			textViewGenres.setText(genresBuilder.toString());
		} else {
			textViewGenres.setText("Brak informacji o gatunkach");
		}
		movieId = movie.getId();
		double voteAverage = movie.getVoteAverage();
		DecimalFormat decimalFormat = new DecimalFormat("#.0");
		String formattedVoteAverage = decimalFormat.format(voteAverage);
		textViewVoteAverage.setText(formattedVoteAverage);
		textViewOverview.setText(movie.getOverview());
		tagline.setText(movie.getTagline());
		String posterPath = movie.getPosterPath();
		if (posterPath != null && !posterPath.isEmpty()) {
			Picasso.get().load("https://image.tmdb.org/t/p/w500" + posterPath)
					.placeholder(R.drawable.placeholder_poster)
					.transform(new RoundedCornersTransformation(50, 0))
					.into(imageViewPoster);
		} else {
			imageViewPoster.setImageResource(R.drawable.placeholder_poster);
		}


		/*String backdropPath = movie.getBackdropPath();
		if (backdropPath != null && !backdropPath.isEmpty()) {
			Picasso.get().load("https://image.tmdb.org/t/p/w500" + backdropPath)
					.placeholder(R.drawable.placeholder_poster)
					.into(new Target() {
						@Override
						public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
							findViewById(R.id.mainLayout).setBackground(new BitmapDrawable(getResources(), bitmap));
						}

						@Override
						public void onBitmapFailed(Exception e, Drawable errorDrawable) {

						}

						@Override
						public void onPrepareLoad(Drawable placeHolderDrawable) {

						}
					});
		} else {

			findViewById(R.id.mainLayout).setBackgroundResource(R.drawable.placeholder_poster);
		}*/
	}
	public class RoundedCornersTransformation implements Transformation {

		private final int radius;
		private final int margin;  // margin is the board in dp

		public RoundedCornersTransformation(int radius, int margin) {
			this.radius = radius;
			this.margin = margin;
		}

		@Override
		public Bitmap transform(Bitmap source) {
			int width = source.getWidth();
			int height = source.getHeight();

			Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(0xff424242);

			canvas.drawRoundRect(margin, margin, width - margin, height - margin, radius, radius, paint);

			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(source, 0, 0, paint);

			if (source != output) {
				source.recycle();
			}

			return output;
		}

		@Override
		public String key() {
			return "rounded_corners";
		}
	}

}
