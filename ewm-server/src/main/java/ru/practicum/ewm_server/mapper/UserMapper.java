package ru.practicum.ewm_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_server.dto.NewUserRequest;
import ru.practicum.ewm_server.dto.UserDto;
import ru.practicum.ewm_server.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static User fromNewUserRequest(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static List<UserDto> toListUsersDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
