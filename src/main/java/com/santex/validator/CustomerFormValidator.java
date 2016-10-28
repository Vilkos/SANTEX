package com.santex.validator;

import com.santex.dto.CustomerCreateFormDto;
import com.santex.service.CustomerService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class CustomerFormValidator implements Validator {
    private final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private final Pattern hasLowercase = Pattern.compile("[a-z]");
    private final Pattern hasNumber = Pattern.compile("\\d");
    private final Pattern names = Pattern.compile("^[\\p{L} .'-]{2,255}+$");
    private final Pattern phone = Pattern.compile("^(\\d|\\+)[0-9]{9,15}$");
    private final Pattern email = Pattern.compile("^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@+[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$");

    private final CustomerService customerService;

    public CustomerFormValidator(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(CustomerCreateFormDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CustomerCreateFormDto form = (CustomerCreateFormDto) o;
        validateEmail(errors, form);
        validatePasswords(errors, form);
        validateNames(errors, form);
        validatePhone(errors, form);
    }

    private void validateEmail(Errors errors, CustomerCreateFormDto form) {
        if (form.getEmail().isEmpty()) {
            errors.rejectValue("email", "email", "Вкажіть, будь-ласка, Вашу електронну адресу.");
        } else if (customerService.getCustomerByEmail(form.getEmail()).isPresent() && form.getId() == 0) {
            errors.rejectValue("email", "email", "Клієнт з таким ім'ям вже існує.");
        } else if (!email.matcher(form.getEmail()).find()) {
            errors.rejectValue("email", "email", "Вкажіть, будь-ласка, дійсну електронну адресу.");
        }
    }

    private void validatePasswords(Errors errors, CustomerCreateFormDto form) {
        String pass1 = form.getPassword();
        String pass2 = form.getPasswordRepeated();
        if (pass1.isEmpty()) {
            errors.rejectValue("password", "password", "Введіть, будь-ласка, пароль.");
        } else if (pass2.isEmpty()) {
            errors.rejectValue("passwordRepeated", "passwordRepeated", "Повторіть, будь-ласка, пароль.");
        } else if (pass1.equals(pass2)) {
            if (pass1.length() < 6 ||
                    pass1.length() > 30 ||
                    !hasUppercase.matcher(pass1).find() ||
                    !hasLowercase.matcher(pass1).find() ||
                    !hasNumber.matcher(pass1).find()) {
                errors.rejectValue("passwordRepeated", "passwordRepeated", "Пароль має містити від 6 до 30 симвлів, щонайменше 1 велику, 1 малу літери та 1 цифру");
            } else
                for (int i = 0; i < pass1.length(); i++) {
                    if (!Character.UnicodeBlock.of(pass1.charAt(i)).equals(Character.UnicodeBlock.BASIC_LATIN)) {
                        errors.rejectValue("passwordRepeated", "passwordRepeated", "Пароль має містити лише латинські літери.");
                    }
                }
        } else {
            errors.rejectValue("password", "password", "Паролі не співпадають!");
        }
    }

    private void validateNames(Errors errors, CustomerCreateFormDto form) {
        if (form.getFirstName().isEmpty()) {
            errors.rejectValue("firstName", "firstName", "Введіть, будь-ласка, і'мя.");
        } else if (!names.matcher(form.getFirstName()).find()) {
            errors.rejectValue("firstName", "firstName", "Введіть, будь-ласка, дійсне і'мя.");
        }
        if (form.getSecondName().isEmpty()) {
            errors.rejectValue("secondName", "secondName", "Введіть, будь-ласка, прізвище.");
        } else if (!names.matcher(form.getSecondName()).find()) {
            errors.rejectValue("secondName", "secondName", "Введіть, будь-ласка, дійсне прізвище.");
        }
    }

    private void validatePhone(Errors errors, CustomerCreateFormDto form) {
        if (form.getPhone().isEmpty()) {
            errors.rejectValue("phone", "phone", "Введіть, будь-ласка, номер телефону.");
        } else {
            if (!phone.matcher(form.getPhone()).find()) {
                errors.rejectValue("phone", "phone", "Невірний формат тел. номеру!");
            }
        }
    }
}
