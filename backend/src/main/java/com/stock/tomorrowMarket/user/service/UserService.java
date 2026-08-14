package com.stock.tomorrowMarket.user.service;

import com.stock.tomorrowMarket.global.exception.CustomException;
import com.stock.tomorrowMarket.global.exception.ErrorCode;
import com.stock.tomorrowMarket.user.dto.PasswordChangeRequest;
import com.stock.tomorrowMarket.user.dto.UserResponse;
import com.stock.tomorrowMarket.user.dto.UserUpdateRequest;
import com.stock.tomorrowMarket.user.entity.Status;
import com.stock.tomorrowMarket.user.entity.Users;
import com.stock.tomorrowMarket.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getMyInfo(Long usersId) {
        Users user = usersRepository.findById(usersId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateMyInfo(Long usersId, UserUpdateRequest request) {
        Users user = usersRepository.findById(usersId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateInfo(request.name(), request.birthdate());
        return UserResponse.from(user);
    }

    @Transactional
    public void changePassword(Long usersId, PasswordChangeRequest request) {
        Users user = usersRepository.findById(usersId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        user.updatePassword(passwordEncoder.encode(request.newPassword()));
    }

    @Transactional
    public void withdraw(Long usersId) {
        Users user = usersRepository.findById(usersId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.changeStatus(Status.WITHDRAWN);
    }
}
