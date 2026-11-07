package backend_clima.dev.service;

import backend_clima.dev.entity.City;
import backend_clima.dev.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    /** Retorna a cidade existente (nome + país, sem diferenciar maiúsculas) ou cria uma nova. */
    public City findOrCreate(String name, String country) {
        String safeCountry = (country == null || country.isBlank()) ? "--" : country;
        return cityRepository
                .findByNameIgnoreCaseAndCountryIgnoreCase(name, safeCountry)
                .orElseGet(() -> cityRepository.save(new City(name, safeCountry)));
    }
}
