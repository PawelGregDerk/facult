package study.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring.entity.Material;

public interface MaterialRepository extends JpaRepository<Material, Integer> {

}
