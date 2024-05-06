package com.github.pavelvil.eventsapi.mapper;

import com.github.pavelvil.eventsapi.entity.User;
import com.github.pavelvil.eventsapi.web.dto.CreateUserRequest;
import com.github.pavelvil.eventsapi.web.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    User toEntity(CreateUserRequest request);

    UserDto toDto(User user);

}
