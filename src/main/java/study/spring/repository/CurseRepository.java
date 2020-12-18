package study.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.spring.entity.Curse;

import java.util.Optional;
import java.util.Set;

public interface CurseRepository extends JpaRepository<Curse, Integer> {
    Optional<Curse> findByCurseName(String curseName);

    @Query("select c from Curse c join c.users u where u.id = :id")
    Set<Curse> findByUserId(@Param("id") Integer id);

    @Query("select c from Curse c join c.users u where u.id = :id")
    Page<Curse> findByUserId(@Param("id") Integer id, Pageable pageable);
}