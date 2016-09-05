package gr.uoa.di.repo;

import gr.uoa.di.dao.ItemEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ItemRepository extends PagingAndSortingRepository<ItemEntity, Long> {
    ItemEntity findOneById(int id);
    List<ItemEntity> findAll();
}
