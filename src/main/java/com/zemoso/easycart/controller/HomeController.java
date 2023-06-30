package com.zemoso.easycart.controller;


import lombok.Generated;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Generated
public class HomeController {

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OidcUser principal) {
        return "index";
    }
}