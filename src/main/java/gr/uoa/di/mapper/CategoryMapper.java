package gr.uoa.di.mapper;

import gr.uoa.di.dao.CategoryEntity;
import gr.uoa.di.dto.category.CategoryResponseDto;
import gr.uoa.di.jax.CategoryJAX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    @Autowired
    ItemMapper itemMapper;

    public CategoryEntity mapCategoryJAXToCategoryEntity(CategoryJAX category) {
        CategoryEntity categoryEnt = new CategoryEntity();
        categoryEnt.setName(category.getvalue());
        return categoryEnt;
    }

    public CategoryJAX mapCategoryEntityToCategoryJAX(CategoryEntity categoryEntity) {
        CategoryJAX category = new CategoryJAX();
        category.setvalue(categoryEntity.getName());
        return category;
    }

    public CategoryResponseDto mapCategoryEntityToCategoryResponseDto(CategoryEntity categoryEntity) {
        return _mapCategoryEntityToCategoryResponseDto(categoryEntity, true);
    }

    public CategoryResponseDto _mapCategoryEntityToCategoryResponseDto(CategoryEntity categoryEntity, boolean getItems) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(categoryEntity.getId());
        categoryResponseDto.setName(categoryEntity.getName());
        categoryResponseDto.setCount(categoryEntity.getItems().size());
        if (categoryEntity.getParentCategory() != null)
            categoryResponseDto.setParent(_mapCategoryEntityToCategoryResponseDto(categoryEntity.getParentCategory(), false));
        if (getItems) {
            categoryResponseDto.setItems(categoryEntity.getItems().stream().map(itemMapper::mapItemEntityToItemResponseDto).collect(Collectors.toList()));
            categoryResponseDto.setSubcategories(categoryEntity.getSubcategories().stream().map(subcat -> _mapCategoryEntityToCategoryResponseDto(subcat, false)).collect(Collectors.toList()));
        }
        return categoryResponseDto;
    }
}
