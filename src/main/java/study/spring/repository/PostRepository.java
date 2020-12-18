package study.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import study.spring.entity.Post;

import java.util.Optional;

@Transactional // for to obtain error org.postgresql.util.PSQLException: Большие объекты не могут использоваться в режиме авто-подтверждения (auto-commit)
public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findByPost(String post);
}
