package com.stock.tomorrowMarket.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UserUpdateRequest(
        @NotBlank(message = "이름은 필수입니다.") String name,
        @NotNull(message = "생년월일은 필수입니다.") LocalDate birthdate
) {}
