package com.github.pavelvil.eventsapi.web.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Set<String> roles;

}
