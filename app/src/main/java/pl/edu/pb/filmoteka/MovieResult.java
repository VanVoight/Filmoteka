package pl.edu.pb.filmoteka;
import java.util.List;
public class MovieResult {
	private int page;
	private List<Movie> results;
	private int total_pages;
	private int total_results;

	public MovieResult(int page, List<Movie> results, int total_pages, int total_results) {
		this.page = page;
		this.results = results;
		this.total_pages = total_pages;
		this.total_results = total_results;
	}
	public int getPage() {
		return page;
	}

	public List<Movie> getResults() {
		return results;
	}

	public int getTotalPages() {
		return total_pages;
	}

	public int getTotalResults() {
		return total_results;
	}
}
