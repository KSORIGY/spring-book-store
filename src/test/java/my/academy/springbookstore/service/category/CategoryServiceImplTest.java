package my.academy.springbookstore.service.category;

import my.academy.springbookstore.dto.category.CategoryDto;
import my.academy.springbookstore.dto.category.CreateCategoryRequestDto;
import my.academy.springbookstore.exception.EntityNotFoundException;
import my.academy.springbookstore.mapper.CategoryMapper;
import my.academy.springbookstore.model.Category;
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
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save category to DB")
    void save_ValidCreateCategoryRequestDto_ReturnsCategoryDto() {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto();
        createCategoryRequestDto.setName("Horror");

        Category category = new Category();
        category.setName("Horror");

        CategoryDto categoryDtoExpected = new CategoryDto();
        categoryDtoExpected.setId(1L);
        categoryDtoExpected.setName("Horror");

        when(categoryMapper.toModel(createCategoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDtoExpected);

        CategoryDto categoryDtoActual = categoryService.save(createCategoryRequestDto);

        assertEquals(categoryDtoExpected, categoryDtoActual);

        verify(categoryRepository, times(1)).save(category);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Get category by valid ID")
    void getById_ValidId_ReturnsCategoryDto() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Horror");

        CategoryDto categoryDtoExpected = new CategoryDto();
        categoryDtoExpected.setId(categoryId);
        categoryDtoExpected.setName("Horror");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDtoExpected);

        CategoryDto categoryDtoActual = categoryService.getById(categoryId);

        assertEquals(categoryDtoExpected, categoryDtoActual);

        verify(categoryRepository, times(1)).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Get category by invalid ID throws exception")
    void getById_InvalidId_ReturnsException() {
        Long invalidId = 10L;

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(invalidId));

        String expectedErrorMessage = "Can`t find category by id: " + invalidId;
        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(categoryRepository, times(1)).findById(invalidId);
        verifyNoMoreInteractions(categoryMapper);
    }

    @Test
    @DisplayName("Find all categories with pagination")
    void findAll_ValidPageable_ReturnsCategoryDtoPage() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Horror");

        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> actual = categoryService.findAll(pageable);

        assertEquals(1, actual.getTotalElements());
        assertEquals(categoryDto, actual.getContent().get(0));

        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    @DisplayName("Update category by valid ID")
    void update_ValidId_ReturnsCategoryDto() {
        Long id = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("New History");

        Category categoryFromDb = new Category();
        categoryFromDb.setId(id);
        categoryFromDb.setName("Old History");

        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(id);
        expectedDto.setName("New History");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(categoryFromDb));
        when(categoryRepository.save(categoryFromDb)).thenReturn(categoryFromDb);
        when(categoryMapper.toDto(categoryFromDb)).thenReturn(expectedDto);

        CategoryDto actual = categoryService.update(id, requestDto);

        assertEquals(expectedDto, actual);

        verify(categoryRepository, times(1)).findById(id);
        verify(categoryMapper, times(1)).updateCategoryFromCategoryDto(requestDto, categoryFromDb);
        verify(categoryRepository, times(1)).save(categoryFromDb);
        verify(categoryMapper, times(1)).toDto(categoryFromDb);
    }

    @Test
    @DisplayName("Delete category by ID")
    void deleteById_ValidId_Success() {
        Long id = 1L;

        categoryService.deleteById(id);

        verify(categoryRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(categoryRepository);
    }

}
