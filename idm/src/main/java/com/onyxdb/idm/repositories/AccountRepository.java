package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.AccountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface AccountRepository {
    Optional<AccountDTO> findById(UUID id);

    List<AccountDTO> findAll();

    void create(AccountDTO account);

    void update(AccountDTO account);

    void delete(UUID id);
}
