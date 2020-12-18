package study.spring.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import study.spring.dto.MaterialDTO;
import study.spring.entity.Curse;
import study.spring.entity.Material;

@Component
public class MaterialDTOMapper extends EntityDTOMapper<MaterialDTO, Material> {
    @Autowired
    private ModelMapper mapper;

    @Override
    public MaterialDTO toDTO(Material entity, Object... args) {
        MaterialDTO materialDTO = mapper.map(entity, dClass);
        String path = materialDTO.getPath();
        String name = path.substring(path.lastIndexOf(",") + 1);
        materialDTO.setName(name);
        return materialDTO;
    }

    @Override
    public Material toEntity(MaterialDTO dto, Object... args) {
        Material material = mapper.map(dto, eClass);
        if (args.length >= 1) {
            Curse curse = (Curse) args[0];
            material.setCurse(curse);
        }

        return material;
    }
}
