package pl.edu.pb.filmoteka.Models;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PersonMovieCredits {

	@SerializedName("cast")
	private List<MovieCredit> cast;

	public List<MovieCredit> getCast() {
		return cast;
	}

	public static class MovieCredit {

		@SerializedName("adult")
		private boolean adult;

		@SerializedName("backdrop_path")
		private String backdropPath;

		@SerializedName("genre_ids")
		private List<Integer> genreIds;

		@SerializedName("id")
		private int id;

		@SerializedName("original_language")
		private String originalLanguage;

		@SerializedName("original_title")
		private String originalTitle;

		@SerializedName("overview")
		private String overview;

		@SerializedName("popularity")
		private double popularity;

		@SerializedName("poster_path")
		private String posterPath;

		@SerializedName("release_date")
		private String releaseDate;

		@SerializedName("title")
		private String title;

		@SerializedName("video")
		private boolean video;

		@SerializedName("vote_average")
		private double voteAverage;

		@SerializedName("vote_count")
		private int voteCount;

		@SerializedName("character")
		private String character;

		@SerializedName("credit_id")
		private String creditId;

		@SerializedName("order")
		private int order;

		public boolean isAdult() {
			return adult;
		}

		public String getBackdropPath() {
			return backdropPath;
		}

		public List<Integer> getGenreIds() {
			return genreIds;
		}

		public int getId() {
			return id;
		}

		public String getOriginalLanguage() {
			return originalLanguage;
		}

		public String getOriginalTitle() {
			return originalTitle;
		}

		public String getOverview() {
			return overview;
		}

		public double getPopularity() {
			return popularity;
		}

		public String getPosterPath() {
			return posterPath;
		}

		public String getReleaseDate() {
			return releaseDate;
		}

		public String getTitle() {
			return title;
		}

		public boolean isVideo() {
			return video;
		}

		public double getVoteAverage() {
			return voteAverage;
		}

		public int getVoteCount() {
			return voteCount;
		}

		public String getCharacter() {
			return character;
		}

		public String getCreditId() {
			return creditId;
		}

		public int getOrder() {
			return order;
		}
	}
}
