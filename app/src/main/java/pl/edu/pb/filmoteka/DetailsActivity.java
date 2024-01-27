package pl.edu.pb.filmoteka;

import static pl.edu.pb.filmoteka.MovieList.getMovieCredits;
import static pl.edu.pb.filmoteka.MovieList.getMovieDetails;
import static pl.edu.pb.filmoteka.MovieList.getMovieVideos;
import static pl.edu.pb.filmoteka.MovieList.getRecommendationsForMovie;


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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import pl.edu.pb.filmoteka.DB.AppDatabase;
import pl.edu.pb.filmoteka.DB.ReviewDao;


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
	private ImageView backdrop;
	private int movieId;
	private long userRole;
	private List<Movie> movieList;
	private List<ReviewDao.ReviewWithAuthor> reviewList;
	private RecyclerView recyclerViewcast;

	private RecyclerView recyclerViewReviews;
	private RecyclerView recyclerViewrecommend;
	private MovieCredits movieCredits;
	private AppDatabase appDatabase;
	private long userId;
	private String key;
	private MovieDetails movieDetails;
	private MovieAdapter movieAdapter;
	private RatingBar ratingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.details_layout);
		Configuration configuration = getResources().getConfiguration();

		if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.e("szczegóły","siema ustawiam pionową");
			// Bieżąca orientacja to pionowa
			setContentView(R.layout.details_layout);
		} else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Bieżąca orientacja to pozioma
			Log.e("szczegóły","siema ustawiam poziomą");
			setContentView(R.layout.details_layout_land);
		}

		appDatabase = AppDatabase.getInstance(this);
		if (getIntent() != null && getIntent().hasExtra("movieId")) {
			movieId = getIntent().getIntExtra("movieId", 0);
			userId = getIntent().getLongExtra("userId", 0);
			userRole = getIntent().getLongExtra("roleId",1);
		} else {

			Toast.makeText(this, "Brak informacji o movieId", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		appDatabase = AppDatabase.getInstance(this);
		imageViewPoster = findViewById(R.id.imageViewPoster);
		textViewTitleYear = findViewById(R.id.textViewTitleYear);
		textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
		ratingBar = findViewById(R.id.ratingBar);
		textViewGenres = findViewById(R.id.textViewGenres);
		textViewVoteAverage = findViewById(R.id.textViewVoteAverage);
		recyclerViewrecommend = findViewById(R.id.recyclerViewrecommendations);
		recyclerViewReviews = findViewById(R.id.recyclerViewreviews);
		backdrop = findViewById(R.id.imageBackdrop);
		textViewOverview = findViewById(R.id.textViewOverview);
		tagline = findViewById(R.id.TagLine);
		cardView = findViewById(R.id.cardView);
		imageButton = findViewById(R.id.imageButtonPlayTrailer);
		ytTextView = findViewById(R.id.ytTextView);
		recyclerViewcast = findViewById(R.id.recyclerView);
		new FetchAverageRatingTask().execute(movieId);
		cardView.setOnClickListener(view -> {
			if (key != null && !key.isEmpty()) {

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + key));
				startActivity(intent);
			} else {

				Toast.makeText(this, "No video available", Toast.LENGTH_SHORT).show();
			}
		});



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
		getMovieVideos(accessToken, movieId, new MovieList.OnVideosFetchedListener() {
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
			getMovieCredits(accessToken, movieId, new MovieList.OnCreditsFetchedListener() {
				@Override
				public void onCreditsFetched(MovieCredits credits) {
					if (credits != null) {
						movieCredits = credits;
						updateCastUI(movieCredits);
					} else {

					}
				}
			});
			movieAdapter = new MovieAdapter(userId,appDatabase,userRole);
			recyclerViewrecommend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
			recyclerViewrecommend.setAdapter(movieAdapter);
			getRecommendationsForMovie(accessToken,movieId, new MovieList.OnMoviesFetchedListener() {
				@Override
				public void onMoviesFetched(List<Movie> movies) {
					movieList = movies;
					movieAdapter.setMovies(movieList);
					movieAdapter.notifyDataSetChanged();
				}
			});
		new FetchReviewsTask().execute(movieId);
	}

	private class FetchReviewsTask extends AsyncTask<Integer, Void, List<ReviewDao.ReviewWithAuthor>> {

		@Override
		protected List<ReviewDao.ReviewWithAuthor> doInBackground(Integer... params) {
			int movieId = params[0];
			return appDatabase.reviewDao().getReviewsWithAuthorByMovieId(movieId);
		}

		@Override
		protected void onPostExecute(List<ReviewDao.ReviewWithAuthor> reviews) {
			if (reviews != null && !reviews.isEmpty()) {
				ReviewAdapter reviewAdapter = new ReviewAdapter(DetailsActivity.this, reviews);
				recyclerViewReviews.setLayoutManager(new LinearLayoutManager(DetailsActivity.this,LinearLayoutManager.HORIZONTAL, false));
				recyclerViewReviews.setAdapter(reviewAdapter);
			} else {
				 Toast.makeText(DetailsActivity.this, "Brak recenzji", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class FetchAverageRatingTask extends AsyncTask<Integer, Void, Float> {

		@Override
		protected Float doInBackground(Integer... params) {
			int movieId = params[0];
			return appDatabase.ratingDao().getAverageRatingForMovie(movieId);
		}

		@Override
		protected void onPostExecute(Float averageRating) {

			if (averageRating != null) {
				ratingBar.setRating(averageRating.floatValue());
			}
		}
	}


	private void updateCastUI(MovieCredits movieCredits) {

		if (recyclerViewcast != null && movieCredits != null) {
			CastAdapter castAdapter = new CastAdapter(this, movieCredits.getCast());
			recyclerViewcast.setAdapter(castAdapter);
		}
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


		String backdropPath = movie.getBackdropPath();
		if (backdropPath != null && !backdropPath.isEmpty()) {
			Picasso.get().load("https://image.tmdb.org/t/p/w500" + backdropPath)
					.placeholder(R.drawable.placeholder_poster)
					.into(backdrop);

		} else {

			findViewById(R.id.mainLayout).setBackgroundResource(R.drawable.placeholder_poster);
		}
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
