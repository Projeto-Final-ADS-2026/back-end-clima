package backend_clima.dev.service;

import backend_clima.dev.dto.FavoriteRequest;
import backend_clima.dev.dto.FavoriteResponse;
import backend_clima.dev.entity.City;
import backend_clima.dev.entity.Favorite;
import backend_clima.dev.entity.User;
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
    public List<FavoriteResponse> list(User user) {
        return favoriteRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId()).stream().map(this::toResponse).toList();
    }

    /** Idempotente: favoritar uma cidade já favoritada devolve o favorito existente. */
    @Transactional
    public FavoriteResponse add(User user, FavoriteRequest request) {
        City city = cityService.findOrCreate(request.cityName().trim(), request.country());
        Favorite favorite = favoriteRepository.findByUserIdAndCityId(user.getId(), city.getId())
                .orElseGet(() -> favoriteRepository.save(new Favorite(user, city)));
        return toResponse(favorite);
    }

    @Transactional
    public boolean remove(User user, Long id) {
        return favoriteRepository.findByIdAndUserId(id, user.getId())
                .map(f -> {
                    favoriteRepository.delete(f);
                    return true;
                })
                .orElse(false);
    }

    private FavoriteResponse toResponse(Favorite f) {
        return new FavoriteResponse(f.getId(), f.getCity().getName(), f.getCity().getCountry(), f.getCreatedAt());
    }
}
