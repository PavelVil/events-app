package com.github.pavelvil.eventsapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usr")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Organization> organizations = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<Event> events = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_category_subscription",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> subscribedCategories = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_organization_subscription",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id"))
    private Set<Organization> subscribedOrganizations = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addSubscription(Organization organization) {
        subscribedOrganizations.add(organization);
    }

    public boolean removeOrganizationSubscription(Long organizationId) {
        return subscribedOrganizations.removeIf(it -> it.getId().equals(organizationId));
    }

    public void addSubscription(Category category) {
        subscribedCategories.add(category);
    }

    public boolean removeCategorySubscription(Long categoryId) {
        return subscribedCategories.removeIf(it -> it.getId().equals(categoryId));
    }

    public boolean hasRole(Role role) {
        return roles != null && roles.contains(role);
    }

    boolean addEvent(Event event) {
        return events.add(event);
    }

    boolean removeEvent(Event event) {
        return events.removeIf(it -> it.getId().equals(event.getId()));
    }

}
