package com.promptduel.service;

import com.promptduel.dto.request.LoginRequest;
import com.promptduel.dto.request.RegisterRequest;
import com.promptduel.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
