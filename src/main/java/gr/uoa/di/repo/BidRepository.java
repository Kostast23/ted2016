package gr.uoa.di.repo;

import gr.uoa.di.dao.BidEntity;
import gr.uoa.di.dao.BidUserOnItem;
import gr.uoa.di.dao.ItemEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BidRepository extends PagingAndSortingRepository<BidEntity, Long> {
    List<BidEntity> findByItem(ItemEntity item);
    @Query("select new gr.uoa.di.dao.BidUserOnItem(b.owner.id, b.item.id) from BidEntity b")
    List<BidUserOnItem> getUserBidsOnItems();
}
