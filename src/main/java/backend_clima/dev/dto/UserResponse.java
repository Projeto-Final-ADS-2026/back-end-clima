package backend_clima.dev.dto;

import backend_clima.dev.entity.User;

public record UserResponse(Long id, String name, String email) {
    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }
}
