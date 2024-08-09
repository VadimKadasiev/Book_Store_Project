package book.store.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String password;
    private String repeatPassword;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        password = constraintAnnotation.first();
        repeatPassword = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value,
                           final ConstraintValidatorContext constraintValidatorContext) {
        final Object firstObj = new BeanWrapperImpl(value).getPropertyValue(password);
        final Object secondObj = new BeanWrapperImpl(value).getPropertyValue(repeatPassword);

        return firstObj == null && secondObj == null
                || firstObj != null && firstObj.equals(secondObj);
    }
}
