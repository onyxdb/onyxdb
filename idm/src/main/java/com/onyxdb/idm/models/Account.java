package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.jooq.tables.records.AccountTableRecord;
import com.onyxdb.idm.generated.openapi.models.AccountDTO;

/**
 * @author ArtemFed
 */
public record Account(
        UUID id,
        String login,
        String password,
        String email,
        String firstName,
        String lastName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public AccountDTO toDTO() {
        return new AccountDTO()
                .id(id)
                .username(login)
                .password(password)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public static Account fromDTO(AccountDTO accountDTO) {
        return new Account(
                accountDTO.getId(),
                accountDTO.getUsername(),
                accountDTO.getPassword(),
                accountDTO.getEmail(),
                accountDTO.getFirstName(),
                accountDTO.getLastName(),
                accountDTO.getCreatedAt(),
                accountDTO.getUpdatedAt()
        );
    }

    public static Account fromDAO(AccountTableRecord accountDAO) {
        return new Account(
                accountDAO.getId(),
                accountDAO.getLogin(),
                accountDAO.getPassword(),
                accountDAO.getEmail(),
                accountDAO.getFirstName(),
                accountDAO.getLastName(),
                accountDAO.getCreatedAt(),
                accountDAO.getUpdatedAt()
        );
    }
}