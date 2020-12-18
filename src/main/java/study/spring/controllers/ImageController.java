package study.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.spring.dto.ImageDTO;
import study.spring.entity.Image;
import study.spring.entity.User;
import study.spring.service.ImgService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class ImageController {
    @Autowired
    private ImgService imgService;

    @PostMapping("upload/{id}")
    public String uploadFile(@Valid @ModelAttribute("imageDTO") ImageDTO imageDTO,
                             BindingResult bindingResult, RedirectAttributes attr,
                             @PathVariable Integer id) {
        if (bindingResult.hasFieldErrors()) {
            attr.addFlashAttribute("error",
                    bindingResult.getFieldError().getDefaultMessage());
        } else {
            imgService.addPhoto(id, imageDTO);
            attr.addFlashAttribute("success", "image.loaded");
        }

        return "redirect:/editUser/" + id;
    }
}
