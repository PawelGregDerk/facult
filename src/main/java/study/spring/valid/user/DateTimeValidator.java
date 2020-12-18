package study.spring.valid.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class DateTimeValidator implements ConstraintValidator<CheckDateTime, Date> {

    @Override
    public void initialize(CheckDateTime constraintAnnotation) {

    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        return value != null && value.before(new Date());
    }
}
