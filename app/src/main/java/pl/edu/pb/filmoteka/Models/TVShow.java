package pl.edu.pb.filmoteka.Models;



import java.util.List;

public class TVShow {

	private boolean adult;


	private String backdrop_path;


	private List<Integer> genre_ids;

	private int id;

	private List<String> origin_country;

	private String original_language;

	private String original_name;

	private String overview;

	private double popularity;

	private String poster_path;

	private String first_air_date;

	private String name;

	private double vote_average;

	private int vote_count;

	public boolean isAdult() {
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

	public List<String> getOriginCountry() {
		return origin_country;
	}

	public String getOriginalLanguage() {
		return original_language;
	}

	public String getOriginalName() {
		return original_name;
	}

	public String getOverview() {
		return overview;
	}

	public double getPopularity() {
		return popularity;
	}

	public String getPosterPath() {
		return "https://image.tmdb.org/t/p/w500"+poster_path;
	}

	public String getFirstAirDate() {
		return first_air_date;
	}

	public String getName() {
		return name;
	}

	public double getVoteAverage() {
		return vote_average;
	}

	public int getVoteCount() {
		return vote_count;
	}
}
