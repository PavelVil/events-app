package com.github.pavelvil.eventsapi.repository;

import com.github.pavelvil.eventsapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT DISTINCT u.email FROM User u " +
            "LEFT JOIN u.subscribedCategories c " +
            "LEFT JOIN u.subscribedOrganizations o " +
            "WHERE c.id IN :categoryIds OR o.id = :organizationId")
    Set<String> getEmailsBySubscriptions(@Param("categoryIds") Collection<Long> categoriesId,
                                         @Param("organizationId") Long organizationId);


    boolean existsByIdAndSubscribedCategoriesId(Long userId, Long categoryId);

    boolean existsByIdAndSubscribedOrganizationsId(Long userId, Long organizationId);

    boolean existsByUsernameOrEmail(String username, String email);
}
