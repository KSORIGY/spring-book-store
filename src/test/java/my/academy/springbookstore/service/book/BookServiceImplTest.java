package my.academy.springbookstore.service.book;

import my.academy.springbookstore.dto.book.BookDto;
import my.academy.springbookstore.dto.book.BookDtoWithoutCategoryIds;
import my.academy.springbookstore.dto.book.BookSearchParameters;
import my.academy.springbookstore.dto.book.CreateBookRequestDto;
import my.academy.springbookstore.exception.EntityNotFoundException;
import my.academy.springbookstore.mapper.BookMapper;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.model.Category;
import my.academy.springbookstore.repository.book.BookRepository;
import my.academy.springbookstore.repository.book.BookSpecificationBuilder;
import my.academy.springbookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save valid book to DB")
    void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        List<Long> categoryIds = List.of(1L);

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("My test book");
        requestDto.setCategoryIds(categoryIds);

        Book book = new Book();
        book.setTitle("My test book");

        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");
        List<Category> categories = List.of(category);

        BookDto expectedDto = new BookDto();
        expectedDto.setId(1L);
        expectedDto.setTitle("My test book");
        expectedDto.setCategoryIds(categoryIds);

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(categoryRepository.findAllById(categoryIds)).thenReturn(categories);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto actual = bookService.save(requestDto);

        assertEquals(expectedDto, actual);

        verify(bookMapper, times(1)).toModel(requestDto);
        verify(categoryRepository, times(1)).findAllById(categoryIds);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, categoryRepository);
    }

    @Test
    @DisplayName("Get book by valid ID")
    void findById_ValidId_ReturnsBookDto() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");

        BookDto expectedDto = new BookDto();
        expectedDto.setId(bookId);
        expectedDto.setTitle("Test Book");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto actual = bookService.findById(bookId);

        assertEquals(expectedDto, actual);

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Throw exception when getting book by invalid ID")
    void findById_InvalidId_ThrowsException() {
        Long invalidId = 10L;

        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(invalidId));

        String expectedMessage = "Can`t find book by id: " + invalidId;

        assertEquals(expectedMessage, exception.getMessage());

        verify(bookRepository, times(1)).findById(invalidId);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Get all books with pagination")
    void findAll_ValidPageable_ReturnsBookDtoPage() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> actual = bookService.findAll(pageable);

        assertEquals(1, actual.getTotalElements());
        assertEquals(bookDto, actual.getContent().get(0));

        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("Update book by valid ID")
    void update_ValidId_ReturnsBookDto() {
        Long id = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("New Title");
        requestDto.setCategoryIds(List.of(1L));

        Book bookFromDb = new Book();
        bookFromDb.setId(id);
        bookFromDb.setTitle("Old Title");

        Category category = new Category();
        category.setId(1L);

        BookDto expectedDto = new BookDto();
        expectedDto.setId(id);
        expectedDto.setTitle("New Title");
        expectedDto.setCategoryIds(List.of(1L));

        when(bookRepository.findById(id)).thenReturn(Optional.of(bookFromDb));
        when(categoryRepository.findAllById(requestDto.getCategoryIds())).thenReturn(List.of(category));
        when(bookRepository.save(bookFromDb)).thenReturn(bookFromDb);
        when(bookMapper.toDto(bookFromDb)).thenReturn(expectedDto);

        BookDto actual = bookService.update(id, requestDto);

        assertEquals(expectedDto, actual);

        verify(bookRepository, times(1)).findById(id);
        verify(bookMapper, times(1)).updateBookFromBookDto(requestDto, bookFromDb);
        verify(categoryRepository, times(1)).findAllById(requestDto.getCategoryIds());
        verify(bookRepository, times(1)).save(bookFromDb);
        verify(bookMapper, times(1)).toDto(bookFromDb);
    }

    @Test
    @DisplayName("Delete book by ID")
    void deleteById_ValidId_Success() {
        Long id = 1L;
        bookService.deleteById(id);

        verify(bookRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Search books by parameters with pagination")
    void search_ValidParameters_ReturnsBookDtoPage() {
        BookSearchParameters searchParams = new BookSearchParameters(new String[]{"Title"},
                new String[]{"Author"}, null, null, null);

        Pageable pageable = PageRequest.of(0, 10);

        Book book = new Book();
        BookDto bookDto = new BookDto();
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        Specification<Book> mockSpecification = org.mockito.Mockito.mock(Specification.class);

        when(bookSpecificationBuilder.build(searchParams)).thenReturn(mockSpecification);
        when(bookRepository.findAll(mockSpecification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> actual = bookService.search(searchParams, pageable);

        assertEquals(1, actual.getTotalElements());

        verify(bookSpecificationBuilder, times(1)).build(searchParams);
        verify(bookRepository, times(1)).findAll(mockSpecification, pageable);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("Get books by category ID")
    void getBooksByCategoryId_ValidId_ReturnsBookDtoWithoutCategoryIdsPage() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Book book = new Book();

        BookDtoWithoutCategoryIds expectedDto = new BookDtoWithoutCategoryIds(
                1L, "Title", "Author", "12345678", java.math.BigDecimal.TEN, "Description", "cover.jpg"
        );

        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findByCategoriesId(categoryId, pageable)).thenReturn(bookPage);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(expectedDto);

        Page<BookDtoWithoutCategoryIds> actual = bookService.getBooksByCategoryId(categoryId, pageable);

        assertEquals(1, actual.getTotalElements());

        verify(bookRepository, times(1)).findByCategoriesId(categoryId, pageable);
        verify(bookMapper, times(1)).toDtoWithoutCategories(book);
    }

}
