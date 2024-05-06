package com.github.pavelvil.eventsapi.service;

import com.github.pavelvil.eventsapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final OrganizationService organizationService;

    private final CategoryService categoryService;

    private final UserService userService;

    private final UserRepository userRepository;

    private final EmailSenderService emailSenderService;

    public void sendNotifications(Long organization, Collection<Long> categoriesId, String eventName) {
        var emails = userService.getEmailsBySubscriptions(categoriesId, organization);
        emails.forEach(email -> {
            String title = "New event for one of your subscriptions!";
            String body = MessageFormat.format("New event! Name is: {0}", eventName);
            emailSenderService.send(email, title, body);
        });
    }

    @Transactional
    public void subscribeOnOrganization(Long userId, Long organizationId) {
        var currentUser = userService.findById(userId);
        currentUser.addSubscription(organizationService.findById(organizationId));
        userService.save(currentUser);
    }

    @Transactional
    public void subscribeOnCategory(Long userId, Long categoryId) {
        var currentUser = userService.findById(userId);
        currentUser.addSubscription(categoryService.findById(categoryId));
        userService.save(currentUser);
    }

    @Transactional
    public void unsubscribeFromOrganization(Long userId, Long organizationId) {
        var currentUser = userService.findById(userId);
        var removed = currentUser.removeOrganizationSubscription(organizationId);

        if (!removed) {
            return;
        }

        userService.save(currentUser);
    }

    @Transactional
    public void unsubscribeFromCategory(Long userId, Long categoryId) {
        var currentUser = userService.findById(userId);
        var removed = currentUser.removeCategorySubscription(categoryId);

        if (!removed) {
            return;
        }

        userService.save(currentUser);
    }

    public boolean hasCategorySubscription(Long userId, Long categoryId) {
        return userRepository.existsByIdAndSubscribedCategoriesId(userId, categoryId);
    }

    public boolean hasOrganizationSubscription(Long userId, Long organizationId) {
        return userRepository.existsByIdAndSubscribedCategoriesId(userId, organizationId);
    }

}
