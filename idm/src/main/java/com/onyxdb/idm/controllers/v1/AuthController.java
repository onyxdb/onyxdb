package com.onyxdb.idm.controllers.v1;


import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.idm.generated.openapi.apis.AuthApi;
import com.onyxdb.idm.generated.openapi.models.AccountDTO;
import com.onyxdb.idm.generated.openapi.models.AuthRequestDTO;
import com.onyxdb.idm.generated.openapi.models.GetCurrentUser200Response;
import com.onyxdb.idm.generated.openapi.models.JwtResponseDTO;
import com.onyxdb.idm.generated.openapi.models.RefreshTokenDTO;
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
    public ResponseEntity<JwtResponseDTO> login(AuthRequestDTO authRequestDTO) {
        JwtResponse tokens = authService.login(authRequestDTO.getUsername(), authRequestDTO.getPassword());

        JwtResponseDTO response = new JwtResponseDTO()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> logout(RefreshTokenDTO refreshTokenDTO) {
        authService.logout(refreshTokenDTO.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<JwtResponseDTO> refreshToken(RefreshTokenDTO refreshTokenDTO) {
        JwtResponse tokens = authService.refresh(refreshTokenDTO.getRefreshToken());

        JwtResponseDTO response = new JwtResponseDTO()
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
                .account(accountDTO)
                .permissions(List.of());

        return ResponseEntity.ok(response);
    }
}