package com.canseverayberk.tickler.request.rest.validation;

import com.canseverayberk.tickler.model.Tickle;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.Objects;

public class TickleValidator implements ConstraintValidator<ValidTickle, Tickle> {
    @Override
    public boolean isValid(Tickle tickle, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        if(Objects.isNull(tickle.getPayload())) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("{invalidTickle.payload.message}").addConstraintViolation();
            return false;
        }

        if((Objects.isNull(tickle.getTtl()) && Objects.isNull(tickle.getExpirationDate()))
             || Objects.nonNull(tickle.getTtl()) && Objects.nonNull(tickle.getExpirationDate())) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("{invalidTickle.expiration.message}").addConstraintViolation();
            return false;
        }

        if(Objects.nonNull(tickle.getTtl()) && tickle.getTtl() < 1) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("{invalidTickle.ttl.message}").addConstraintViolation();
            return false;
        }

        if(Objects.nonNull(tickle.getExpirationDate()) && LocalDateTime.now().isAfter(tickle.getExpirationDate())) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("{invalidTickle.expirationDate.message}").addConstraintViolation();
            return false;
        }

        return true;
    }
}