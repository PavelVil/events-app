package com.github.pavelvil.eventsapi.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {

    @NotBlank(message = "Name must be set!")
    private String name;

    @NotNull(message = "Start time must be set!")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant startTime;

    @NotNull(message = "End time must be set!")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant endTime;

    @NotEmpty(message = "Categories must not be empty!")
    private Set<@NotBlank(message = "Category must not be blank!") String> categories;

    @NotBlank(message = "Schedule must not be blank!")
    private String schedule;

    @NotBlank(message = "City must not be blank!")
    private String cityLocation;

    @NotBlank(message = "Street must be not blank!")
    private String streetLocation;

    @NotNull(message = "Organization id must be set!")
    private Long organizationId;

    @NotNull(message = "Id of creator must be set!")
    private Long creatorId;

}
