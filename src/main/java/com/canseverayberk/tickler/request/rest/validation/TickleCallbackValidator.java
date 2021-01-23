package com.canseverayberk.tickler.request.rest.validation;

import com.canseverayberk.tickler.model.Tickle;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class TickleCallbackValidator implements ConstraintValidator<ValidTickleCallback, Tickle> {
    @Override
    public boolean isValid(Tickle tickle, ConstraintValidatorContext constraintValidatorContext) {
        if(Objects.isNull(tickle.getRestCallbackUrl()) && Objects.isNull(tickle.getKafkaCallbackTopic())) {
            return false;
        }
        return true;
    }
}