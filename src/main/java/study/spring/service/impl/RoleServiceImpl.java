package study.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.spring.dto.RoleDTO;
import study.spring.entity.Role;
import study.spring.repository.RoleRepository;
import study.spring.service.RoleService;
import study.spring.service.mapper.RoleDTOMapper;

import java.util.List;

import static com.google.common.collect.Lists.transform;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private RoleDTOMapper rMapper;

    @Override
    public RoleRepository getRepository() {
        return roleRepo;
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<RoleDTO> roleDTOs = transform(getAll(), rMapper::toDTO);
        return roleDTOs;
    }
}
