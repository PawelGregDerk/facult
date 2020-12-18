package study.spring.controllers;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.spring.dto.CurseDTO;
import study.spring.entity.User;
import study.spring.service.CurseService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
import static study.spring.util.FacultUtils.DATA_ON_PAGE;

@Controller
public class CurseController {
    @Autowired
    private CurseService cService;

    @GetMapping("/addCurse")
    public String main(Model model) {
        model.addAttribute("curses", cService.getAllCurses());
        return "user/addCurse";
    }

    @PostMapping("/saveCurse")
    public String addCurse(HttpSession session, @RequestParam Optional<String> curseName,
                           @RequestParam Optional<String> curse, RedirectAttributes attr) {
        String msg = "empty";
        outer: {
            if ((!curseName.isPresent() || curseName.get().trim().isEmpty())
            && (!curse.isPresent() || curse.get().trim().isEmpty())) {
                break outer;
            }

            User user = (User) session.getAttribute("user");
            Pair<String, User> result = new Pair<>(msg, user);
            if (curseName.isPresent() && !curseName.get().trim().isEmpty()) {
                result = cService.addCurse(curseName.get(), user);
            } else if (curse.isPresent() && !curse.get().trim().isEmpty()) {
                result = cService.addCurse(curse.get(), user);
            }

            msg = result.getKey();
            user = result.getValue();
            session.setAttribute("user", user);
        }
        attr.addFlashAttribute("msg", msg);
        return "redirect:/addCurse";
    }


    @GetMapping("editCurse/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        CurseDTO curseDTO = cService.getCurseById(id);
        model.addAttribute("curseDTO", curseDTO);
        return "user/editCurse";
    }

    @PostMapping("updateCurse/{id}")
    public String updateUser(@PathVariable Integer id, @Valid CurseDTO curseDTO,
                             HttpSession session, BindingResult result,
                             RedirectAttributes attr, Model model) {
        if (result.hasErrors()) {
            curseDTO.setId(id);
            return "user/editCurse";
        }

        User user = (User) session.getAttribute("user");
        CurseDTO curse = cService.editCurse(curseDTO, user);
        if (curse == null) {
            model.addAttribute("msg", "empty");
            curseDTO.setId(id);
            return "user/editCurse";
        }

        attr.addFlashAttribute("msg", "success");
        return "redirect:/showUserCurses";
    }

    @GetMapping("showUserCurses")
    public String showUserCurses(HttpSession session, Model model,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {
        final int currentPage = page.orElse(1);
        final int pageSize = size.orElse(DATA_ON_PAGE);
        User user = (User) session.getAttribute("user");
        Page<CurseDTO> curses = cService.getUsersCourses(
                PageRequest.of(currentPage - 1, pageSize), user);
        model.addAttribute("curses", curses);
        int totalPages = curses.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = rangeClosed(1, totalPages).boxed().collect(toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user/showUserCurses";
    }

    @GetMapping("deleteCurse/{id}")
    public String deleteCurse(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        user = cService.deleteFromUser(id, user);
        session.setAttribute("user", user);
        return "redirect:/showUserCurses";
    }
}
