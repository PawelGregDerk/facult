package study.spring.service.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import study.spring.dto.ImageDTO;
import study.spring.entity.Image;

import java.util.Base64;

@Component
public class ImageDTOMapper extends EntityDTOMapper<ImageDTO, Image> {
    private static final PropertyMap<ImageDTO, Image> TO_ENTITY = new PropertyMap<ImageDTO, Image>() {
        @Override
        protected void configure() {
            skip().setSketch(null);
        }
    };
    private static final PropertyMap<Image, ImageDTO> TO_DTO = new PropertyMap<Image, ImageDTO>() {
        @Override
        protected void configure() {
            skip().setSketch(null);
        }
    };
    @Autowired
    private ModelMapper mapper;

    @Override
    public ImageDTO toDTO(Image entity, Object... args) {
        String sketch = Base64.getMimeEncoder().encodeToString(entity.getSketch());
        ImageDTO dto = mapToDTO(mapper, TO_DTO, entity);
        dto.setSketch(sketch);
        return dto;
    }

    @Override
    public Image toEntity(ImageDTO dto, Object... args) {
        byte[] sketch = args.length > 0 ? (byte[]) args[0] : Base64.getMimeDecoder().decode(dto.getSketch());
        Image entity = mapToEntity(mapper, TO_ENTITY, dto);
        entity.setSketch(sketch);
        return entity;
    }
}
