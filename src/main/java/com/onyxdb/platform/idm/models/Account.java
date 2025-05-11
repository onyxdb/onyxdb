package com.onyxdb.platform.idm.models;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

import com.onyxdb.platform.generated.jooq.tables.records.AccountTableRecord;
import com.onyxdb.platform.generated.openapi.models.AccountDTO;
import com.onyxdb.platform.generated.openapi.models.AccountPostDTO;

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
        Map<String, Object> data,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    static final TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
    };
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Account fromDTO(AccountDTO accountDTO) {
        return new Account(
                accountDTO.getId(),
                accountDTO.getUsername(),
                accountDTO.getPassword(),
                accountDTO.getEmail(),
                accountDTO.getFirstName(),
                accountDTO.getLastName(),
                accountDTO.getData(),
                accountDTO.getCreatedAt(),
                accountDTO.getUpdatedAt()
        );
    }

    public static Account fromPostDTO(AccountPostDTO accountDTO) {
        return new Account(
                null,
                accountDTO.getUsername(),
                accountDTO.getPassword(),
                accountDTO.getEmail(),
                accountDTO.getFirstName(),
                accountDTO.getLastName(),
                accountDTO.getData(),
                null,
                null
        );
    }

    public static Account fromDAO(AccountTableRecord accountDAO) {
        Map<String, Object> dataMap = null;
        try {
            dataMap = objectMapper.readValue(accountDAO.getData().data(), typeRef);
        } catch (JsonProcessingException ignored) {
        }
        return new Account(
                accountDAO.getId(),
                accountDAO.getLogin(),
//                null,
                accountDAO.getPassword(),
                accountDAO.getEmail(),
                accountDAO.getFirstName(),
                accountDAO.getLastName(),
                dataMap,
                accountDAO.getCreatedAt(),
                accountDAO.getUpdatedAt()
        );
    }

    public AccountDTO toDTO() {
        return new AccountDTO()
                .id(id)
                .username(login)
                .password(null)
//                .password(password)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .data(data)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public JSONB getDataAsJsonb() {
        try {
            return JSONB.valueOf(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Map to JSONB", e);
        }
    }
}