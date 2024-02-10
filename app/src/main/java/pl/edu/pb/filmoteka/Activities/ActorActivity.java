package pl.edu.pb.filmoteka.Activities;

import static pl.edu.pb.filmoteka.MovieList.getPersonDetails;
import static pl.edu.pb.filmoteka.MovieList.getPersonDetailsEng;
import static pl.edu.pb.filmoteka.MovieList.getPersonMovieCredits;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import pl.edu.pb.filmoteka.Adapters.KnownFromAdapter;
import pl.edu.pb.filmoteka.Models.PersonDetails;
import pl.edu.pb.filmoteka.Models.PersonMovieCredits;
import pl.edu.pb.filmoteka.MovieList;
import pl.edu.pb.filmoteka.R;  // Uwaga: Zastąp to odpowiednim identyfikatorem dla Twojej aplikacji

public class ActorActivity extends AppCompatActivity {

	private ImageView imageViewProfile;

	private TextView textViewDead;
	private TextView textViewName;
	private TextView textViewBirthInfo;
	private TextView textViewDeath;
	private TextView textViewBiography;
	private int actor_id;
	private RecyclerView recyclerViewMovies;
	private KnownFromAdapter knownFromAdapter = new KnownFromAdapter(this, new ArrayList<>());
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_layout);
		getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

		if (getIntent() != null && getIntent().hasExtra("actorId")) {
			actor_id = getIntent().getIntExtra("actorId", 0);
			Log.d("ActorID","ActorID: "+actor_id);
		} else {

			Toast.makeText(this, "Brak informacji o actorId", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		imageViewProfile = findViewById(R.id.imageViewProfile);
		textViewName = findViewById(R.id.textViewName);
		textViewBirthInfo = findViewById(R.id.textViewBirthInfo);
		textViewDeath = findViewById(R.id.textViewDeath);
		textViewBiography = findViewById(R.id.textViewBiography);
		textViewDead = findViewById(R.id.dead);
		recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
		recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
		recyclerViewMovies.setAdapter(knownFromAdapter);
		getPersonDetails("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", actor_id, new MovieList.OnPersonFetchedListener() {
			@Override
			public void onPersonFetched(PersonDetails personDetails) {
				updateViews(personDetails);
			}

			@Override
			public void onFetchError(String errorMessage) {

			}
		});

	}

	private void updateViews(PersonDetails personDetails) {

		if (personDetails != null) {
			getPersonMovieCredits("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", personDetails.getId(), new MovieList.OnMovieCreditsFetchedListener() {
				@Override
				public void onMovieCreditsFetched(PersonMovieCredits personMovieCredits) {
					if (personMovieCredits != null && personMovieCredits.getCast() != null) {
						knownFromAdapter.setCastList(personMovieCredits.getCast());
						knownFromAdapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onFetchError(String errorMessage) {

				}
			});

			Picasso.get()
					.load("https://image.tmdb.org/t/p/w500" + personDetails.getProfilePath())
					.placeholder(R.drawable.placeholder_poster)
					.error(R.drawable.placeholder_poster)
					.transform(new RoundedCornersTransformation(50, 0))
					.into(imageViewProfile);
			textViewName.setText(personDetails.getName());
			textViewBirthInfo.setText(personDetails.getPlaceOfBirth() + ", " + personDetails.getBirthday());
			if(personDetails.getDeathday()==null){
				textViewDead.setText("");
			}
			textViewDeath.setText((personDetails.getDeathday() != null ? personDetails.getDeathday() : ""));
			Log.d("NOZOBACZMY", "PATRZ:"+"'"+personDetails.getBiography()+"'");
			if(personDetails.getBiography().isEmpty()){
				getPersonDetailsEng("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmI1OTA2OTU4ZDY0YjRmOWM1MjMzMzQxNjM3M2Y0YiIsInN1YiI6IjY1OTVhYTFjNTkwN2RlMDE2NzYzYmYwMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IlVmj8Oxv5RunQqXK55LVmJerMote8EMPNsO6jcEdRA", personDetails.getId(), new MovieList.OnPersonFetchedListener() {
					@Override
					public void onPersonFetched(PersonDetails person) {
						textViewBiography.setText(person.getBiography());
					}

					@Override
					public void onFetchError(String errorMessage) {

					}
				});
			}
			else {
				textViewBiography.setText(personDetails.getBiography());
			}
		} else {
			Log.e("ActorActivity", "Person details are null");
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
