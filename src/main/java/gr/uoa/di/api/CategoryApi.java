package gr.uoa.di.api;

import gr.uoa.di.dao.CategoryEntity;
import gr.uoa.di.dto.category.CategoryResponseDto;
import gr.uoa.di.mapper.CategoryMapper;
import gr.uoa.di.mapper.ItemMapper;
import gr.uoa.di.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryApi {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ItemMapper itemMapper;

    private CategoryResponseDto getCategoriesRecursive(CategoryEntity catEnt) {
        CategoryResponseDto cat = categoryMapper._mapCategoryEntityToCategoryResponseDto(catEnt, false, false);
        if (catEnt.getSubcategories() != null) {
            cat.setSubcategories(catEnt.getSubcategories().stream().map(this::getCategoriesRecursive).collect(Collectors.toList()));
        }
        return cat;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findByParentCategoryIsNull().stream().map(this::getCategoriesRecursive).collect(Collectors.toList());
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<CategoryResponseDto> getCategories() {
       return categoryRepository.findByParentCategoryIsNull().stream().map(categoryMapper::mapCategoryEntityToCategoryResponseDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{categoryId}")
    public CategoryResponseDto getCategory(@PathVariable Integer categoryId) {
        CategoryEntity category = categoryRepository.findOneById(categoryId);
        if (category == null)
            return null;
        return categoryMapper.mapCategoryEntityToCategoryResponseDto(category);
    }
}
