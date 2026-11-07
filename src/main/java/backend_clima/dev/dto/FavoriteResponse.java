package backend_clima.dev.dto;

import java.time.LocalDateTime;

public record FavoriteResponse(Long id, String cityName, String country, LocalDateTime createdAt) {
}
