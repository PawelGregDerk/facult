package study.spring.service.impl;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.spring.dto.CurseDTO;
import study.spring.dto.MaterialDTO;
import study.spring.entity.Curse;
import study.spring.entity.Material;
import study.spring.exception.NotExistsException;
import study.spring.repository.CurseRepository;
import study.spring.repository.MaterialRepository;
import study.spring.service.MaterialService;
import study.spring.service.mapper.CurseDTOMapper;
import study.spring.service.mapper.MaterialDTOMapper;

import static java.util.stream.Collectors.toSet;

@Service
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    private MaterialRepository matRepo;
    @Autowired
    private CurseRepository curseRepo;
    @Autowired
    private MaterialDTOMapper mMapper;
    @Autowired
    private CurseDTOMapper cMapper;

    @Override
    public MaterialRepository getRepository() {
        return matRepo;
    }

    @Override
    @SneakyThrows
    public MaterialDTO getMaterialDTO(Integer id) {
        Material m = matRepo.findById(id).orElseThrow(NotExistsException::new);
        return mMapper.toDTO(m);
    }

    @Override
    @SneakyThrows
    public CurseDTO showUserMaterials(Integer cId, Integer uId) {
        Curse curse = curseRepo.findById(cId).orElseThrow(NotExistsException::new);
        val usersMats = curse.getMaterials().stream()
                .filter(m -> m.getPath().split(",")[1].equals(uId.toString()))
                .map(mMapper::toDTO).collect(toSet());
        CurseDTO curseDTO = cMapper.toDTO(curse);
        curseDTO.setMaterials(usersMats);
        return curseDTO;
    }
}
