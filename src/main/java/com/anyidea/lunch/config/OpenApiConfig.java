package com.anyidea.lunch.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "오늘 점심 뭐 먹지? API",
                description = "카카오 장소/지도 API를 이용해 점심 후보를 찾아주는 서비스의 백엔드 API 문서",
                version = "1.0.0",
                contact = @Contact(name = "any-idea-for-lunch")
        )
)
public class OpenApiConfig {
}
