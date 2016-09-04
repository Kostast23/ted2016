package gr.uoa.di.repo;

import gr.uoa.di.dao.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findOneById(int userId);
    UserEntity findOneByUsername(String username);

    Page<UserEntity> findAll(Pageable pageable);
    Page<UserEntity> findByValidatedFalse(Pageable pageable);
}
