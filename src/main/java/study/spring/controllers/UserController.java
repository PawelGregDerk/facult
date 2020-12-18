package study.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.spring.dto.ImageDTO;
import study.spring.dto.RoleDTO;
import study.spring.dto.UserDTO;
import study.spring.entity.User;
import study.spring.service.ImgService;
import study.spring.service.RoleService;
import study.spring.service.UserService;
import study.spring.service.mapper.UserDTOMapper;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Value("${posts}")
    private List<String> posts;
    @Autowired
    private UserService uService;
    @Autowired
    private ImgService iService;
    @Autowired
    private RoleService rService;
    @Autowired
    private UserDTOMapper mapper;

    @GetMapping("cabinet")
    public String toCabinet(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        ImageDTO photo = iService.getPhoto(user.getId());
        model.addAttribute("photo", photo.getSketch());
        return user.hasRole("ROLE_ADMIN") ? "admin/cabinet" : "user/userCabinet";
    }

    @GetMapping("showAllUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAllUsers(Model model) {
        model.addAttribute("users", uService.getAllUsers());
        return "admin/allUsers";
    }

    @GetMapping("searchUser")
    public String searchUser(Model model, String keyword, String post) {
        model.addAttribute("users", uService.getSearchUsers(keyword, post));
        return "admin/allUsers";
    }

    @GetMapping("deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        uService.deleteUser(id);
        return id.equals(user.getId()) ? "redirect:/logout" : "redirect:/showAllUsers";
    }

    @GetMapping(value = {"editUser", "editUser/{id}"})
    public String editUser(@PathVariable(required = false) Integer id, Model model,
                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        boolean isSame = id == null || id.equals(user.getId());
        UserDTO userDTO = isSame ? mapper.toDTO(user) : uService.getUserById(id);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("imageDTO", new ImageDTO());
        if (isSame) {
            model.addAttribute("same", true);
        }
        return "user/editUser";
    }

    @PostMapping("update/{id}")
    public String updateUser(@PathVariable Integer id,
                             @Valid UserDTO userDTO, BindingResult result, Model m,
                             HttpSession session, RedirectAttributes attr,
                             @RequestParam Optional<List<RoleDTO>> roles) {
        User user = (User) session.getAttribute("user");
        if (result.hasErrors()) {
            m.addAttribute("imageDTO", new ImageDTO());
            userDTO.setId(id);
            return "user/editUser";
        }

        if (roles.isPresent()) {
            userDTO.setRoles(roles.get());
        }

        User u = uService.editUser(userDTO);
        attr.addFlashAttribute("success", true);
        if (user.getId().equals(u.getId())) {
            session.setAttribute("user", u);
            return "redirect:/cabinet";
        }

        return "redirect:/showAllUsers";
    }

    @ModelAttribute("posts")
    public List<String> getPosts() {
        return posts;
    }

    @ModelAttribute("roles")
    public List<RoleDTO> initializeProfiles() {
        return rService.getAllRoles();
    }
}
