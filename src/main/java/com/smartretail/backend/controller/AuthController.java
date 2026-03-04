package com.smartretail.backend.controller;

import com.smartretail.backend.dto.request.LoginRequest;
import com.smartretail.backend.dto.request.RegisterRequest;
import com.smartretail.backend.dto.response.ApiResponse;
import com.smartretail.backend.dto.response.LoginResponse;
import com.smartretail.backend.security.JwtTokenProvider;
import com.smartretail.backend.service.AuthService;
import com.smartretail.backend.service.TokenBlacklistService;
import com.smartretail.backend.entity.User;
import com.smartretail.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtTokenProvider tokenProvider;
    private final AuthService authService;
    private final TokenBlacklistService blacklistService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtTokenProvider tokenProvider,
                          AuthService authService,
                          TokenBlacklistService blacklistService,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.tokenProvider = tokenProvider;
        this.authService = authService;
        this.blacklistService = blacklistService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);

        if (user == null || !user.isEnabled() || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, "Email hoặc mật khẩu không đúng, hoặc tài khoản chưa được kích hoạt", null));
        }

        String jwt = tokenProvider.generateTokenFromEmail(user.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(true, "Đăng nhập thành công", new LoginResponse(jwt, "Bearer")));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đăng ký tài khoản thành công!", null));
    }
    @GetMapping("/verify")
    public void verifyAccount(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        authService.verifyEmail(token);
        response.sendRedirect("http://localhost:5173/login?verified=true");
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String jwt = bearerToken.substring(7);
            long expiryDuration = tokenProvider.getExpiryDuration(jwt);
            blacklistService.blacklistToken(jwt, expiryDuration);

            return ResponseEntity.ok(new ApiResponse<>(true, "Đăng xuất thành công", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Không tìm thấy token", null));
    }
}