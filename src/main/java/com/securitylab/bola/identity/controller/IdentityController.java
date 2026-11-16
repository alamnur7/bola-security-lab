package com.securitylab.bola.identity.controller;

import com.securitylab.bola.identity.service.CurrentUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/identity")
public class IdentityController {

    private final CurrentUserService currentUserService;

    public IdentityController(CurrentUserService currentUserService){
        this.currentUserService = currentUserService;
    }

    public CurrentUserService.CurrentUser getCurrentUser() {
        return currentUserService.getCurrentUser();
    }

    @GetMapping("/me")
    public CurrentUserService.CurrentUser getCurrentUserService() {
        return currentUserService.getCurrentUser();
    }
}
