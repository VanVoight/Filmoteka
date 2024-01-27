package pl.edu.pb.filmoteka.Models;

import java.util.List;

public class MovieDetails {
	private boolean adult;
	private String backdrop_path;
	private Object belongsToCollection;
	private int budget;
	private List<Genre> genres;
	private String homepage;
	private int id;
	private String imdbId;
	private String originalLanguage;
	private String originalTitle;
	private String overview;
	private double popularity;
	private String poster_path;
	private List<ProductionCompany> productionCompanies;
	private List<ProductionCountry> productionCountries;
	private String release_date;
	private int revenue;
	private int runtime;
	private List<SpokenLanguage> spokenLanguages;
	private String status;
	private String tagline;
	private String title;
	private boolean video;
	private double vote_average;
	private int voteCount;



	public static class Genre {
		private int id;
		private String name;
		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	public static class ProductionCompany {
		private int id;
		private String logoPath;
		private String name;
		private String originCountry;


	}

	public static class ProductionCountry {
		private String iso31661;
		private String name;


	}

	public static class SpokenLanguage {
		private String englishName;
		private String iso6391;
		private String name;


	}
	public String getReleaseYear() {
		if (release_date != null && release_date.length() >= 4) {
			return release_date.substring(0, 4);
		} else {
			return "Data niedostępna";
		}
	}
	public boolean isAdult() {
		return adult;
	}

	public String getBackdropPath() {
		return backdrop_path;
	}

	public Object getBelongsToCollection() {
		return belongsToCollection;
	}

	public int getBudget() {
		return budget;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public String getHomepage() {
		return homepage;
	}

	public int getId() {
		return id;
	}

	public String getImdbId() {
		return imdbId;
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
		return poster_path;
	}

	public List<ProductionCompany> getProductionCompanies() {
		return productionCompanies;
	}

	public List<ProductionCountry> getProductionCountries() {
		return productionCountries;
	}

	public String getReleaseDate() {
		return release_date;
	}

	public int getRevenue() {
		return revenue;
	}

	public int getRuntime() {
		return runtime;
	}

	public List<SpokenLanguage> getSpokenLanguages() {
		return spokenLanguages;
	}

	public String getStatus() {
		return status;
	}

	public String getTagline() {
		return tagline;
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
		return voteCount;
	}
}

