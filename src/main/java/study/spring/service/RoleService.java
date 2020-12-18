package study.spring.service;

import study.spring.dto.RoleDTO;
import study.spring.entity.Role;
import study.spring.repository.RoleRepository;

import java.util.List;

public interface RoleService extends Service<Integer, Role, RoleRepository> {
    List<RoleDTO> getAllRoles();
}
