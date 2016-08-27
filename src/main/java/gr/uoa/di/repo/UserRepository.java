package gr.uoa.di.repo;

import gr.uoa.di.dao.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findOneById(int userId);
    UserEntity findOneByUsername(String username);

    List<UserEntity> findAll();
    List<UserEntity> findByValidatedFalse();
}
