package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserForm;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "school", ignore = true)
    User mapToUser(UserForm userForm);
    @Mapping(target = "schoolName", source = "school.name", defaultValue = "")
    UserDto mapToUserDto(User user);
}

