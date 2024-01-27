package pl.edu.pb.filmoteka.Models;

import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
	private int currentIndex;
	private int currentCategoryId;
	private String currentCategoryName;

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getCurrentCategoryId() {
		return currentCategoryId;
	}

	public void setCurrentCategoryId(int currentCategoryId) {
		this.currentCategoryId = currentCategoryId;
	}

	public String getCurrentCategoryName() {
		return currentCategoryName;
	}

	public void setCurrentCategoryName(String currentCategoryName) {
		this.currentCategoryName = currentCategoryName;
	}
}
