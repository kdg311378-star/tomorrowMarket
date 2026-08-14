package com.stock.tomorrowMarket.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "내일장 API 명세서", version = "v1", description = "내일장 프로젝트 통합 API 명세서입니다."),
        security = @SecurityRequirement(name = "cookieAuth")
)
@SecurityScheme(
        name = "cookieAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "accessToken",
        description = "인증을 위해 발급받은 HttpOnly 쿠키(accessToken)가 브라우저에 의해 자동으로 전송됩니다."
)
public class SwaggerConfig {
}
