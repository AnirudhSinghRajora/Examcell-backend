package com.examcell.resultgen.mapper;

import com.examcell.resultgen.dto.AuthResponse;
import com.examcell.resultgen.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "email")
    @Mapping(source = "role", target = "role")
    // Assuming AuthResponse doesn't need firstName/lastName directly, it's handled in other mappers
    AuthResponse userToAuthResponse(User user);

    // If needed, mapping back from DTO to entity
    // @Mapping(target = "password", ignore = true)
    // User authResponseToUser(AuthResponse authResponse);
} 