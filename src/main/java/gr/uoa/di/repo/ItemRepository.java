package gr.uoa.di.repo;

import gr.uoa.di.dao.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface ItemRepository extends PagingAndSortingRepository<ItemEntity, Long>, QueryDslPredicateExecutor<ItemEntity> {
    ItemEntity findOneById(int id);
    List<ItemEntity> findAll();
    Page<ItemEntity> findByCategory_IdOrderByFinishedAscStartDateDesc(int id, Pageable pageable);
    List<ItemEntity> findByOwner_Username(String username);
    List<ItemEntity> findByFinishedIsFalseAndEndDateLessThan(Date endDate);
    List<ItemEntity> findByFinishedIsTrue();
}
