package kata.academy.socialnetwork.model.converter;

import kata.academy.socialnetwork.model.dto.request.comment.CommentPersistRequestDto;
import kata.academy.socialnetwork.model.dto.request.comment.CommentUpdateRequestDto;
import kata.academy.socialnetwork.model.dto.response.comment.CommentResponseDto;
import kata.academy.socialnetwork.model.entity.Comment;
import kata.academy.socialnetwork.model.entity.Post;
import kata.academy.socialnetwork.model.entity.User;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                PostMapper.toDto(comment.getPost()),
                UserMapper.toDto(comment.getUser())
        );
    }

    public static Comment toEntity(User user, Post post, CommentPersistRequestDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.text());
        comment.setPost(post);
        comment.setUser(user);
        return comment;
    }

    public static Comment commentUpdate(Comment comment, CommentUpdateRequestDto dto) {
        comment.setText(dto.text());
        return comment;
    }
}
