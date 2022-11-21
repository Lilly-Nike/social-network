package kata.academy.socialnetwork.model.dto.request.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CommentPersistRequestDto(
        @NotNull Long postId,
        @NotBlank String text
) {
}
