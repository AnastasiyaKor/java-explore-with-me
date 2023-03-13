package ru.practicum.ewm_server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_server.entity.User;
import ru.practicum.ewm_server.exceptions.NotFoundException;
import ru.practicum.ewm_server.repository.UserRepository;
import ru.practicum.ewm_server.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getUsers(List<Integer> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids == null) {
            return userRepository.findAll(pageable).getContent();
        }
        return userRepository.findByIdIn(ids, pageable).getContent();
    }

    @Override
    public User create(User newUser) {
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
