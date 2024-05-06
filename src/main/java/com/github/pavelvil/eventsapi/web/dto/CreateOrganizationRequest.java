package com.github.pavelvil.eventsapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateOrganizationRequest {

    @NotBlank(message = "Organization name must be set!")
    private String name;

}
