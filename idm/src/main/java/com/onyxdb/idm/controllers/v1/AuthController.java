package com.onyxdb.idm.controllers.v1;


import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.idm.generated.openapi.apis.AuthApi;
import com.onyxdb.idm.generated.openapi.models.AccountDTO;
import com.onyxdb.idm.generated.openapi.models.GetCurrentUser200Response;
import com.onyxdb.idm.generated.openapi.models.Login200Response;
import com.onyxdb.idm.generated.openapi.models.LoginRequest;
import com.onyxdb.idm.generated.openapi.models.LogoutRequest;
import com.onyxdb.idm.generated.openapi.models.RefreshToken200Response;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.services.AuthService;
import com.onyxdb.idm.services.jwt.JwtResponse;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;


    @Override
    public ResponseEntity<Login200Response> login(LoginRequest loginRequest) {
        JwtResponse tokens = authService.login(loginRequest.getUsername(), loginRequest.getPassword());

        Login200Response response = new Login200Response()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> logout(LogoutRequest logoutRequest) {
        authService.logout(logoutRequest.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RefreshToken200Response> refreshToken(LogoutRequest logoutRequest) {
        JwtResponse tokens = authService.refresh(logoutRequest.getRefreshToken());

        RefreshToken200Response response = new RefreshToken200Response()
                .accessToken(tokens.getAccessToken());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetCurrentUser200Response> getCurrentUser() {
        // Получаем ID текущего пользователя из контекста безопасности (заглушка)
        UUID userId = UUID.randomUUID(); // Заглушка, нужно реализовать логику

        Account account = authService.getCurrentUser(userId);
        AccountDTO accountDTO = account.toDTO();

        GetCurrentUser200Response response = new GetCurrentUser200Response()
                .id(accountDTO.getId())
                .username(accountDTO.getUsername())
                .email(accountDTO.getEmail());

        return ResponseEntity.ok(response);
    }
}