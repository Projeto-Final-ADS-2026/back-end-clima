package backend_clima.dev.controller;

import backend_clima.dev.dto.WeatherResponse;
import backend_clima.dev.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/clima")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/{city}")
    public ResponseEntity<WeatherResponse> getClimaByCity(@PathVariable String city){

        try {
            WeatherResponse clima = weatherService.getWeatherByCity(city);
            return ResponseEntity.ok(clima);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
