package gr.uoa.di.repo;

import gr.uoa.di.dao.CategoryEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long> {
    CategoryEntity findOneById(int id);
    CategoryEntity findOneByName(String name);
    List<CategoryEntity> findAll();
}
