package backend_clima.dev.repository;

import backend_clima.dev.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByNameIgnoreCaseAndCountryIgnoreCase(String name, String country);
}
