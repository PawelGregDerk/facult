package study.spring.service.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import study.spring.dto.ImageDTO;
import study.spring.dto.RoleDTO;
import study.spring.dto.UserDTO;
import study.spring.entity.Post;
import study.spring.entity.Role;
import study.spring.entity.User;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

@Component
public class UserDTOMapper extends EntityDTOMapper<UserDTO, User> {
    private static final PropertyMap<UserDTO, User> TO_ENTITY = new PropertyMap<UserDTO, User>() {
        @Override
        protected void configure() {
            skip().setExperience(null);
            skip().setPost(null);
            skip().setImage(null);
            skip().setCurses(null);
            skip().setRoles(null);
        }
    };
    private static final PropertyMap<User, UserDTO> TO_DTO = new PropertyMap<User, UserDTO>() {
        @Override
        protected void configure() {
            skip().setExperience(null);
            skip().setPost(null);
            skip().setImage(null);
            skip().setCurses(null);
            skip().setRoles(null);
        }
    };
    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDTO toDTO(User entity, Object... args) {
        LocalDate exp = entity.getExperience();
        Date date = Date.from(exp.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Period experience = Period.between(exp, LocalDate.now());
        String post = entity.getPost().getPost();
        UserDTO dto = mapToDTO(mapper, TO_DTO, entity);
        dto.setExperience(date);
        dto.setPost(post);
        dto.setWorkExperience(experience);
        if (args.length == 1) {
            Set<RoleDTO> roles = (Set<RoleDTO>) args[0];
            dto.setRoles(roles);
        }
        return dto;
    }

    @Override
    public User toEntity(UserDTO dto, Object... args) {
        LocalDate ld = dto.getExperience().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
        User entity = mapToEntity(mapper, TO_ENTITY, dto);
        entity.setExperience(ld);
        if (args.length == 1) {
            Post post = (Post) args[0];
            entity.setPost(post);
        }
        return entity;
    }
}
