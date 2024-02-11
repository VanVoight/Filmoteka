package pl.edu.pb.filmoteka.Models;

import java.util.List;

public class TVShowResult {

	private int page;


	private List<TVShow> results;


	private int total_pages;


	private int total_results;

	public int getPage() {
		return page;
	}

	public List<TVShow> getResults() {
		return results;
	}

	public int getTotalPages() {
		return total_pages;
	}

	public int getTotalResults() {
		return total_results;
	}
}
