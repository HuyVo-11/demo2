package com.example.demo.mapper;

import com.example.demo.dto.UserForm;
import com.example.demo.entity.User;

public class UserMapper {

    public static User toEntity(UserForm form) {
        if (form == null) {
            return null;
        }

        User user = new User();
        user.setAddress(form.getAddress());
        user.setAvatar(form.getAvatar());
        user.setDistrictId(form.getDistrictId());
        user.setEmail(form.getEmail());
        if (form.getGender() != null) {
            user.setGender(form.getGender() ? "Male" : "Female");
        } else {
            user.setGender(null);
        }
        user.setLastName(form.getLastName());
        user.setPassword(form.getPassword());
        user.setPhone(form.getPhone());
        user.setProvinceId(form.getProvinceId());
        user.setWardId(form.getWardId());

        return user;
    }

    public static UserForm toForm(User user) {
        if (user == null) {
            return null;
        }

        UserForm form = new UserForm();
        form.setAddress(user.getAddress());
        form.setAvatar(user.getAvatar());
        form.setDistrictId(user.getDistrictId());
        form.setEmail(user.getEmail());
        form.setGender(user.getGender() != null ? user.getGender().equalsIgnoreCase("Male") : null);
        form.setLastName(user.getLastName());
        form.setPhone(user.getPhone());
        form.setProvinceId(user.getProvinceId());
        form.setWardId(user.getWardId());

        return form;
    }
}
