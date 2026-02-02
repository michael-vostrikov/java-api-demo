package com.app.userapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.app.userapi.service.dto.EndpointDescription;
import java.util.List;
import java.util.Map;

@Controller
public class SiteController {

    @GetMapping("/")
    public String getHomePage(Model model) {
        List endpointDescriptions = List.of(
            new EndpointDescription("GET", "/product/view", Map.of("id", "1"), null),
            new EndpointDescription("POST", "/product/create", null, Map.of("name", "Product 4")),
            new EndpointDescription("POST", "/product/save", Map.of("id", "1"), Map.of("categoryId", "2", "name", "Product 1", "description", "Description 1")),
            new EndpointDescription("POST", "/product/send-for-review", Map.of("id", "1"), null),

            new EndpointDescription("POST", "http://127.0.0.1:8090/review/accept", Map.of("id", "1"), null),
            new EndpointDescription("POST", "http://127.0.0.1:8090/review/decline", Map.of("id", "1"), null)
        );

        model.addAttribute("endpointDescriptions", endpointDescriptions);

        return "home";
    }

}
