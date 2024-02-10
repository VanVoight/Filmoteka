package pl.edu.pb.filmoteka.Models;

import java.util.List;

public class PersonDetails {
	private boolean adult;
	private List<String> also_known_as;
	private String biography;
	private String birthday;
	private String deathday;
	private int gender;
	private String homepage;
	private int id;
	private String imdb_id;
	private String known_for_department;
	private String name;
	private String place_of_birth;
	private double popularity;
	private String profile_path;

	public boolean isAdult() {
		return adult;
	}

	public void setAdult(boolean adult) {
		this.adult = adult;
	}

	public List<String> getAlsoKnownAs() {
		return also_known_as;
	}

	public void setAlsoKnownAs(List<String> alsoKnownAs) {
		this.also_known_as = alsoKnownAs;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDeathday() {
		return deathday;
	}

	public void setDeathday(String deathday) {
		this.deathday = deathday;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImdbId() {
		return imdb_id;
	}

	public void setImdbId(String imdbId) {
		this.imdb_id = imdbId;
	}

	public String getKnownForDepartment() {
		return known_for_department;
	}

	public void setKnownForDepartment(String knownForDepartment) {
		this.known_for_department = knownForDepartment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlaceOfBirth() {
		return place_of_birth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.place_of_birth = placeOfBirth;
	}

	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}

	public String getProfilePath() {
		return profile_path;
	}

	public void setProfilePath(String profilePath) {
		this.profile_path = profilePath;
	}
}
