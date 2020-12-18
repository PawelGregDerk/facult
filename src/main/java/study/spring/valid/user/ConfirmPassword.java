package study.spring.valid.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConfirmPasswordValidator.class})
public @interface ConfirmPassword {

    String message() default "{user.confirm.pas.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
