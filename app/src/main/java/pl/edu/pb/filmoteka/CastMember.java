package pl.edu.pb.filmoteka;

public class CastMember {
	private boolean adult;
	private int gender;
	private int id;
	private String knownForDepartment;
	private String name;
	private String originalName;
	private double popularity;
	private String profile_path;
	private int castId;
	private String character;
	private String creditId;
	private int order;

	public boolean isAdult() {
		return adult;
	}

	public int getGender() {
		return gender;
	}

	public int getId() {
		return id;
	}

	public String getKnownForDepartment() {
		return knownForDepartment;
	}

	public String getName() {
		return name;
	}

	public String getOriginalName() {
		return originalName;
	}

	public double getPopularity() {
		return popularity;
	}

	public String getProfilePath() {
		return profile_path;
	}

	public int getCastId() {
		return castId;
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