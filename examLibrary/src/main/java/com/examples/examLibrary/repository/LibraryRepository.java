package com.examples.examLibrary.repository;

import java.util.List;

import com.examples.examLibrary.model.Library;

public interface LibraryRepository {

	public List<Library> getAllLibraries();
	public Library findLibraryById(String idLibrary);
	public void saveLibrary(Library library);
	public void deleteLibrary(String idLibrary);
}
