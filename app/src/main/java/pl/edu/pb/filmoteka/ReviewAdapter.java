package pl.edu.pb.filmoteka;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
		holder.itemView.setOnClickListener(view -> showReviewDialog(review));
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
	private void showReviewDialog(ReviewDao.ReviewWithAuthor review) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.CustomAlertDialog);
		builder.setTitle("Review by " + review.authorUserName);
		String message = review.content;
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.gold));
		SpannableString spannableString = new SpannableString(message);
		spannableString.setSpan(colorSpan, 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setMessage(spannableString);
		builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
		builder.show();
	}
}
