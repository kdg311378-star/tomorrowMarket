package com.stock.tomorrowMarket.auth.controller;

import com.stock.tomorrowMarket.auth.dto.EmailSendRequest;
import com.stock.tomorrowMarket.auth.dto.EmailVerifyRequest;
import com.stock.tomorrowMarket.auth.service.EmailService;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Email", description = "이메일 인증 API")

@RestController
@RequestMapping("/api/auth/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "이메일 인증번호 발송", description = "회원가입 또는 비밀번호 찾기 시 본인 인증을 위해 이메일로 6자리 인증번호를 발송합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이메일로 인증번호가 발송되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "이메일 전송 실패")
    })
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendEmail(@Valid @RequestBody EmailSendRequest request) {
        emailService.sendVerificationEmail(request.email());
        return ResponseEntity.ok(ApiResponse.success("이메일로 인증번호가 발송되었습니다."));
    }

    @Operation(summary = "이메일 인증번호 확인", description = "이메일로 발송된 6자리 인증번호가 올바른지 검증합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이메일 인증이 완료되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않거나 만료된 인증번호입니다.")
    })
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyCode(@Valid @RequestBody EmailVerifyRequest request) {
        emailService.verifyCode(request.email(), request.code());
        return ResponseEntity.ok(ApiResponse.success("이메일 인증이 완료되었습니다."));
    }
}
