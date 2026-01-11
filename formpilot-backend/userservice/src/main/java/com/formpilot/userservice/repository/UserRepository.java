package com.formpilot.userservice.repository;

import com.formpilot.userservice.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email);

    @Nullable Boolean existsByKeycloakId(String userId);

    User findByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email);
}
