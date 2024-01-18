package pl.edu.pb.filmoteka;
import java.util.List;
public class MovieResult {

	private List<Movie> results;


	public MovieResult( List<Movie> results) {

		this.results = results;

	}

	public List<Movie> getResults() {
		return results;
	}

}
