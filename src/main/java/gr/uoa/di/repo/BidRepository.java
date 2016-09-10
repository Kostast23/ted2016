package gr.uoa.di.repo;

import gr.uoa.di.dao.BidEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BidRepository extends PagingAndSortingRepository<BidEntity, Long> {
}
