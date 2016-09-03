package gr.uoa.di.repo;

import gr.uoa.di.dao.ItemEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItemRepository extends PagingAndSortingRepository<ItemEntity, Long> {
}
