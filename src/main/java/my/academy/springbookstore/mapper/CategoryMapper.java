package my.academy.springbookstore.mapper;

import my.academy.springbookstore.dto.category.CategoryDto;
import my.academy.springbookstore.dto.category.CreateCategoryRequestDto;
import my.academy.springbookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto createCategoryRequestDto);

    void updateCategoryFromCategoryDto(CreateCategoryRequestDto createCategoryRequestDto,
                                       @MappingTarget Category category);
}
