package com.anyidea.lunch.controller;

import com.anyidea.lunch.dto.MenuDataDto;
import com.anyidea.lunch.service.MenuService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final String kakaoJavascriptKey;
    private final MenuService menuService;
    private final ObjectMapper objectMapper;

    public ViewController(
            @Value("${kakao.javascript-key:}") String kakaoJavascriptKey,
            MenuService menuService,
            ObjectMapper objectMapper
    ) {
        this.kakaoJavascriptKey = kakaoJavascriptKey;
        this.menuService = menuService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public String index(Model model) throws JsonProcessingException {
        MenuDataDto menuData = menuService.getMenuData();
        String menuDataJson = objectMapper.writeValueAsString(menuData);

        model.addAttribute("kakaoJsKey", kakaoJavascriptKey);
        model.addAttribute("menuDataJson", menuDataJson);
        return "index"; // templates/index.html
    }
}
