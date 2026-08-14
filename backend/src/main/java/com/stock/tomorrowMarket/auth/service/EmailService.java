package com.stock.tomorrowMarket.auth.service;

import com.stock.tomorrowMarket.global.exception.CustomException;
import com.stock.tomorrowMarket.global.exception.ErrorCode;
import com.stock.tomorrowMarket.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UsersRepository usersRepository;
    
    private final Map<String, OtpInfo> otpStorage = new ConcurrentHashMap<>();
    
    private static class OtpInfo {
        String code;
        long expireTime;
        boolean verified;
        
        OtpInfo(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
            this.verified = false;
        }
    }

    public void sendVerificationEmail(String email) {
        if (usersRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        String code = generateCode();
        long expireTime = System.currentTimeMillis() + (3 * 60 * 1000); // 3분
        otpStorage.put(email, new OtpInfo(code, expireTime));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[내일장] 회원가입 이메일 인증 안내");
        message.setText("인증번호: " + code + "\n\n3분 이내에 화면에 입력해주세요.");
        
        javaMailSender.send(message);
    }

    public void verifyCode(String email, String code) {
        OtpInfo info = otpStorage.get(email);
        if (info == null || System.currentTimeMillis() > info.expireTime || !info.code.equals(code)) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_CODE);
        }
        info.verified = true;
    }

    public void checkVerified(String email) {
        OtpInfo info = otpStorage.get(email);
        if (info == null || !info.verified) {
            throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
        // 인증 통과 후에는 저장소에서 제거하여 재가입 방지
        otpStorage.remove(email);
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int num = 100000 + random.nextInt(900000);
        return String.valueOf(num);
    }
}
