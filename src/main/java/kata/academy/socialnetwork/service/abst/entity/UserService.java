package kata.academy.socialnetwork.service.abst.entity;

import kata.academy.socialnetwork.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService extends AbstractService<User, Long> {

    User updatePassword(User user);

    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);

    User findByEmail(String email);
}
