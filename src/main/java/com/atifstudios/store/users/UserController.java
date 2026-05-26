package com.atifstudios.store.users;

import com.atifstudios.store.auth.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get users.")
    public Iterable<UserDto> getAllUsers(@Parameter(description = "Sort value (Optional).") @RequestParam(required = false, defaultValue = "", name = "sort") String sort) {
        // this gets all users in order (Optional e.g., /users?sort=name/email)
        return userService.getUsers(sort);
    } // method ends

    @GetMapping("/{id}")
    @Operation(summary = "Get a user.")
    public ResponseEntity<UserDto> getUser(@Parameter(description = "The ID of the user.") @PathVariable(name = "id") Long id) {
        // this gets the user by its id
        var userDto = userService.getUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDto); // status 200
    } // method ends

    @PostMapping
    @Operation(summary = "Register a new User.")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegisterUserRequest request) {
         // this adds a new user
         var userDto = userService.addUser(request);
         return ResponseEntity.status(HttpStatus.CREATED).body(userDto); // status 201
    } // method ends

    @PutMapping("/{id}")
    @Operation(summary = "Update the user details.")
    public ResponseEntity<UserDto> updateUser(@Parameter(description = "The ID of the user.") @PathVariable(name = "id") Long id, @Valid @RequestBody UpdateUserRequest request) {
        // this updates the user details
        var userDto = userService.updateUserDetails(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(userDto); // status 200
    } // method ends

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the user.")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "The ID of the user.") @PathVariable(name = "id") Long id) {
        // this deletes the user
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // status 204
    } // method ends

    @PostMapping("/{id}/change-password")
    @Operation(summary = "Change the user password.")
    public ResponseEntity<Void> changePassword(@Parameter(description = "The ID of the user.") @PathVariable(name = "id") Long id, @Valid @RequestBody ChangePasswordRequest request) {
        // this changes the password
        userService.changePassword(id, request);
        return ResponseEntity.noContent().build(); // status 204
    } // method ends

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "user not found.")); // status 404
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyRegistered() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "this email is already registered")); // status 400
    }

    @ExceptionHandler(PasswordMismatchedException.class)
    public ResponseEntity<Map<String, String>> handlePasswordMismatched() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "incorrect old password")); // status 401
    }

} // class ends
