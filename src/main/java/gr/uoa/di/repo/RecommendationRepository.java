package gr.uoa.di.repo;

import gr.uoa.di.dao.RecommendationEntity;
import gr.uoa.di.dao.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecommendationRepository extends CrudRepository<RecommendationEntity, Long> {
    List<RecommendationEntity> findByUser(UserEntity user);
}
