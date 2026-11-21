package backend_clima.dev.controller;

import backend_clima.dev.dto.HistoryItemResponse;
import backend_clima.dev.dto.TrendPointResponse;
import backend_clima.dev.service.AuthService;
import backend_clima.dev.service.HistoryService;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/historico")
public class HistoryController {

    private final HistoryService historyService;
    private final AuthService authService;

    @GetMapping
    public List<HistoryItemResponse> recent(Authentication auth, @RequestParam(defaultValue = "10") int limite) {
        return historyService.recent(authService.currentUser(auth.getName()), limite);
    }

    @GetMapping("/tendencia/{city}")
    public List<TrendPointResponse> trend(Authentication auth, @PathVariable String city, @RequestParam(defaultValue = "20") int limite) {
        return historyService.trend(authService.currentUser(auth.getName()), city, limite);
    }

    @DeleteMapping
    public ResponseEntity<Void> clear(Authentication auth) {
        historyService.clear(authService.currentUser(auth.getName()));
        return ResponseEntity.noContent().build();
    }
}
