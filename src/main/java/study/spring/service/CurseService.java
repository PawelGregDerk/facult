package study.spring.service;

import javafx.util.Pair;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.spring.dto.CurseDTO;
import study.spring.entity.Curse;
import study.spring.entity.User;
import study.spring.repository.CurseRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public interface CurseService extends Service<Integer, Curse, CurseRepository> {
    Pair<String, User> addCurse(String c, User user);

    CurseDTO getCurseById(Integer id);

    CurseDTO editCurse(CurseDTO c, User user);

    @Transactional
    User deleteFromUser(Integer id, User user);

    void deleteCurse(Integer id);

    Page<CurseDTO> getUsersCourses(Pageable pageable, User u);

    List<CurseDTO> getAllCurses();

    default Set<Pair<String, String>> upload(int id, int uId, String path,
                        MultipartFile... files) throws IOException {
        Set<Pair<String, String>> paths = new HashSet<>();
        if (files != null && files.length > 0) {
            int i = 0;
            for (MultipartFile f : files) {
                if (!f.isEmpty()) {
                    String name = f.getOriginalFilename();
                    val p = new File(name).toPath();
                    String mimeType = null;
                    try {
                        mimeType = Files.probeContentType(p);
                    } catch (IOException e) {}
                    val date = java.time.LocalDate.now().toString();
                    val time = java.time.LocalTime.now().toString().replaceAll(":", ".");
                    String fullName = String.format("%s%d,%d,%d,%s,%s,%s",
                            path, id, uId, ++i, date, time, name);
                    val is = f.getInputStream();
                    Files.copy(is, Paths.get(fullName));
                    is.close();
                    paths.add(new Pair<>(fullName, mimeType));
                }
            }
        }
        return paths;
    }

    @SneakyThrows
    void deleteMaterial(Integer id);
}
