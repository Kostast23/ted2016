package gr.uoa.di.repo;

import gr.uoa.di.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserDto, Long> {
    List<UserDto> findByUsername(String username);
}
