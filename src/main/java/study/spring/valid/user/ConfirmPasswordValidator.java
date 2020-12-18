package study.spring.valid.user;

import study.spring.dto.UserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, UserDTO> {
    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext context) {
        return (userDTO.getPassword() != null && userDTO.getConfirmPassword()
                != null && userDTO.getPassword()
                .equals(userDTO.getConfirmPassword()));
    }
}
