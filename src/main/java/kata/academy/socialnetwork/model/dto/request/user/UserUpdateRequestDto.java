package kata.academy.socialnetwork.model.dto.request.user;

import kata.academy.socialnetwork.model.enums.Gender;

import java.time.LocalDate;

public record UserUpdateRequestDto(
        String firstName,
        String lastName,
        String city,
        LocalDate birthdate,
        Gender gender) {
}
