package backend_clima.dev.dto;

import java.time.LocalDateTime;

public record HistoryItemResponse(
        Long id,
        String cityName,
        String country,
        Double temperature,
        String description,
        LocalDateTime searchedAt) {
}
