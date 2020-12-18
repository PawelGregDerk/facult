package study.spring.service;

import lombok.SneakyThrows;
import study.spring.dto.CurseDTO;
import study.spring.dto.MaterialDTO;
import study.spring.entity.Material;
import study.spring.repository.MaterialRepository;

import java.io.File;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public interface MaterialService extends Service<Integer, Material, MaterialRepository> {
    MaterialDTO getMaterialDTO(Integer id);

    CurseDTO showUserMaterials(Integer cId, Integer uId);

    default Set<String> getMaterials(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(File::isFile).map(File::getName).collect(toSet());
    }
}
