package study.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.spring.dto.ImageDTO;
import study.spring.dto.RoleDTO;
import study.spring.dto.UserDTO;
import study.spring.entity.User;
import study.spring.exception.UserAlreadyExistException;
import study.spring.service.ImgService;
import study.spring.service.RoleService;
import study.spring.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Controller
public class RegisterController {
    @Value("${posts}")
    List<String> posts;
    @Autowired
    private UserService uService;
    @Autowired
    private ImgService iService;
    @Autowired
    private RoleService rService;

    @ModelAttribute("posts")
    public List<String> getPosts() {
        return posts;
    }

    @GetMapping("/register")
    public String showForm(UserDTO userDTO) {
        return "register/register_form";
    }

    @PostMapping("/register")
    public String submitForm(@Valid UserDTO userDTO, BindingResult bindingResult,
                             @RequestParam Optional<List<RoleDTO>> roles, Model model) {
        if (bindingResult.hasErrors()) {
            return "register/register_form";
        } else {
            try {
                if (roles.isPresent()) {
                    userDTO.setRoles(roles.get());
                } else {
                    bindingResult.rejectValue("roles", "roles.empty");
                    return "register/register_form";
                }
                User user = uService.addUser(userDTO);
                iService.addPhoto(user.getId(), new ImageDTO());
                model.addAttribute("user", user);
                return "register/register_success";
            } catch (UserAlreadyExistException e) {
                bindingResult.rejectValue("email", e.getMessage());
                return "register/register_form";
            }
        }
    }

    @ModelAttribute("roles")
    public List<RoleDTO> initializeProfiles() {
        List<RoleDTO> roles = rService.getAllRoles().stream()
                .filter(r -> r != RoleDTO.ROLE_ADMIN).collect(toList());
        return roles;
    }
}