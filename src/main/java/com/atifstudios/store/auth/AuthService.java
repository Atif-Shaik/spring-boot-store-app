package com.atifstudios.store.auth;

import com.atifstudios.store.users.UserDto;
import com.atifstudios.store.users.User;
import com.atifstudios.store.users.UserMapper;
import com.atifstudios.store.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;

    public void login(UserLoginRequest request) {
        // user authentication (UserAuthenticationService gets called)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                     request.getEmail(),
                     request.getPassword()
                )
        );

    } // method ends

    public JwtResponse createAccessToken(User user) {
        var accessToken = jwtService.generateAccessToken(user);
        return new JwtResponse(accessToken.toString());
    } // ends

    public User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    } // ends

    public Cookie generateCookie(User user) {
        var refreshToken = jwtService.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());

        return cookie;
    } // method ends

    public JwtResponse refreshToken(String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);

        if (jwt == null || jwt.isExpired()) {
            throw new TokenExpiredException();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return new JwtResponse(accessToken.toString());
    } // method ends

    public UserDto getUserThroughToken() {
        var user = getCurrentUser();

        if (user == null) {
            throw new UserNotFoundException();
        }

        return userMapper.toDto(user);
    } // method ends

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        var userId = (Long) authentication.getPrincipal(); // it will get the user id from token

        assert userId != null;
        return userRepository.findById(userId).orElse(null);
    } // method ends

    public Cookie logOut(HttpServletRequest request) {
        String refreshToken = extractFromCookie(request);

        if (refreshToken != null) {
            // delete token from database
        } else {
            // this means that token is somehow missing
            throw new TokenNotFoundException();
        }

        // create a "dummy" cookie
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // deletes cookie
        return cookie;
    } // method ends

    private String extractFromCookie(HttpServletRequest request) {

        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie: request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        } // loop ends

        return null;
    } // method ends

} // class ends
