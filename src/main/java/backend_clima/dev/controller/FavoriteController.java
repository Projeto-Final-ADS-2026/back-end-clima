package backend_clima.dev.controller;

import backend_clima.dev.dto.FavoriteRequest;
import backend_clima.dev.dto.FavoriteResponse;
import backend_clima.dev.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favoritos")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public List<FavoriteResponse> list() {
        return favoriteService.list();
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> add(@RequestBody FavoriteRequest request) {
        if (request == null || request.cityName() == null || request.cityName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteService.add(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        return favoriteService.remove(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
