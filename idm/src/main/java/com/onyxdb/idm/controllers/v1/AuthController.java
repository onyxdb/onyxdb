package com.onyxdb.idm.controllers.v1;


import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.idm.common.jwt.SecurityContextUtil;
import com.onyxdb.idm.generated.openapi.apis.AuthApi;
import com.onyxdb.idm.generated.openapi.models.AccountDTO;
import com.onyxdb.idm.generated.openapi.models.AuthRequestDTO;
import com.onyxdb.idm.generated.openapi.models.GetCurrentUser200Response;
import com.onyxdb.idm.generated.openapi.models.JwtResponseDTO;
import com.onyxdb.idm.generated.openapi.models.RefreshTokenDTO;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.services.AccountService;
import com.onyxdb.idm.services.AuthService;
import com.onyxdb.idm.services.jwt.JwtResponse;

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
        Account account = SecurityContextUtil.getCurrentAccount();
        AccountDTO accountDTO = account.toDTO();

        Map<String, Map<String, Object>> data = accountService.getAllPermissionBitsResponse(account.id());
        GetCurrentUser200Response response = new GetCurrentUser200Response()
                .account(accountDTO)
                .permissions(data);

        return ResponseEntity.ok(response);
    }
}