package kata.academy.socialnetwork.model.dto.response.user;

import kata.academy.socialnetwork.model.enums.Gender;

import java.time.LocalDate;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String city,
        LocalDate birthdate,
        Gender gender) {
}
