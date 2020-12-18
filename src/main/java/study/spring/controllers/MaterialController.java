package study.spring.controllers;

import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.spring.dto.MaterialDTO;
import study.spring.entity.User;
import study.spring.service.CurseService;
import study.spring.service.MaterialService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.Math.toIntExact;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;

@Controller
@SessionAttributes("user")
public class MaterialController {
    @Autowired
    private CurseService cService;
    @Autowired
    private MaterialService mService;
    private static final int PAGE_SIZE = 5;

    @GetMapping("showUserMaterial") //RequestParam тут используется потому, что при применении PathVariable не подгружаются js-файлы bootstrap'a. Остальные js и css грузятся нармально
    public String showUserMaterial(@RequestParam Integer cId, User user,
                                   HttpSession session, Model model,
                                   @RequestParam("page") Optional<Integer> page) {
        Integer uId = user.getId();
        val curseDTO = mService.showUserMaterials(cId, uId);
        model.addAttribute("curseDTO", curseDTO);
        val materials = curseDTO.getMaterials().stream().collect(toList());
        int currentPage = page.orElse(1);
        val pageable = PageRequest.of(currentPage - 1, PAGE_SIZE);
        int totalPages = materials.size();
        int start = toIntExact(pageable.getOffset());
        int end = Math.min(start + PAGE_SIZE, totalPages);
        List<MaterialDTO> output = emptyList();
        if (start < end) {
            output = materials.subList(start, end);
        }

        Page<MaterialDTO> materialDTOS = new PageImpl<>(output, pageable, PAGE_SIZE);
        model.addAttribute("materialDTOS", materialDTOS);
        if (output.size() > 0) {
            int pageCount = totalPages <= 5 ? 1 : totalPages % PAGE_SIZE == 0
                    ? totalPages / PAGE_SIZE : totalPages / PAGE_SIZE + 1;
            val pageNumbers = rangeClosed(1, pageCount).boxed().collect(toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        session.setAttribute("cId", cId);
        return "user/showMaterials";
    }

    @GetMapping("deleteMaterial/{id}")
    public String deleteMaterial(@PathVariable Integer id, HttpSession session,
                                 RedirectAttributes attr) {
        cService.deleteMaterial(id);
        Integer cId = (Integer) session.getAttribute("cId");
        attr.addAttribute("cId", cId);
        attr.addFlashAttribute("success", true);
        return "redirect:/showUserMaterial";
    }

    @GetMapping("download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam Integer id)
            throws IOException {
        MaterialDTO material = mService.getMaterialDTO(id);
        val path = Paths.get(material.getPath());
        byte[] data = Files.readAllBytes(path);
        val resource = new ByteArrayResource(data);
        var mediaType = MediaType.APPLICATION_OCTET_STREAM;
        try {
            mediaType = MediaType.parseMediaType(material.getMediaType());
        } catch (Exception e) { }
        return ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(data.length)
                .body(resource);
    }
}
