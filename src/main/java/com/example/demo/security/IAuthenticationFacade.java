package com.example.demo.security;

import com.example.demo.entity.User;

public interface IAuthenticationFacade {
    User getAuthenticatedUser();
}
