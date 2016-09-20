package gr.uoa.di.repo;

import gr.uoa.di.dao.BidEntity;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.ItemPicturesEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ItemPicturesRepository extends PagingAndSortingRepository<ItemPicturesEntity, String> {
    ItemPicturesEntity findOneByUuid(String uuid);
    List<ItemPicturesEntity> findByItem(ItemEntity item);
}
