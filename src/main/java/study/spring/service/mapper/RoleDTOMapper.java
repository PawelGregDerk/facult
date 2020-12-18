package study.spring.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import study.spring.dto.RoleDTO;
import study.spring.entity.Role;
//https://github.com/modelmapper/modelmapper/issues/99
@Component
public class RoleDTOMapper extends EntityDTOMapper<RoleDTO, Role> {
    @Autowired
    private ModelMapper mapper;

    @Override
    public RoleDTO toDTO(Role entity, Object... args) {
        RoleDTO role = RoleDTO.valueOf(entity.getName());
        return role;
    }

    @Override
    public Role toEntity(RoleDTO dto, Object... args) {
        Role role = new Role(dto.toString());
        return role;
    }
}
