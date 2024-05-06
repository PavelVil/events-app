package com.github.pavelvil.eventsapi.web.controller;

import com.github.pavelvil.eventsapi.mapper.OrganizationMapper;
import com.github.pavelvil.eventsapi.service.OrganizationService;
import com.github.pavelvil.eventsapi.utils.AuthUtils;
import com.github.pavelvil.eventsapi.web.dto.CreateOrganizationRequest;
import com.github.pavelvil.eventsapi.web.dto.OrganizationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    private final OrganizationMapper organizationMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ORGANIZATION_OWNER')")
    public ResponseEntity<OrganizationDto> createOrganization(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateOrganizationRequest request
    ) {
        var createdOrganization = organizationService.save(
            organizationMapper.toEntity(request),
                AuthUtils.getCurrentUserId(userDetails)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(organizationMapper.toDto(createdOrganization));
    }

}
