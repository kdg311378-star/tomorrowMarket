package com.stock.tomorrowMarket.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordResetConfirmRequest(
        @NotBlank(message = "토큰은 필수입니다.") 
        String token,
        
        @NotBlank(message = "새 비밀번호는 필수입니다.") 
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$", message = "비밀번호는 8~20자리이며, 영문/숫자/특수문자를 모두 포함해야 합니다.")
        String newPassword
) {}
