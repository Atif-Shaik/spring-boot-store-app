package com.atifstudios.store.users;

import com.atifstudios.store.auth.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getUsers(String sort) {
        if (!Set.of("name", "email").contains(sort)) {
            sort = "name"; // if not contain, set to default
        }
        return userRepository.findAllUsers(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    } // method ends

    public UserDto getUser(Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return userMapper.toDto(user);
    } // method ends

    public UserDto addUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyRegisteredException();
        }
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // hashing the password
        user.setRole(Role.USER); // setting role
        userRepository.save(user);
        return userMapper.toDto(user);
    } // method ends

    public UserDto updateUserDetails(Long id, UpdateUserRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userMapper.update(request, user); // updating user entity with request obj
        userRepository.save(user);
        return userMapper.toDto(user);
    } // method ends

    public void deleteUser(Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userRepository.delete(user);
    } // method ends

    public void changePassword(Long id, ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new PasswordMismatchedException();
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    } // method ends

} // class ends
