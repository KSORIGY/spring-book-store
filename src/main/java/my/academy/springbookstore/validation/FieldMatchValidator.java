package my.academy.springbookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import my.academy.springbookstore.dto.user.UserRegistrationRequestDto;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch,
        UserRegistrationRequestDto> {

    @Override
    public boolean isValid(UserRegistrationRequestDto userRegistrationRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (userRegistrationRequestDto.getPassword() == null
                && userRegistrationRequestDto.getRepeatPassword() == null) {
            return true;
        }

        if (userRegistrationRequestDto.getPassword() == null
                || userRegistrationRequestDto.getRepeatPassword() == null) {
            return false;
        }

        boolean isValid = userRegistrationRequestDto.getPassword()
                .equals(userRegistrationRequestDto.getRepeatPassword());

        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("repeatPassword")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
