package com.github.pavelvil.eventsapi.mapper;

import com.github.pavelvil.eventsapi.entity.Organization;
import com.github.pavelvil.eventsapi.web.dto.CreateOrganizationRequest;
import com.github.pavelvil.eventsapi.web.dto.OrganizationDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrganizationMapper {

    Organization toEntity(CreateOrganizationRequest request);

    OrganizationDto toDto(Organization organization);

}
