package ou.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ou.services.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@Slf4j
public class UsernameValidator implements ConstraintValidator<ValidUsernameConstraint, String> {
    @Autowired
    private UserService userService;

    @Override
    public void initialize(ValidUsernameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return userService == null || userService.findByUserName(s) == null;
    }
}
