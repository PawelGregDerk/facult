package study.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.spring.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Transactional // for to obtain error org.postgresql.util.PSQLException: Большие объекты не могут использоваться в режиме авто-подтверждения (auto-commit)
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u INNER JOIN u.post p WHERE CONCAT(LOWER(u.name), LOWER(u.email)) LIKE %:keyword% AND p.post LIKE %:post% ")
    List<User> search(@Param("keyword") String keyword, @Param("post") String post);
}