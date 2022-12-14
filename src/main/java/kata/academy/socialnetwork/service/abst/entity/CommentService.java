package kata.academy.socialnetwork.service.abst.entity;


import kata.academy.socialnetwork.model.dto.response.comment.CommentResponseDto;
import kata.academy.socialnetwork.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService extends AbstractService <Comment, Long> {

    Page<Comment> findAll(Pageable pageable);

    Page<CommentResponseDto> getCommentPageByPostId(Long postId, Pageable pageable);

}
