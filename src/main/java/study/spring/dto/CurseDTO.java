package study.spring.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
public class CurseDTO {
    private Integer id;
    @Size
    private MultipartFile[] file;
    @NotBlank(message = "{not.empty}")
    private String curseName;
    private Set<MaterialDTO> materials = new HashSet<>();
    private Set<UserDTO> users;
}
