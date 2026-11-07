package backend_clima.dev.dto;

import java.time.LocalDateTime;

public record TrendPointResponse(Double temperature, Double feelsLike, Integer humidity, LocalDateTime searchedAt) {
}
