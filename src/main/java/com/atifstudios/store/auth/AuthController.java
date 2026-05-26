package com.atifstudios.store.auth;

import com.atifstudios.store.users.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login")
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody UserLoginRequest request, HttpServletResponse response) {
        // this method logins user, generates token and creates cookie
        authService.login(request);
        var user = authService.findUser(request.getEmail()); // getting user for generating tokens

        var jwtResponse = authService.createAccessToken(user);
        response.addCookie(authService.generateCookie(user)); // adding cookie

        return ResponseEntity.status(HttpStatus.OK).body(jwtResponse); // returning new JwtResponse Dto
    } // method ends

    @PostMapping("/refresh")
    @Operation(summary = "Refresh")
    public ResponseEntity<JwtResponse> refresh(@Parameter(description = "Refresh Token") @CookieValue(value = "refreshToken") String refreshToken) {
        // re-creates access token using cookie
        var jwtResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
    } // method ends

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public ResponseEntity<?> logOutUser(HttpServletRequest request, HttpServletResponse response) {
        // this logs out the user and deletes the refresh token from the client
        Cookie cookie = authService.logOut(request);
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
    } // method ends

    @GetMapping("/me")
    @Operation(summary = "Verify Access Token")
    public ResponseEntity<UserDto> me() {
        // gets userDto by access Token sending through Authorization header
        var userDto = authService.getUserThroughToken();
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    } // ends


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Void> handleTokenExpired() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<Void> handleTokenNotFound() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

} // class ends
