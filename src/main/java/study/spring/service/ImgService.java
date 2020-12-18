package study.spring.service;

import org.springframework.transaction.annotation.Transactional;
import study.spring.dto.ImageDTO;

public interface ImgService {
    @Transactional
    ImageDTO addPhoto(Integer id, ImageDTO imageDTO);

    ImageDTO getPhoto(Integer id);
}
