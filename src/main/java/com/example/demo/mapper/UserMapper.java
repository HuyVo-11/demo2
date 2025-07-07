package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserForm;
import com.example.demo.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "school", ignore = true)
    @Mapping(target = "gender", expression = "java(userForm.getGender() != null ? userForm.getGender() ? \"Male\" : \"Female\" : null)") // Xử lý gender thủ công
    @Named("entityFromModel")
    User getEntityFromModel(UserForm userForm);

    @Mapping(target = "schoolName", source = "school.name", defaultValue = "")
    UserDto getModelFromEntity(User user);

    @IterableMapping(qualifiedByName = "entityFromModel")
    List<User> getEntityListFromModelList(List<UserForm> userForms);

    @Mapping(target = "school", ignore = true)
    @Mapping(target = "gender", ignore = true)
    void updateEntityFromModel(UserForm form, @MappingTarget User entity);

}

