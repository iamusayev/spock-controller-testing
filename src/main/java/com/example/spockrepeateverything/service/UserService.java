package com.example.spockrepeateverything.service;

import com.example.spockrepeateverything.dao.entity.UserEntity;
import com.example.spockrepeateverything.dao.repository.UserRepository;
import com.example.spockrepeateverything.exception.NotFoundException;
import com.example.spockrepeateverything.mapper.UserMapper;
import com.example.spockrepeateverything.model.dto.UserRequestDto;
import com.example.spockrepeateverything.model.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.spockrepeateverything.model.constants.ExceptionConstants.USER_NOT_FOUND_CODE;
import static com.example.spockrepeateverything.model.constants.ExceptionConstants.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;


    public void createUser(UserRequestDto dto) {
        log.info("ActionLog.createUser.start");

        userRepository.save(UserMapper.INSTANCE
                .mapRequestDtoToEntity(dto));

        log.info("ActionLog.createUser.success");
    }


    public UserResponseDto getUserById(Long id) {
        log.info("ActionLog.getUserById.start id: {}", id);

        var dto = UserMapper.INSTANCE
                .mapEntityToResponseDto(fetchUserById(id));

        log.info("ActionLog.getUserById.success id: {}", id);
        return dto;
    }


    public List<UserResponseDto> getAllUsers() {
        log.info("ActionLog.getAllUsers.start");

        var users = UserMapper.INSTANCE
                .mapEntitiesToResponseDtos(userRepository.findAll());
        if (users.isEmpty()) {
            throw new NotFoundException("Users not found", "USERS_NOT_FOUND_EXCEPTION");
        }
        log.info("ActionLog.getAllUsers.success");
        return users;
    }

    public void updateName(Long id, String name) {
        log.info("ActionLog.updateName.start id: {}", id);

        var user = fetchUserById(id);
        user.setName(name);
        userRepository.save(user);

        log.info("ActionLog.updateName.success id: {}", id);
    }

    public void updateLastname(Long id, String lastname) {
        log.info("ActionLog.updateLastname.start id: {}", id);

        var user = fetchUserById(id);
        user.setLastname(lastname);
        userRepository.save(user);

        log.info("ActionLog.updateLastname.success id: {}", id);
    }

    public void updateUsername(Long id, String username) {
        log.info("ActionLog.updateUserName.start id: {}", id);

        var user = fetchUserById(id);
        user.setUsername(username);
        userRepository.save(user);

        log.info("ActionLog.updateUserName.success id: {}", id);
    }

    public void updatePassword(Long id, String password) {
        log.info("ActionLog.updatePassword.start id: {}", id);


        var user = fetchUserById(id);
        user.setPassword(password);
        userRepository.save(user);

        log.info("ActionLog.updatePassword.success id: {}", id);
    }


    public void deleteUser(Long id) {
        log.info("ActionLog.deleteUser.start");

        userRepository.deleteById(id);

        log.info("ActionLog.deleteUser.success");
    }

    private UserEntity fetchUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id), USER_NOT_FOUND_CODE);
        });
    }

}
