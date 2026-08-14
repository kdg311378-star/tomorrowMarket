package com.stock.tomorrowMarket.user.dto;

import com.stock.tomorrowMarket.user.entity.Users;
import java.time.LocalDate;

public record UserResponse(
        Long usersId,
        String email,
        String name,
        LocalDate birthdate,
        String role
) {
    public static UserResponse from(Users user) {
        return new UserResponse(
                user.getUsersId(),
                user.getEmail(),
                user.getName(),
                user.getBirthdate(),
                user.getRole().name()
        );
    }
}
