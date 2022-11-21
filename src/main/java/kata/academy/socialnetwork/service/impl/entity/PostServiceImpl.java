package kata.academy.socialnetwork.service.impl.entity;

import kata.academy.socialnetwork.model.converter.PostMapper;
import kata.academy.socialnetwork.model.dto.response.post.PostResponseDto;
import kata.academy.socialnetwork.model.entity.Post;
import kata.academy.socialnetwork.model.entity.User;
import kata.academy.socialnetwork.repository.abst.entity.PostRepository;
import kata.academy.socialnetwork.service.abst.entity.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl extends AbstractServiceImpl<Post, Long> implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        super(postRepository);
        this.postRepository = postRepository;
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<PostResponseDto> getPostPageByUser(User user, Pageable pageable) {
        return postRepository.getPostPageByUserId(user, pageable)
                .map(PostMapper::toDto);
    }

    @Override
    public List<PostResponseDto> getPostsByTags(List<String> tags) {
        return sortByNumberMatchesByTags(postRepository.getPostsByTags(tags), tags);
    }

    private List<PostResponseDto> sortByNumberMatchesByTags(List<Post> posts, List<String> tags) {
        Map<Post, Integer> numberOfTagMatchesInPost = new HashMap<>();

        posts.forEach(post ->
                post.getTags().forEach(tag -> {
                    if (tags.contains(tag)) {
                        numberOfTagMatchesInPost.put(
                                post,
                                numberOfTagMatchesInPost.getOrDefault(post, 0) + 1
                        );
                    }
                })
        );

        Comparator<Map.Entry<Post, Integer>> comparator = Map.Entry.comparingByValue();
        return numberOfTagMatchesInPost.entrySet().stream()
                .sorted(comparator.reversed())
                .map(Map.Entry::getKey)
                .map(PostMapper::toDto)
                .toList();
    }
}
