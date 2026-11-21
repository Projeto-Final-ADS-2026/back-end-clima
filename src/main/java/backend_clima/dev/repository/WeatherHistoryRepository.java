package backend_clima.dev.repository;

import backend_clima.dev.entity.WeatherHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WeatherHistoryRepository extends JpaRepository<WeatherHistory, Long> {

    List<WeatherHistory> findAllByUserIdOrderBySearchedAtDesc(Long userId, Pageable pageable);

    List<WeatherHistory> findByUserIdAndCityNameIgnoreCaseOrderBySearchedAtDesc(Long userId, String cityName, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from WeatherHistory h where h.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
