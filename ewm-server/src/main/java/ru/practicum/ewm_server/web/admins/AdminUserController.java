package ru.practicum.ewm_server.web.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.NewUserRequest;
import ru.practicum.ewm_server.dto.UserDto;
import ru.practicum.ewm_server.entity.User;
import ru.practicum.ewm_server.mapper.UserMapper;
import ru.practicum.ewm_server.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("A request was received from the administrator to obtain information about users");
        return UserMapper.toListUsersDto(userService.getUsers(ids, from, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Received a request from the administrator to add a new user");
        User user = userService.create(UserMapper.fromNewUserRequest(newUserRequest));
        return UserMapper.toUserDto(user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserId(@PathVariable int userId) {
        log.info("Received a request from the administrator to delete the user");
        userService.deleteUserId(userId);
    }
}
