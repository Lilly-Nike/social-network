package kata.academy.socialnetwork.model.dto.request.comment;

import javax.validation.constraints.NotBlank;

public record CommentUpdateRequestDto(
        Long id,
        @NotBlank String text
) {
}
