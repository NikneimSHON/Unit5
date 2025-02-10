package com.dmdev.mapper;

import com.dmdev.dao.SubscriptionDao;
import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.validator.CreateSubscriptionValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class CreateSubscriptionMapperTest {

    private final CreateSubscriptionMapper mapper = CreateSubscriptionMapper.getInstance();

    @Test
    void map() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(2)
                .name("Ivan")
                .provider(Provider.APPLE.name())
                .expirationDate(Instant.parse("2099-02-10T12:53:29.124Z"))
                .build();

        Subscription subscription = mapper.map(dto);

        Subscription expectedResult = Subscription.builder()
                .userId(2)
                .name("Ivan")
                .provider(Provider.APPLE)
                .expirationDate(Instant.parse("2099-02-10T12:53:29.124Z"))
                .status(Status.ACTIVE)
                .build();

        assertEquals(expectedResult, subscription);
    }
}