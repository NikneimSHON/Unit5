package com.dmdev.service;

import com.dmdev.dao.SubscriptionDao;
import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.mapper.CreateSubscriptionMapper;
import com.dmdev.validator.CreateSubscriptionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class SubscriptionServiceIT extends IntegrationTestBase {

    private SubscriptionDao subscriptionDao;
    private SubscriptionService subscriptionService;

    @BeforeEach
    void init() {
        subscriptionDao = SubscriptionDao.getInstance();
        subscriptionService = new SubscriptionService(subscriptionDao,
                CreateSubscriptionMapper.getInstance(),
                CreateSubscriptionValidator.getInstance(),
                null);
    }


    @Test
    void upsertIfExistingSubscriptionHave() {
        CreateSubscriptionDto dto = getValidDto();
        Subscription expectedSubscription = getValidSubscriptionEqualsDto();


        subscriptionDao.insert(expectedSubscription);

        Subscription actualSubscription = subscriptionService.upsert(dto);

        assertThat(actualSubscription.getExpirationDate())
                .isEqualTo(expectedSubscription.getExpirationDate());
        assertThat(actualSubscription.getStatus())
                .isEqualTo(Status.ACTIVE);
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
    private CreateSubscriptionDto getValidDto() {
        return CreateSubscriptionDto.builder()
                .userId(2)
                .name("Ivan")
                .provider(Provider.APPLE.name())
                .expirationDate(Instant.parse("2099-02-10T12:53:29.124Z"))
                .build();
    }

}