package com.lcwd.electronic.store.validate;

import com.lcwd.electronic.store.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Annotation;

public class UniqueTitleValidator implements ConstraintValidator<UniqueTitle,String>{

    @Autowired
    private CategoryRepository repository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String val = repository.isFieldUnique(s);
        if(val == null || val.length() == 0)
        {
            return true;
        }
        return false;
    }
}