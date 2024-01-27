package pl.edu.pb.filmoteka.Models;

import java.util.List;

public class MovieCredits {
	private int id;
	private List<CastMember> cast;
	private List<CrewMember> crew;

	// Getters and setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<CastMember> getCast() {
		return cast;
	}

	public void setCast(List<CastMember> cast) {
		this.cast = cast;
	}

	public List<CrewMember> getCrew() {
		return crew;
	}

	public void setCrew(List<CrewMember> crew) {
		this.crew = crew;
	}
}
