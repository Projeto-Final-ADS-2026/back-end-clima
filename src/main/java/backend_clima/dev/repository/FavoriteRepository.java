package backend_clima.dev.repository;

import backend_clima.dev.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Favorite> findByUserIdAndCityId(Long userId, Long cityId);

    Optional<Favorite> findByIdAndUserId(Long id, Long userId);
}
