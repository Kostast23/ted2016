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

    public CategoryResponseDto mapCategoryEntityToCategoryResponseDto(CategoryEntity categoryEntity, boolean getParent, int subCategoriesDepth) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(categoryEntity.getId());
        categoryResponseDto.setName(categoryEntity.getName());
        categoryResponseDto.setCount(categoryEntity.getItems().size());
        if (getParent && categoryEntity.getParentCategory() != null) {
            categoryResponseDto.setParent(mapCategoryEntityToCategoryResponseDto(categoryEntity.getParentCategory(), true, 0));
        }
        if (subCategoriesDepth > 0) {
            categoryResponseDto.setSubcategories(categoryEntity.getSubcategories().stream().map(subcat
                    -> mapCategoryEntityToCategoryResponseDto(subcat, false, subCategoriesDepth - 1)).collect(Collectors.toList()));
        }
        return categoryResponseDto;
    }
}
