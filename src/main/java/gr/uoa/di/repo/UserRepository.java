package gr.uoa.di.repo;

import gr.uoa.di.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDto, Long> {
    UserDto findOneByUsername(String username);
}
