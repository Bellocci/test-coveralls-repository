package com.examples.examLibrary.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.examLibrary.model.Library;
import com.examples.examLibrary.repository.LibraryRepository;
import com.examples.examLibrary.view.LibraryView;

public class LibraryControllerTest {

	@Mock
	private LibraryRepository libraryRepository;
	
	@Mock
	private LibraryView libraryView;
	
	@InjectMocks
	private LibraryController libraryController;
	
	private AutoCloseable closeable;

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}
	
	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}
	
	@Test
	public void testGetAllLibrariesShouldRequestTheLibrariesFromDatabaseAndReturnThemToTheView() {
		// setup
		List<Library> listLibraries = new ArrayList<Library>();	
		when(libraryRepository.getAllLibraries()).thenReturn(listLibraries);
		
		// exercise
		libraryController.getAllLibraries();
		
		// verify
		verify(libraryView).showAllLibraries(listLibraries);
	}
	

	@Test
	public void testNewLibraryWhenItDoesntExistShouldAddToDatabaseAndReturnThemToTheView() {
		// setup
		Library newLibrary = new Library("1", "library1");
		when(libraryRepository.findLibraryById("1")).thenReturn(null);
		
		// exercise
		libraryController.newLibrary(newLibrary);
		
		// verify
		InOrder inOrder = inOrder(libraryRepository, libraryView);
		inOrder.verify(libraryRepository).saveLibrary(newLibrary);
		inOrder.verify(libraryView).libraryAdded(newLibrary);
	}
	
	@Test
	public void testNewLibraryWhenItAlreadyExistShouldNotAddLibraryAndShowErrorToView() {
		// setup
		Library library = new Library("1", "library1");
		when(libraryRepository.findLibraryById("1")).thenReturn(library);
		
		// exercise
		libraryController.newLibrary(library);
		
		// verify
		verify(libraryView).showError("Already existing library with id 1", library);
		verifyNoMoreInteractions(ignoreStubs(libraryRepository));
	}
	
	@Test
	public void testNewLibraryWhenIdIsEmptyShouldThrow() {
		// setup
		Library library = new Library("", "library1");
		
		// exercise & verify
		assertThatThrownBy(() -> libraryController.newLibrary(library))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Id library cannot be empty or only blank space");
		verifyNoMoreInteractions(ignoreStubs(libraryRepository));
	}
	
	@Test
	public void testNewLibraryWhenIdAreOnlyBlankSpaceShouldThrow() {
		// setup
		Library library = new Library("  ", "library1");
		
		// exercise & verify
		assertThatThrownBy(() -> libraryController.newLibrary(library))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Id library cannot be empty or only blank space");
		verifyNoMoreInteractions(ignoreStubs(libraryRepository));
	}
	
	@Test
	public void testDeleteLibraryWhenLibraryExist() {
		// setup
		Library library = new Library("1", "library1");
		when(libraryRepository.findLibraryById("1")).thenReturn(library);
		
		// exercise
		libraryController.deleteLibrary(library);
		
		// verify
		InOrder inOrder = inOrder(libraryRepository, libraryView);
		inOrder.verify(libraryRepository).deleteLibrary("1");
		inOrder.verify(libraryView).libraryRemoved(library);
	}
	
	@Test
	public void testDeleteLibraryWhenLibraryDoesntExist() {
		// setup
		Library library = new Library("1", "library1");
		when(libraryRepository.findLibraryById("1")).thenReturn(null);
		
		// exercise
		libraryController.deleteLibrary(library);
		
		// verify
		verify(libraryView).libraryRemoved(library);
		verify(libraryView).showError("Doesn't exist library with id 1", library);
		verifyNoMoreInteractions(ignoreStubs(libraryRepository));
	}
	
	@Test
	public void testFindLibraryWhenLibraryExist() {
		// setup
		Library library = new Library("1", "library1");
		when(libraryRepository.findLibraryById("1")).thenReturn(library);
		
		// exercise
		libraryController.findLibrary(library);
		
		// verify
		InOrder inOrder = inOrder(libraryRepository, libraryView);
		inOrder.verify(libraryRepository).findLibraryById("1");
		inOrder.verify(libraryView).showAllBooksOfLibrary(library);
	}
	
	@Test
	public void testFindLibraryWhenLibraryDoesntExist() {
		// setup
		Library library = new Library("1", "library1");
		when(libraryRepository.findLibraryById("1")).thenReturn(null);
		
		// exercise
		libraryController.findLibrary(library);
		
		// verify
		InOrder inOrder = inOrder(libraryRepository, libraryView);
		inOrder.verify(libraryRepository).findLibraryById("1");
		inOrder.verify(libraryView).libraryRemoved(library);
		inOrder.verify(libraryView).showError("Doesn't exist library with id 1", library);
		verifyNoMoreInteractions(ignoreStubs(libraryRepository));
	}

}
