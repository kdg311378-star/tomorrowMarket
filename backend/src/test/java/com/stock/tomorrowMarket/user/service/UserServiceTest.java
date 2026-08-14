package com.stock.tomorrowMarket.user.service;

import com.stock.tomorrowMarket.global.exception.CustomException;
import com.stock.tomorrowMarket.global.exception.ErrorCode;
import com.stock.tomorrowMarket.user.dto.UserResponse;
import com.stock.tomorrowMarket.user.entity.Role;
import com.stock.tomorrowMarket.user.entity.Status;
import com.stock.tomorrowMarket.user.entity.Users;
import com.stock.tomorrowMarket.user.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("내 정보 조회 - 정상 처리")
    void getMyInfo_success() {
        // given
        Users user = Users.builder()
                .email("test@test.com")
                .name("홍길동")
                .password("password")
                .birthdate(LocalDate.of(1990, 1, 1))
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        ReflectionTestUtils.setField(user, "usersId", 1L);

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // when
        UserResponse response = userService.getMyInfo(1L);

        // then
        assertEquals("test@test.com", response.email());
        assertEquals("홍길동", response.name());
        assertEquals("USER", response.role());
    }

    @Test
    @DisplayName("존재하지 않는 유저 조회 시 예외 발생")
    void getMyInfo_userNotFound() {
        // given
        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> userService.getMyInfo(999L));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}
