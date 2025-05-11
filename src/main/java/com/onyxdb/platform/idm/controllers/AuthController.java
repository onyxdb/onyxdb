package com.onyxdb.platform.idm.controllers;


import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.AuthApi;
import com.onyxdb.platform.generated.openapi.models.AccountDTO;
import com.onyxdb.platform.generated.openapi.models.AuthRequestDTO;
import com.onyxdb.platform.generated.openapi.models.GetCurrentUser200Response;
import com.onyxdb.platform.generated.openapi.models.JwtResponseDTO;
import com.onyxdb.platform.generated.openapi.models.RefreshTokenDTO;
import com.onyxdb.platform.idm.common.jwt.SecurityContextUtils;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.services.AccountService;
import com.onyxdb.platform.idm.services.AuthService;
import com.onyxdb.platform.idm.services.jwt.JwtResponse;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;
    private final AccountService accountService;

    @Override
    public ResponseEntity<JwtResponseDTO> login(AuthRequestDTO authRequestDTO) {
        JwtResponse tokens = authService.login(authRequestDTO.getUsername(), authRequestDTO.getPassword());
        JwtResponseDTO response = new JwtResponseDTO()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<JwtResponseDTO> generateServiceToken(AuthRequestDTO authRequestDTO) {
        JwtResponse tokens = authService.generateServiceToken(authRequestDTO.getUsername(), authRequestDTO.getPassword());
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
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetCurrentUser200Response> getCurrentUser() {
        System.out.println("getCurrentUser");
        Account account = SecurityContextUtils.getCurrentAccount();
        System.out.println("getCurrentUser context" + account.id());
        AccountDTO accountDTO = account.toDTO();

        Map<String, Optional<Map<String, Object>>> permissions = SecurityContextUtils.getCurrentPermissions();
        Map<String, Map<String, Object>> data = accountService.filterPermissionBits(permissions);
        GetCurrentUser200Response response = new GetCurrentUser200Response()
                .account(accountDTO)
                .permissions(data);
        return ResponseEntity.ok(response);
    }
}