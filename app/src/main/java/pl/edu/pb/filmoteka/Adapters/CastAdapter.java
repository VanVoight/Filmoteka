package pl.edu.pb.filmoteka.Adapters;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso; // Pamiętaj, aby dodać odpowiednią bibliotekę do ładowania obrazów
import com.squareup.picasso.Transformation;

import java.util.List;

import pl.edu.pb.filmoteka.Models.CastMember;
import pl.edu.pb.filmoteka.R;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

	private Context context;
	private List<CastMember> castList;


	public CastAdapter(Context context, List<CastMember> castList) {
		this.context = context;
		this.castList = castList;
	}

	@NonNull
	@Override
	public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actor_layout, parent, false);
		return new CastViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
		CastMember castMember = castList.get(position);


		if (castMember.getProfilePath() != null && !castMember.getProfilePath().isEmpty()) {
			Picasso.get()
					.load("https://image.tmdb.org/t/p/w500" + castMember.getProfilePath())
					.placeholder(R.drawable.placeholder_poster)
					.error(R.drawable.placeholder_poster)
					.transform(new RoundedCornersTransformation(50, 0))
					.into(holder.imageViewProfile);
		} else {
			holder.imageViewProfile.setImageResource(R.drawable.placeholder_poster);
		}


		holder.textViewName.setText(castMember.getName());
		holder.textViewCharacter.setText(castMember.getCharacter());


		holder.textViewName.setText(castMember.getName());


		holder.textViewCharacter.setText(castMember.getCharacter());
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
	@Override
	public int getItemCount() {
		return castList.size();
	}

	public class CastViewHolder extends RecyclerView.ViewHolder {
		ImageView imageViewProfile;
		TextView textViewName;
		TextView textViewCharacter;
		CardView cardView;

		public CastViewHolder(@NonNull View itemView) {
			super(itemView);

			imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
			textViewName = itemView.findViewById(R.id.textViewName);
			textViewCharacter = itemView.findViewById(R.id.textViewCharacter);
			cardView = itemView.findViewById(R.id.cardView);
		}
	}
}
