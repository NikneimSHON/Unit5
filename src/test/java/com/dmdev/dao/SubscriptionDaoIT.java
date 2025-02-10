package com.dmdev.dao;

import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.integration.IntegrationTestBase;
import org.h2.engine.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionDaoIT extends IntegrationTestBase {
    private final SubscriptionDao subscriptionDao = SubscriptionDao.getInstance();

    @Test
    void findAll() {
        Subscription sub1 = subscriptionDao.insert(getSubscription(1));
        Subscription sub2 = subscriptionDao.insert(getSubscription(2));
        Subscription sub3 = subscriptionDao.insert(getSubscription(3));

        List<Subscription> subscriptionsResult = subscriptionDao.findAll();

        assertThat(subscriptionsResult).hasSize(3);

        List<Integer> subIds = subscriptionsResult.stream().map(Subscription::getId).toList();
        assertThat(subIds).contains(sub1.getId(), sub2.getId(), sub3.getId());
    }

    @Test
    void findById() {
        Subscription sub = subscriptionDao.insert(getSubscription(1));

        Optional<Subscription> subResult = subscriptionDao.findById(sub.getId());

        assertThat(subResult).isPresent();
        assertThat(subResult.get()).isEqualTo(sub);
    }

    @Test
    void deleteExistingEntity() {
        Subscription subscription = subscriptionDao.insert(getSubscription(10));

        boolean actualResult = subscriptionDao.delete(subscription.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() {
        subscriptionDao.insert(getSubscription(10));

        boolean actualResult = subscriptionDao.delete(100);

        assertFalse(actualResult);
    }

    @Test
    void update() {
        Subscription subscription = getSubscription(10);
        subscriptionDao.insert(subscription);
        subscription.setName("Nikita");

        subscriptionDao.update(subscription);

        Subscription updatedSubscription = subscriptionDao.findById(subscription.getId()).get();
        assertThat(updatedSubscription).isEqualTo(subscription);

    }

    private Subscription getSubscription(Integer userId) {
        return Subscription.builder()
                .userId(userId)
                .name("Ivan")
                .provider(Provider.APPLE)
                .status(Status.ACTIVE)
                .expirationDate(Instant.parse("2099-02-10T12:53:29.124Z"))
                .build();
    }
}