package com.lcwd.electronic.store.validate;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueTitleValidator.class)
public @interface UniqueTitle {

    String message() default "Title of category already present ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
