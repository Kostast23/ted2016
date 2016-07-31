package gr.uoa.di.repo;

import gr.uoa.di.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDao, Long> {
    UserDao findOneByUsername(String username);
}
