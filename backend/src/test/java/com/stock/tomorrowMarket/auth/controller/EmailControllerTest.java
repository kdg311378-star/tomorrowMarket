package com.stock.tomorrowMarket.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.tomorrowMarket.auth.dto.EmailSendRequest;
import com.stock.tomorrowMarket.auth.dto.EmailVerifyRequest;
import com.stock.tomorrowMarket.auth.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmailService emailService;

    @Test
    @DisplayName("이메일 전송 API 성공 시 200 반환")
    void sendEmail_Success() throws Exception {
        EmailSendRequest request = new EmailSendRequest("test@test.com");

        mockMvc.perform(post("/api/auth/email/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("이메일로 인증번호가 발송되었습니다."));

        verify(emailService).sendVerificationEmail(anyString());
    }

    @Test
    @DisplayName("인증번호 확인 API 성공 시 200 반환")
    void verifyCode_Success() throws Exception {
        EmailVerifyRequest request = new EmailVerifyRequest("test@test.com", "123456");

        mockMvc.perform(post("/api/auth/email/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("이메일 인증이 완료되었습니다."));

        verify(emailService).verifyCode(anyString(), anyString());
    }
}
