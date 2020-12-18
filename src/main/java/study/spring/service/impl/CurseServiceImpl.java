package study.spring.service.impl;

import javafx.util.Pair;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.spring.dto.CurseDTO;
import study.spring.entity.Curse;
import study.spring.entity.Material;
import study.spring.entity.User;
import study.spring.exception.NotExistsException;
import study.spring.repository.CurseRepository;
import study.spring.repository.MaterialRepository;
import study.spring.repository.UserRepository;
import study.spring.service.CurseService;
import study.spring.service.mapper.CurseDTOMapper;
import study.spring.service.mapper.MaterialDTOMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static java.util.stream.Collectors.toSet;

@Service
public class CurseServiceImpl implements CurseService {
    @Autowired
    private CurseRepository curseRepo;
    @Autowired
    private UserRepository userRrepo;
    @Autowired
    private MaterialRepository matRepo;
    @Autowired
    private CurseDTOMapper cMapper;
    @Autowired
    private MaterialDTOMapper mMapper;
    @Value("${path.to.files}")
    private String path;

    @Override
    @Transactional
    public Pair<String, User> addCurse(String curseName, User user) {
        var curse = user.getCurses().stream()
                .filter(c -> curseName.equals(c.getCurseName())).findAny();
        if (curse.isPresent()) {
            return new Pair<>("wrong", user);
        }

        curse = curseRepo.findByCurseName(curseName);
        Curse c = curse.orElse(new Curse(curseName));
        user = userRrepo.getOne(user.getId());
        user.addCurse(c);
        userRrepo.save(user);
        return new Pair<>("ok", user);
    }

    @Override
    @SneakyThrows
    public CurseDTO getCurseById(Integer id) {
        Curse curse = getById(id);
        if (curse == null) {
            throw new NotExistsException();
        }
        return cMapper.toDTO(curse);
    }

    @Override
    @SneakyThrows
    public CurseDTO editCurse(CurseDTO c, User user) {
        Integer id = c.getId();
        Curse curse = getById(id);
        if (curse == null) {
            throw new NotExistsException();
        }

        Optional<Curse> optionalCurse = curseRepo.findByCurseName(c.getCurseName());
        if (optionalCurse.isPresent() && !id.equals(optionalCurse.get().getId())) {
            return null;
        }

        curse.setCurseName(c.getCurseName());
        int uId = user.getId();
        Set<Pair<String, String>> paths = upload(id, uId, path, c.getFile());
        if (!paths.isEmpty()) {
            curse.setMaterials(paths.stream()
                    .map(p -> new Material(p.getKey(), p.getValue(),
                            LocalDate.now(), getById(id))).collect(toSet()));

        }

        curse = curseRepo.save(curse);
        user.setCurses(curseRepo.findByUserId(uId));
        c = cMapper.toDTO(curse);
        return c;
    }



    @Override
    @SneakyThrows
    @Transactional
    public User deleteFromUser(Integer id, User user) {
        Curse curse = getById(id);
        if (curse != null) {
            val optUser= curse.getUsers().stream()
                    .filter(u -> user.equals(u)).findAny();
            if (optUser.isPresent()) {
                User u = optUser.get();
                u.removeCurse(curse);
                userRrepo.save(u);
                if (!curse.getMaterials().isEmpty()) {
                    val delMat = curse.getMaterials().stream().filter(m ->
                            m.getPath().split(",")[1].equals(u.getId().toString()))
                            .collect(toSet());
                    delMat.forEach(m -> deleteMaterial(m.getId()));
                    curse.getMaterials().removeAll(delMat);
                    curseRepo.save(curse);
                }
                return u;
            }
        }

        throw new NotExistsException();
    }

    @Override
    public void deleteCurse(Integer id) {
        Curse curse = getById(id);
        if (delete(id)) {

        }
    }

    @Override
    public Page<CurseDTO> getUsersCourses(Pageable pageable, User u) {
        val cSet = curseRepo.findByUserId(u.getId(), pageable).map(cMapper::toDTO);
        return cSet;
    }

    @Override
    public List<CurseDTO> getAllCurses() {
        val cList = newArrayList(transform(curseRepo.findAll(), cMapper::toDTO));
        return cList;
    }

    @Override
    @SneakyThrows
    public void deleteMaterial(Integer id) {
        Material material = matRepo.findById(id).orElseThrow(NotExistsException::new);
        matRepo.delete(material);
        deleteMaterialFile(material);
    }

    @Override
    public CurseRepository getRepository() {
        return curseRepo;
    }

    @SneakyThrows
    private void deleteMaterialFile(Material material) {
        Path fileToDeletePath = Paths.get(material.getPath());
        Files.delete(fileToDeletePath);
    }
}
