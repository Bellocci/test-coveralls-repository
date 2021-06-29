package com.examples.examLibrary.repository;

import java.util.List;

import com.examples.examLibrary.model.Book;
import com.examples.examLibrary.model.Library;

public interface BookRepository {

	public List<Book> getAllBooksOfLibrary(String idLibrary);
	public Book findBookById(String idBook);
	public void saveBookInTheLibrary(Library library, Book newBook);
	public void deleteBookFromLibrary(String idLibrary, String idBook);
}
