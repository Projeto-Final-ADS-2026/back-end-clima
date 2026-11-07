package backend_clima.dev.repository;

import backend_clima.dev.entity.WeatherHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherHistoryRepository extends JpaRepository<WeatherHistory, Long> {

    List<WeatherHistory> findAllByOrderBySearchedAtDesc(Pageable pageable);

    List<WeatherHistory> findByCityNameIgnoreCaseOrderBySearchedAtDesc(String cityName, Pageable pageable);
}
