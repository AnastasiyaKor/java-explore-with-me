package ru.practicum.ewm_server.service;

import ru.practicum.ewm_server.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsers(List<Integer> ids, int from, int size);

    User create(User newUser);

    void deleteUserId(int userId);

    User getById(int userId);
}
