package study.spring.service.impl;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.spring.dto.ImageDTO;
import study.spring.repository.ImageRepository;
import study.spring.repository.UserRepository;
import study.spring.service.ImgService;
import study.spring.service.mapper.ImageDTOMapper;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

import static org.imgscalr.Scalr.resize;

@Service
public class ImgServiceImpl implements ImgService {
    @Value("${path.to.imgs}")
    private String path;
    @Value("classpath:static/img/default.jpg")
    private Resource resourceFile;
    @Autowired
    private UserRepository uRepo;
    @Autowired
    private ImageRepository iRepo;
    @Autowired
    private ImageDTOMapper mapper;

    @Override
    @SneakyThrows
    @Transactional
    public ImageDTO addPhoto(Integer id, ImageDTO imageDTO) {
        val user = uRepo.getOne(id);
        val f = imageDTO.getFile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (f != null) {
            String type = f.getContentType();
            String ext = type.substring(type.lastIndexOf("/") + 1);
            val image = ImageIO.read(f.getInputStream());
            String pathName = path + id + "." + ext;
            imageDTO.setPath(pathName);
            ImageIO.write(image, ext, new File(pathName));
            val sketch = resize(image, 100, 120);
            ImageIO.write(sketch, ext, baos);
        } else {
            File file = resourceFile.getFile();
            val sketch = resize(ImageIO.read(file), 100, 120);
            ImageIO.write(sketch, "jpg", baos);
        }
        val img = mapper.toEntity(imageDTO, baos.toByteArray());
        if (imageDTO.getId() == null) {
            img.setUser(user);
        }
        val i = iRepo.save(img);
        baos.close();
        return mapper.toDTO(i);
    }

    @Override
    @SneakyThrows
    @Transactional
    public ImageDTO getPhoto(Integer id) {
        val image = iRepo.getOne(id);
        if (image != null) {
            ImageDTO imageDTO = mapper.toDTO(image);
            String pathName = imageDTO.getPath();
            if (pathName == null || pathName.isEmpty()) {
                File file = resourceFile.getFile();
                byte[] bytes = Files.readAllBytes(file.toPath());
                imageDTO.setSketch(Base64.getMimeEncoder().encodeToString(bytes));
            }

            return imageDTO;
        }

        return null;
    }
}