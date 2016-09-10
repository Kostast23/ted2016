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
