package com.example.demo.payload;

import com.example.demo.dto.UserDto;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ApiResponse<T> implements List<UserDto> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<UserDto> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(UserDto userDto) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends UserDto> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends UserDto> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public UserDto get(int index) {
        return null;
    }

    @Override
    public UserDto set(int index, UserDto element) {
        return null;
    }

    @Override
    public void add(int index, UserDto element) {

    }

    @Override
    public UserDto remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<UserDto> listIterator() {
        return null;
    }

    @Override
    public ListIterator<UserDto> listIterator(int index) {
        return null;
    }

    @Override
    public List<UserDto> subList(int fromIndex, int toIndex) {
        return List.of();
    }

    // Getters & Setters
}
