package backend_clima.dev.service;

import backend_clima.dev.dto.FavoriteRequest;
import backend_clima.dev.dto.FavoriteResponse;
import backend_clima.dev.entity.City;
import backend_clima.dev.entity.Favorite;
import backend_clima.dev.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final CityService cityService;

    @Transactional(readOnly = true)
    public List<FavoriteResponse> list() {
        return favoriteRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toResponse).toList();
    }

    /** Idempotente: favoritar uma cidade já favoritada devolve o favorito existente. */
    @Transactional
    public FavoriteResponse add(FavoriteRequest request) {
        City city = cityService.findOrCreate(request.cityName().trim(), request.country());
        Favorite favorite = favoriteRepository.findByCityId(city.getId())
                .orElseGet(() -> favoriteRepository.save(new Favorite(city)));
        return toResponse(favorite);
    }

    @Transactional
    public boolean remove(Long id) {
        if (!favoriteRepository.existsById(id)) {
            return false;
        }
        favoriteRepository.deleteById(id);
        return true;
    }

    private FavoriteResponse toResponse(Favorite f) {
        return new FavoriteResponse(f.getId(), f.getCity().getName(), f.getCity().getCountry(), f.getCreatedAt());
    }
}
