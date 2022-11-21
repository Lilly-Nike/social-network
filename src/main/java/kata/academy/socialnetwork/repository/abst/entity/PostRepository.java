package kata.academy.socialnetwork.repository.abst.entity;

import kata.academy.socialnetwork.model.entity.Post;
import kata.academy.socialnetwork.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"user", "tags"})
    @Query("""
            SELECT p
            FROM Post p
            WHERE p.user = :user
            """)
    Page<Post> getPostPageByUserId(@Param("user") User user, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "tags"})
    @Query("""
            SELECT p
            FROM Post p
            WHERE p IN (
                SELECT DISTINCT p FROM Post p JOIN p.tags t WHERE t IN (:tags)
            )
            """)
    List<Post> getPostsByTags(@Param("tags") List<String> tags);
}
