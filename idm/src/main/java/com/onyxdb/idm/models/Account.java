package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

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
        Map<String, Object> data,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static final TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
    };

    public AccountDTO toDTO() {
        return new AccountDTO()
                .id(id)
                .username(login)
                .password(password)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .data(data)
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
                accountDTO.getData(),
                accountDTO.getCreatedAt(),
                accountDTO.getUpdatedAt()
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
                accountDAO.getPassword(),
                accountDAO.getEmail(),
                accountDAO.getFirstName(),
                accountDAO.getLastName(),
                dataMap,
                accountDAO.getCreatedAt(),
                accountDAO.getUpdatedAt()
        );
    }

    public JSONB getDataAsJsonb() {
        try {
            return JSONB.valueOf(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Map to JSONB", e);
        }
    }
}