package cl.smartjob.smartjobassessment.validators;

import cl.smartjob.smartjobassessment.annotations.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Value("${password.regex}")
    private String passwordRegex;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && password.matches(passwordRegex);
    }
}
