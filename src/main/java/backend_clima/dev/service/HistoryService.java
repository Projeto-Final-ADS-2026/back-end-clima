package backend_clima.dev.service;

import backend_clima.dev.dto.HistoryItemResponse;
import backend_clima.dev.dto.TrendPointResponse;
import backend_clima.dev.dto.WeatherResponse;
import backend_clima.dev.entity.City;
import backend_clima.dev.entity.User;
import backend_clima.dev.entity.WeatherHistory;
import backend_clima.dev.repository.WeatherHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private static final int MAX_LIMIT = 100;

    private final WeatherHistoryRepository historyRepository;
    private final CityService cityService;

    @Transactional
    public void record(User user, WeatherResponse weather) {
        if (weather == null || weather.getCityName() == null) {
            return;
        }
        City city = cityService.findOrCreate(weather.getCityName(), weather.getCountry());

        WeatherHistory entry = new WeatherHistory();
        entry.setUser(user);
        entry.setCity(city);
        entry.setTemperature(weather.getTemp());
        entry.setFeelsLike(weather.getFeelsLike());
        entry.setHumidity(weather.getHumidity());
        entry.setPressure(weather.getPressure());
        entry.setWindSpeed(weather.getWindSpeed());
        entry.setVisibility(weather.getVisibility());
        entry.setDescription(weather.getDescription());
        historyRepository.save(entry);
    }

    @Transactional(readOnly = true)
    public List<HistoryItemResponse> recent(User user, int limit) {
        return historyRepository.findAllByUserIdOrderBySearchedAtDesc(user.getId(), PageRequest.of(0, clamp(limit))).stream()
                .map(h -> new HistoryItemResponse(
                        h.getId(),
                        h.getCity().getName(),
                        h.getCity().getCountry(),
                        h.getTemperature(),
                        h.getDescription(),
                        h.getSearchedAt()))
                .toList();
    }

    /** Últimas medições da cidade, em ordem cronológica (mais antiga primeiro) para plotar o gráfico. */
    @Transactional(readOnly = true)
    public List<TrendPointResponse> trend(User user, String cityName, int limit) {
        List<TrendPointResponse> points = new ArrayList<>(
                historyRepository.findByUserIdAndCityNameIgnoreCaseOrderBySearchedAtDesc(user.getId(), cityName, PageRequest.of(0, clamp(limit)))
                        .stream()
                        .map(h -> new TrendPointResponse(h.getTemperature(), h.getFeelsLike(), h.getHumidity(), h.getSearchedAt()))
                        .toList());
        Collections.reverse(points);
        return points;
    }

    @Transactional
    public void clear(User user) {
        historyRepository.deleteAllByUserId(user.getId());
    }

    private int clamp(int limit) {
        return Math.min(Math.max(limit, 1), MAX_LIMIT);
    }
}
