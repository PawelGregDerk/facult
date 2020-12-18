package study.spring.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MaterialDTO {
    private Integer id;
    private String name;
    private String path;
    private String mediaType;
    private LocalDate loadDate;
}
