package com.example.demo.dto;

// Không cần import LocalDate nữa vì birthday đã bị loại bỏ

public class UserForm {
    private String address;
    private String avatar;
    private Integer districtId;
    private String email;
    private Boolean gender; // Giữ nguyên Boolean cho gender
    private String lastName;
    private String password;
    private String phone;
    private Integer provinceId;
    private Integer wardId;
    private String school; // Giữ nguyên String cho school

    // Getters and Setters (Đã được cập nhật để chỉ bao gồm các thuộc tính trên)

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getGender() { // Kiểu trả về là Boolean (có thể null)
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
