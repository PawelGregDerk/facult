package study.spring.valid.mat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class SizeValidator implements ConstraintValidator<Size, MultipartFile[]> {
    @Value("${max-files-size}")
    private Long maxSize;

    @Override
    public boolean isValid(MultipartFile[] value, ConstraintValidatorContext context) {
        if (value == null || value.length == 0) {
            return true;
        }

        long s = Arrays.stream(value).mapToLong(MultipartFile::getSize).sum();
        return s <= maxSize;
    }
}
