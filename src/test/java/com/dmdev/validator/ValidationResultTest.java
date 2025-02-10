package com.dmdev.validator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.*;

class ValidationResultTest {

    private final List<Error> errors = new ArrayList<>();

    @Test
    void addValidationError() {
        Error error = Error.of(100, "fakeMessage");

        int errorsSize = errors.size();
        errors.add(error);

        assertAll(
                () -> assertEquals(errors.size(), errorsSize + 1),
                () -> assertTrue(errors.contains(error)),
                () -> assertEquals(error, errors.get(0))
        );

    }

    @Test
    void checkHasErrorsIsEmpty() {
        assertTrue(errors.isEmpty());
    }

    @Test
    void checkHasErrorsIsNotEmpty() {
        Error error = Error.of(100, "fakeMessage");

        errors.add(error);

        assertTrue(errors.contains(error));
    }
}