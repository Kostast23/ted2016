package gr.uoa.di.mapper;

import gr.uoa.di.dao.CategoryEntity;
import gr.uoa.di.jax.CategoryJAX;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
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
}
