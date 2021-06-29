package com.examples.examLibrary.view;

import java.util.List;

import com.examples.examLibrary.model.Library;



public interface LibraryView {
	public void showAllLibraries(List<Library> libraries);
	public void libraryAdded(Library library);
	public void libraryRemoved(Library library);
	public void showError(String errorMessage, Library library);
	public void showAllBooksOfLibrary(Library library);
}
