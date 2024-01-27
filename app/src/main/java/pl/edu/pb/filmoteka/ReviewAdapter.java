package pl.edu.pb.filmoteka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.edu.pb.filmoteka.DB.ReviewDao;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

	private List<ReviewDao.ReviewWithAuthor> reviews;
	private Context context;

	public ReviewAdapter(Context context, List<ReviewDao.ReviewWithAuthor> reviews) {
		this.context = context;
		this.reviews = reviews;
	}

	@NonNull
	@Override
	public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.review_layout, parent, false);
		return new ReviewViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
		ReviewDao.ReviewWithAuthor review = reviews.get(position);

		holder.textViewAuthor.setText(review.authorUserName);
		holder.textViewContent.setText(review.content);
	}

	@Override
	public int getItemCount() {
		return reviews.size();
	}

	public static class ReviewViewHolder extends RecyclerView.ViewHolder {
		TextView textViewAuthor;
		TextView textViewContent;

		public ReviewViewHolder(@NonNull View itemView) {
			super(itemView);
			textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
			textViewContent = itemView.findViewById(R.id.textViewContent);
		}
	}
}
