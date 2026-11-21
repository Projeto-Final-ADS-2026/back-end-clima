package backend_clima.dev.service;

import backend_clima.dev.dto.AuthResponse;
import backend_clima.dev.dto.LoginRequest;
import backend_clima.dev.dto.RegisterRequest;
import backend_clima.dev.dto.UserResponse;
import backend_clima.dev.entity.User;
import backend_clima.dev.repository.UserRepository;
import backend_clima.dev.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public AuthResponse register(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }
        User user = users.save(new User(req.name().trim(), email, encoder.encode(req.password())));
        return new AuthResponse(jwt.generateToken(user.getEmail()), UserResponse.from(user));
    }

    public AuthResponse login(LoginRequest req) {
        User user = users.findByEmailIgnoreCase(req.email().trim())
                .filter(u -> encoder.matches(req.password(), u.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas"));
        return new AuthResponse(jwt.generateToken(user.getEmail()), UserResponse.from(user));
    }

    public User currentUser(String email) {
        return users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao encontrado"));
    }
}
