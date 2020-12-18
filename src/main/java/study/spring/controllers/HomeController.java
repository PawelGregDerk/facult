package study.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import study.spring.entity.User;
import study.spring.service.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    UserService service;

    @GetMapping(value = {"/", "/login"}) // login тут нужен для корректного логаута
    public String showForm() {
        return "login";
    }

    @GetMapping("/user")
    public String userIndex(Principal princ, HttpSession session) {
        showUser(princ, session);
        return "redirect:/cabinet";
    }

    @GetMapping("/admin")
    public String adminIndex(Principal princ, HttpSession session) {
        showUser(princ, session);
        return "redirect:/cabinet";
    }

    protected void showUser(Principal princ, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = service.getUser(princ.getName());
            session.setAttribute("user", user);
        }
    }
}
