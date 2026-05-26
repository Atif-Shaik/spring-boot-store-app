package com.atifstudios.store.users;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user); // this is for get
    User toEntity(RegisterUserRequest request); // this is for post
    void update(UpdateUserRequest request,@MappingTarget User user); // this is for put. @MappingTarget tells which obj should be updated
} // interface ends
