package com.gmail.tikrai.books.validators;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.validators.NotAntiqueScienceBook.NotAntiqueScienceBookValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NotAntiqueScienceBookValidator.class})
public @interface NotAntiqueScienceBook {

  String message() default "Book cannot be both antique and science journal";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  class NotAntiqueScienceBookValidator implements ConstraintValidator<NotAntiqueScienceBook, Book> {

    @Override
    public void initialize(NotAntiqueScienceBook constraint) {
    }

    @Override
    public boolean isValid(Book book, ConstraintValidatorContext context) {
      return !book.scienceIndex().isPresent() || !book.antiqueReleaseYear().isPresent();
    }
  }
}
