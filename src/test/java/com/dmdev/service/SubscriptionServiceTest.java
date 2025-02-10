package com.dmdev.service;

import com.dmdev.dao.SubscriptionDao;
import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateSubscriptionMapper;
import com.dmdev.validator.CreateSubscriptionValidator;
import com.dmdev.validator.Error;
import com.dmdev.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionDao subscriptionDao;
    @Mock
    private CreateSubscriptionMapper createSubscriptionMapper;
    @Mock
    private CreateSubscriptionValidator createSubscriptionValidator;
    @InjectMocks
    SubscriptionService subscriptionService;

    @Test
    void upsertIfExistingSubscriptionHave() {
        CreateSubscriptionDto dto = getValidDto();
        Subscription existingSubscription = getValidSubscriptionEqualsDto();
        ValidationResult validationResult = new ValidationResult();


        doReturn(Collections.singletonList(existingSubscription))
                .when(subscriptionDao).findByUserId(dto.getUserId());
        doReturn(validationResult)
                .when(createSubscriptionValidator).validate(dto);
        doReturn(existingSubscription)
                .when(subscriptionDao).upsert(existingSubscription);


        Subscription result = subscriptionService.upsert(dto);

        assertNotNull(result);
        assertEquals(existingSubscription, result);

        verify(subscriptionDao).findByUserId(dto.getUserId());
        verify(createSubscriptionValidator).validate(dto);
        verify(subscriptionDao).upsert(existingSubscription);

        assertEquals(dto.getExpirationDate(), existingSubscription.getExpirationDate());
        assertEquals(Status.ACTIVE, existingSubscription.getStatus());
    }

    @Test
    void upsertIfExistingSubscriptionNoHave() {
        CreateSubscriptionDto dto = getValidDto();
        Subscription newSubscription = getValidSubscriptionEqualsDto();
        ValidationResult validationResult = new ValidationResult();


        doReturn(Collections.emptyList())
                .when(subscriptionDao).findByUserId(dto.getUserId());
        doReturn(validationResult)
                .when(createSubscriptionValidator).validate(dto);
        doReturn(newSubscription)
                .when(createSubscriptionMapper).map(dto);
        doReturn(newSubscription)
                .when(subscriptionDao).upsert(newSubscription);


        Subscription result = subscriptionService.upsert(dto);


        assertNotNull(result);
        assertEquals(newSubscription, result);

        verify(subscriptionDao).findByUserId(dto.getUserId());
        verify(createSubscriptionValidator).validate(dto);
        verify(createSubscriptionMapper).map(dto);
        verify(subscriptionDao).upsert(newSubscription);

        assertEquals(dto.getExpirationDate(), newSubscription.getExpirationDate());
        assertEquals(Status.ACTIVE, newSubscription.getStatus());
    }


    @Test
    void shouldValidationExceptionDtoValid() {
        CreateSubscriptionDto dto = getInvalidDto();
        ValidationResult validationResult = new ValidationResult();

        validationResult.add(Error.of(101, "message"));
        doReturn(validationResult).when(createSubscriptionValidator).validate(dto);

        assertThrows(ValidationException.class,()-> subscriptionService.upsert(dto));
        verifyNoInteractions(subscriptionDao,createSubscriptionMapper);


    }

    private Subscription getValidSubscriptionEqualsDto() {
        return Subscription.builder()
                .id(2)
                .userId(2)
                .name("Ivan")
                .status(Status.ACTIVE)
                .provider(Provider.APPLE)
                .expirationDate(Instant.parse("2099-02-10T12:53:29.124Z"))
                .build();
    }

    private CreateSubscriptionDto getInvalidDto() {
        return CreateSubscriptionDto.builder().userId(2)
                .name("")
                .provider(Provider.APPLE.name())
                .expirationDate(Instant.parse("2099-02-10T12:53:29.124Z"))
                .build();
    }

    private CreateSubscriptionDto getValidDto() {
        return CreateSubscriptionDto.builder().userId(2)
                .name("Ivan")
                .provider(Provider.APPLE.name())
                .expirationDate(Instant.parse("2099-02-10T12:53:29.124Z"))
                .build();
    }
}