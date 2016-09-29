package gr.uoa.di.repo;

import gr.uoa.di.dao.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends PagingAndSortingRepository<MessageEntity, Long> {
    MessageEntity findById(int id);
    Page<MessageEntity> findByFrom_UsernameAndDeletedsenderFalseOrderBySentdateDesc(String username, Pageable pageable);
    Page<MessageEntity> findByTo_UsernameAndDeletedreceiverFalseOrderBySentdateDesc(String username, Pageable pageable);
}
