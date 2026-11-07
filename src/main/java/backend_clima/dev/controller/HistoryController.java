package backend_clima.dev.controller;

import backend_clima.dev.dto.HistoryItemResponse;
import backend_clima.dev.dto.TrendPointResponse;
import backend_clima.dev.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/historico")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public List<HistoryItemResponse> recent(@RequestParam(defaultValue = "10") int limite) {
        return historyService.recent(limite);
    }

    @GetMapping("/tendencia/{city}")
    public List<TrendPointResponse> trend(@PathVariable String city, @RequestParam(defaultValue = "20") int limite) {
        return historyService.trend(city, limite);
    }

    @DeleteMapping
    public ResponseEntity<Void> clear() {
        historyService.clear();
        return ResponseEntity.noContent().build();
    }
}
