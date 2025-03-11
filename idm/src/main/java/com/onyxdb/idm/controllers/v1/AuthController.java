package com.onyxdb.idm.controllers.v1;


import java.util.List;

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

        GetCurrentUser200Response response = new GetCurrentUser200Response()
                .account(accountDTO)
                .permissions(List.of(
                        "web-global-domain-component-create",
                        "web-global-domain-component-edit",
                        "web-global-domain-component-delete",
                        "web-global-organization-unit-create",
                        "web-global-organization-unit-edit",
                        "web-global-organization-unit-delete",
                        "web-global-business-role-create",
                        "web-global-business-role-edit",
                        "web-global-business-role-delete",
                        "web-global-role-create",
                        "web-global-role-edit",
                        "web-global-role-delete",
                        "web-global-role-request-edit",
                        "web-global-account-create",
                        "web-global-account-edit",
                        "web-global-account-delete",
                        "web-global-product-create",
                        "web-global-product-edit",
                        "web-global-product-delete",
                        "web-product-123-edit",
                        "web-product-123-view"
                ));

        return ResponseEntity.ok(response);
    }
}