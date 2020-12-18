package study.spring.service.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import study.spring.dto.CurseDTO;
import study.spring.entity.Curse;

@Component
public class CurseDTOMapper extends EntityDTOMapper<CurseDTO, Curse> {
    private static final PropertyMap<CurseDTO, Curse> TO_ENTITY = new PropertyMap<CurseDTO, Curse>() {
        @Override
        protected void configure() {
            skip().setUsers(null);
        }
    };
    private static final PropertyMap<Curse, CurseDTO> TO_DTO = new PropertyMap<Curse, CurseDTO>() {
        @Override
        protected void configure() {
            skip().setUsers(null);
        }
    };
    @Autowired
    private ModelMapper mapper;

    @Override
    public CurseDTO toDTO(Curse entity, Object... args) {
        CurseDTO dto = mapToDTO(mapper, TO_DTO, entity);
        return dto;
    }

    @Override
    public Curse toEntity(CurseDTO dto, Object... args) {
        Curse entity = mapToEntity(mapper, TO_ENTITY, dto);
        return entity;
    }
}
