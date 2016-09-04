package gr.uoa.di.repo;

import gr.uoa.di.dao.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    CategoryEntity findOneByName(String name);
}
