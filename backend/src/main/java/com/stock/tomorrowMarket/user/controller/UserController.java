package com.stock.tomorrowMarket.user.controller;

import com.stock.tomorrowMarket.global.security.CustomUserDetails;
import com.stock.tomorrowMarket.user.dto.PasswordChangeRequest;
import com.stock.tomorrowMarket.user.dto.UserResponse;
import com.stock.tomorrowMarket.user.dto.UserUpdateRequest;
import com.stock.tomorrowMarket.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse response = userService.getMyInfo(userDetails.getUsersId());
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateMyInfo(userDetails.getUsersId(), request);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(userDetails.getUsersId(), request);
        return ResponseEntity.ok(Map.of("success", true, "data", "비밀번호 변경 성공"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.withdraw(userDetails.getUsersId());
        return ResponseEntity.ok(Map.of("success", true, "data", "회원 탈퇴 성공"));
    }
}
