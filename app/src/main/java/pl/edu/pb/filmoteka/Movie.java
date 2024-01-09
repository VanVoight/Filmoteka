package pl.edu.pb.filmoteka;
import java.util.List;
public class Movie {
	private boolean adult;
	private String backdrop_path;
	private List<Integer> genre_ids;
	private int id;
	private String original_language;
	private String original_title;
	private String overview;
	private double popularity;
	private String poster_path;
	private String release_date;
	private String title;
	private boolean video;
	private double vote_average;
	private int vote_count;

	public Movie(boolean adult, String backdrop_path, List<Integer> genre_ids, int id,
				 String original_language, String original_title, String overview,
				 double popularity, String poster_path, String release_date, String title,
				 boolean video, double vote_average, int vote_count) {
		this.adult = adult;
		this.backdrop_path = backdrop_path;
		this.genre_ids = genre_ids;
		this.id = id;
		this.original_language = original_language;
		this.original_title = original_title;
		this.overview = overview;
		this.popularity = popularity;
		this.poster_path = poster_path;
		this.release_date = release_date;
		this.title = title;
		this.video = video;
		this.vote_average = vote_average;
		this.vote_count = vote_count;
	}
	public boolean isAdult(){
		return adult;
	}
	public String getBackdropPath() {
		return backdrop_path;
	}

	public List<Integer> getGenreIds() {
		return genre_ids;
	}

	public int getId() {
		return id;
	}

	public String getOriginalLanguage() {
		return original_language;
	}

	public String getOriginalTitle() {
		return original_title;
	}

	public String getOverview() {
		return overview;
	}

	public double getPopularity() {
		return popularity;
	}

	public String getPosterPath() {
		return poster_path;
	}

	public String getReleaseDate() {
		return release_date;
	}

	public String getTitle() {
		return title;
	}

	public boolean isVideo() {
		return video;
	}

	public double getVoteAverage() {
		return vote_average;
	}

	public int getVoteCount() {
		return vote_count;
	}
}
