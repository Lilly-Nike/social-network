package kata.academy.socialnetwork.service.abst.entity;

import kata.academy.socialnetwork.model.dto.response.post.PostResponseDto;
import kata.academy.socialnetwork.model.entity.Post;
import kata.academy.socialnetwork.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService extends AbstractService<Post, Long> {

    Page<Post> findAll(Pageable pageable);

    Page<PostResponseDto> getPostPageByUser(User user, Pageable pageable);

    List<PostResponseDto> getPostsByTags(List<String> tags);
}
