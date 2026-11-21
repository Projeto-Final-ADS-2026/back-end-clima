package backend_clima.dev.controller;

import backend_clima.dev.dto.WeatherResponse;
import backend_clima.dev.service.AuthService;
import backend_clima.dev.service.HistoryService;
import org.springframework.security.core.Authentication;
import backend_clima.dev.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clima")
public class WeatherController {

    private final WeatherService weatherService;
    private final HistoryService historyService;
    private final AuthService authService;

    @GetMapping("/{city}")
    public ResponseEntity<WeatherResponse> getClimaByCity(Authentication auth, @PathVariable String city){

        try {
            WeatherResponse clima = weatherService.getWeatherByCity(city);
            try {
                historyService.record(authService.currentUser(auth.getName()), clima);
            } catch (RuntimeException e) {
                // falha ao gravar o histórico não deve impedir a consulta do clima
                System.err.println("Erro ao gravar histórico: " + e.getMessage());
            }
            return ResponseEntity.ok(clima);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
