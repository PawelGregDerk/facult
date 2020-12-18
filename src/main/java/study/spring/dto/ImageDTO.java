package study.spring.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import study.spring.valid.image.ValidImage;

@Data
public class ImageDTO {
    @ValidImage
    private MultipartFile file;
    private Integer id;
    private String sketch;
    private String path;
}
