package com.dmdev.validator;

import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.set;
import static org.junit.jupiter.api.Assertions.*;


class CreateSubscriptionValidatorTest {

    private static final CreateSubscriptionValidator validator = CreateSubscriptionValidator.getInstance();

    @Test
    void fullDataValidate() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(2)
                .name("Ivan")
                .provider(Provider.APPLE.name())
                .expirationDate(Instant.now().plusSeconds(10))
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertFalse(actualResult.hasErrors());
    }

    @Test
    void invalidGetUserId() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(null)
                .name("Ivan")
                .provider(Provider.APPLE.name())
                .expirationDate(Instant.now().plusSeconds(10))
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertAll(
                () -> assertThat(actualResult.getErrors()).hasSize(1),
                () -> assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(100)
        );
    }

    @Test
    void invalidName() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(2)
                .name("")
                .provider(Provider.APPLE.name())
                .expirationDate(Instant.now().plusSeconds(10))
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertAll(
                () -> assertThat(actualResult.getErrors()).hasSize(1),
                () -> assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(101)
        );
    }

    @Test
    void invalidProvider() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(2)
                .name("ivan")
                .provider("fake")
                .expirationDate(Instant.now())
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertAll(
                () -> assertThat(actualResult.getErrors()).hasSize(1),
                () -> assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(102)

        );
    }

    @Test
    void invalidExpirationDateIsNull() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(2)
                .name("ivan")
                .provider(Provider.APPLE.name())
                .expirationDate(null)
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertAll(
                () -> assertThat(actualResult.getErrors()).hasSize(1),
                () -> assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(103)

        );
    }

    @Test
    void invalidExpirationDateIsBeforeTimestamp() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(2)
                .name("ivan")
                .provider(Provider.APPLE.name())
                .expirationDate(Instant.now().minusSeconds(10))
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertAll(
                () -> assertThat(actualResult.getErrors()).hasSize(1),
                () -> assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(103)

        );
    }

    @Test
    void invalidFullData() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(null)
                .name("")
                .provider("fake")
                .expirationDate(null)
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(4);

        List<Integer> errorsCode = actualResult.getErrors().stream().map(Error::getCode).toList();

        assertThat(errorsCode).contains(100, 101, 102, 103);
    }

}