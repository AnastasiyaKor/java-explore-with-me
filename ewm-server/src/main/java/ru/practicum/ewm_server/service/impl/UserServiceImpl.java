package ru.practicum.ewm_server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_server.entity.User;
import ru.practicum.ewm_server.exceptions.ConflictException;
import ru.practicum.ewm_server.exceptions.NotFoundException;
import ru.practicum.ewm_server.repository.UserRepository;
import ru.practicum.ewm_server.service.UserService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getUsers(List<Integer> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<User> users = userRepository.findByIdIn(ids, pageable).getContent();
        if (!users.isEmpty()) {
            return users;
        }
        return Collections.emptyList();
    }

    @Override
    public User create(User newUser) {
        List<User> users = userRepository.findAll();
        for (User u : users) {
            if (u.getEmail().equals(newUser.getEmail())) {
                throw new ConflictException("Пользователь с таким email уже существует");
            }
        }
        return userRepository.save(newUser);
    }

    @Override
    public void deleteUserId(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User getById(int userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
    }
}
