package com.example.bookstore;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Unit tests for BookService using Mockito mocks to isolate behavior from repository storage.
 *
 * Mockito core concepts shown here:
 * - mock(): creates a test double (fake object) for BookRepository.
 * - when(...).thenReturn(...): stubs repository methods to return controlled values.
 * - verify(...): asserts interactions (calls) with the mock repository.
 *
 * This keeps tests fast, deterministic, and focused on the service logic without real DB or state.
 */
class BookServiceTest {

    private BookRepository repository;
    private BookService service;

    @BeforeEach
    void setUp() {
        // Create a Mockito mock for the dependency
        repository = mock(BookRepository.class);
        // Inject the mock into the class under test
        service = new BookService(repository);
    }

    @Test
    @DisplayName("Should Add Book Successfully")
    void testAddBook() {
        Book book = new Book("1", "JUnit 5 in Action", "John Doe");

        // Exercise service behavior
        service.addBook(book);

        // Verify service called repository.save once with the correct book
        verify(repository, times(1)).save(book);
    }

    @Test
    @DisplayName("Should Retrieve Book By ID")
    void testGetBookById() {
        Book book = new Book("1", "JUnit 5 in Action", "John Doe");
        // Stub repository.findById to return the book for id "1"
        when(repository.findById("1")).thenReturn(Optional.of(book));

        Book result = service.getBookById("1");

        assertNotNull(result);
        assertEquals("JUnit 5 in Action", result.getTitle());

        // Optionally assert repository interaction (implicit in service logic)
        verify(repository).findById("1");
    }

    @Test
    @DisplayName("Should Throw Exception When Book Not Found")
    void testGetBookById_NotFound() {
        // Stub repository.findById for a missing ID
        when(repository.findById("999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.getBookById("999");
        });

        assertEquals("Book not found", exception.getMessage());

        // Verify repository method was called during service handling
        verify(repository).findById("999");
    }

    @Test
    @DisplayName("Should Retrieve All Books")
    void testGetAllBooks() {
        Book book1 = new Book("1", "JUnit 5 in Action", "John Doe");
        Book book2 = new Book("2", "Spring Boot", "Jane Smith");
        List<Book> books = Arrays.asList(book1, book2);

        // Stub repository.findAll to return a predefined list
        when(repository.findAll()).thenReturn(books);

        List<Book> result = service.getAllBooks();

        assertEquals(2, result.size());
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));

        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should Delete Book")
    void testDeleteBook() {
        // No stubbing needed because delete() returns void; just verify call
        service.deleteBook("1");

        verify(repository, times(1)).delete("1");
    }

    
     
    /* // Equivalent test code without using mocks
    @BeforeEach
    void setUpWithoutMock() {
        repository = new BookRepository(); // Use actual implementation
        service = new BookService(repository);
    }

    @Test
    @DisplayName("Should Add Book Successfully Without Mock")
    void testAddBookWithoutMock() {
        Book book = new Book("1", "JUnit 5 in Action", "John Doe");
        service.addBook(book);
        assertEquals(book, repository.findById("1").orElse(null));
    }

    @Test
    @DisplayName("Should Retrieve Book By ID Without Mock")
    void testGetBookByIdWithoutMock() {
        Book book = new Book("1", "JUnit 5 in Action", "John Doe");
        repository.save(book);
        Book result = service.getBookById("1");
        assertEquals("JUnit 5 in Action", result.getTitle());
    }

    @Test
    @DisplayName("Should Throw Exception When Book Not Found Without Mock")
    void testGetBookById_NotFoundWithoutMock() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.getBookById("999");
        });
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should Retrieve All Books Without Mock")
    void testGetAllBooksWithoutMock() {
        Book book1 = new Book("1", "JUnit 5 in Action", "John Doe");
        Book book2 = new Book("2", "Spring Boot", "Jane Smith");
        repository.save(book1);
        repository.save(book2);
        List<Book> result = service.getAllBooks();
        assertEquals(2, result.size());
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));
    }

    @Test
    @DisplayName("Should Delete Book Without Mock")
    void testDeleteBookWithoutMock() {
        Book book = new Book("1", "JUnit 5 in Action", "John Doe");
        repository.save(book);
        service.deleteBook("1");
        assertFalse(repository.findById("1").isPresent());
    } */


    
}
