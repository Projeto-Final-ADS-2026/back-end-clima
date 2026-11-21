package backend_clima.dev.controller;

import backend_clima.dev.dto.FavoriteRequest;
import backend_clima.dev.dto.FavoriteResponse;
import backend_clima.dev.service.AuthService;
import backend_clima.dev.service.FavoriteService;
import org.springframework.security.core.Authentication;
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
    private final AuthService authService;

    @GetMapping
    public List<FavoriteResponse> list(Authentication auth) {
        return favoriteService.list(authService.currentUser(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> add(Authentication auth, @RequestBody FavoriteRequest request) {
        if (request == null || request.cityName() == null || request.cityName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteService.add(authService.currentUser(auth.getName()), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(Authentication auth, @PathVariable Long id) {
        return favoriteService.remove(authService.currentUser(auth.getName()), id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
