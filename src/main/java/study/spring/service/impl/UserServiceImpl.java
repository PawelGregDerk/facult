package study.spring.service.impl;

import com.google.common.collect.Collections2;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.spring.dto.ImageDTO;
import study.spring.dto.RoleDTO;
import study.spring.dto.UserDTO;
import study.spring.entity.Image;
import study.spring.entity.Post;
import study.spring.entity.Role;
import study.spring.entity.User;
import study.spring.exception.NotExistsException;
import study.spring.exception.UserAlreadyExistException;
import study.spring.repository.ImageRepository;
import study.spring.repository.PostRepository;
import study.spring.repository.RoleRepository;
import study.spring.repository.UserRepository;
import study.spring.service.UserService;
import study.spring.service.mapper.ImageDTOMapper;
import study.spring.service.mapper.RoleDTOMapper;
import study.spring.service.mapper.UserDTOMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    PostRepository postRepo;
    @Autowired
    private ImageRepository imageRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private UserDTOMapper uMapper;
    @Autowired
    private ImageDTOMapper iMapper;
    @Autowired
    private RoleDTOMapper rMapper;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(@Lazy BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserRepository getRepository() {
        return userRepo;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        val iList = transform(imageRepo.findAll(), iMapper::toDTO);
        return buildUserDTOList(users, iList);
    }

    @Override
    public List<UserDTO> getSearchUsers(String keyword, String post) {
        keyword = keyword.toLowerCase();
        List<User> users = userRepo.search(keyword, post);
        List<Integer> ids = transform(users, User::getId);
        val iList = transform(imageRepo.findByUserIds(ids), iMapper::toDTO);
        return buildUserDTOList(users, iList);
    }

    @Override
    @SneakyThrows
    @Transactional
    public User editUser(UserDTO userDTO) {
        User user = getById(userDTO.getId());
        if (user != null) {
            User usr = buildUser(userDTO);
            if (!user.getPassword().equals(userDTO.getPassword())) {
                String pass = passwordEncoder.encode(userDTO.getPassword());
                usr.setPassword(pass);
            }

            if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
                Collection<String> r2 = Collections2.transform(userDTO.getRoles(), u -> u.toString());
                user.setRoles(null);
                userRepo.save(user);
                usr.setRoles(roleRepo.findByNameIn(r2));
            } else {
                usr.setRoles(user.getRoles());
            }

            return userRepo.save(usr);
        } else throw new NotExistsException("Invalid user Id:" + userDTO.getId());
    }

    @Override
    @SneakyThrows
    public UserDTO getUserById(Integer id) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new NotExistsException("Invalid user Id:" + id));
        return uMapper.toDTO(user);
    }

    @Override
    @SneakyThrows
    public boolean deleteUser(Integer id) {
        Image image = imageRepo.getOne(id);
        if (delete(id)) {
            if (image != null) {
                String path = image.getPath();
                if (path != null && !path.isEmpty()) {
                    Path fileToDeletePath = Paths.get(path);
                    Files.delete(fileToDeletePath);
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public User addUser(UserDTO user) throws UserAlreadyExistException {
        User u = getUser(user.getEmail());
        if (u == null) {
            u = buildUser(user);
            u.setPassword(passwordEncoder.encode(u.getPassword()));
            Collection<Role> roles = user.getRoles().stream().map(r ->
                    roleRepo.findRoleByName(r.toString()).get()).collect(toSet());
            u.setRoles(roles);
            return userRepo.save(u);
        } else {
            throw new UserAlreadyExistException();
        }
    }

    @Override
    public User createAdmin(UserDTO u) {
        Optional<Post> post = postRepo.findByPost(u.getPost());
        User user;
        if (post.isPresent()) {
            user = userRepo.save(uMapper.toEntity(u));
            user.setPost(post.get());
        } else {
            user = buildUser(u);
        }

        List<Role> rls = roleRepo.findAll();
        Collection<Role> roles;
        if (rls != null && !rls.isEmpty()) {
            Optional<Role> optional = roleRepo.findRoleByName("ROLE_ADMIN");
            if (!optional.isPresent()) {
                rls.add(rMapper.toEntity(RoleDTO.ROLE_ADMIN));
            }

            roles = newHashSet(rls);
        } else {
            roles = newHashSet(
                    new Role("ROLE_USER"),
                    new Role("ROLE_ADMIN"),
                    new Role("ROLE_STUDENT"));
        }
        user.setRoles(roles);
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUser(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private User buildUser(UserDTO user) {
        String p = user.getPost();
        Post post = postRepo.findByPost(p).orElse(new Post(p));
        return uMapper.toEntity(user, post);
    }

    private List<UserDTO> buildUserDTOList(List<User> users, List<ImageDTO> iList) {
        val uList = new ArrayList<>(transform(users, uMapper::toDTO));
        outer:
        for (UserDTO userDTO : uList) {
            for (ImageDTO imageDTO : iList) {
                if (userDTO.getId() == imageDTO.getId()) {
                    userDTO.setImage(imageDTO);
                    continue outer;
                }
            }
        }
        return uList;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(toSet());
    }
}
