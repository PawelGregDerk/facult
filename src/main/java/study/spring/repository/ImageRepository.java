package study.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.spring.entity.Image;

import java.util.List;

@Transactional
public interface ImageRepository extends JpaRepository<Image, Integer> {
    @Query("select im from Image im where im.user.id in :ids")
    List<Image> findByUserIds(@Param("ids") Iterable<Integer> ids);
}
