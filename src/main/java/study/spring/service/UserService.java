package study.spring.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import study.spring.dto.UserDTO;
import study.spring.entity.User;
import study.spring.exception.UserAlreadyExistException;
import study.spring.repository.UserRepository;

import java.util.List;

public interface UserService extends Service<Integer, User, UserRepository>,
        UserDetailsService {
    User addUser(UserDTO user) throws UserAlreadyExistException;

    List<UserDTO> getAllUsers();

    List<UserDTO> getSearchUsers(String keyword, String post);

    UserDTO getUserById(Integer id);

    User editUser(UserDTO user);

    User createAdmin(UserDTO u);

    boolean deleteUser(Integer id);

    default User getUser(String email) {
        return getRepository().findByEmail(email).orElse(null);
    }
}
