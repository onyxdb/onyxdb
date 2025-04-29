package com.onyxdb.platform.idm.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.idm.models.clickhouse.AccountBusinessRolesHistory;
import com.onyxdb.platform.idm.models.clickhouse.AccountRolesHistory;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class ClickHouseRepository {
    private final JdbcTemplate clickHouseJdbcTemplate;

    public List<AccountRolesHistory> getAllAccountsRoleHistory() {
        return clickHouseJdbcTemplate.query("SELECT * FROM default.account_roles_history",
                (rs, rowNum) -> new AccountRolesHistory(
                        UUID.fromString(rs.getString("record_id")),
                        UUID.fromString(rs.getString("account_id")),
                        UUID.fromString(rs.getString("role_id")),
                        rs.getString("status"),
                        rs.getObject("created_at", LocalDateTime.class)
                ));
    }


    public List<AccountRolesHistory> getAllAccountRoleHistory(UUID accountId) {
        String sql = "SELECT * FROM default.account_roles_history WHERE default.account_roles_history.account_id == '" + accountId.toString() + "'";
        return clickHouseJdbcTemplate.query(sql,
                (rs, rowNum) -> new AccountRolesHistory(
                        UUID.fromString(rs.getString("record_id")),
                        UUID.fromString(rs.getString("account_id")),
                        UUID.fromString(rs.getString("role_id")),
                        rs.getString("status"),
                        rs.getObject("created_at", LocalDateTime.class)
                ));
    }

    public AccountRolesHistory addAccountRoleHistory(AccountRolesHistory data) {
        String sqlInsert = "INSERT INTO default.account_roles_history (record_id, account_id, role_id, status, created_at) VALUES (?, ?, ?, ?, now())";
        clickHouseJdbcTemplate.update(sqlInsert, data.record_id(), data.account_id(), data.role_id(), data.status());
        return data;
    }

    public List<AccountBusinessRolesHistory> getAllAccountsBusinessRoleHistory() {
        return clickHouseJdbcTemplate.query("SELECT * FROM default.account_business_roles_history",
                (rs, rowNum) -> new AccountBusinessRolesHistory(
                        UUID.fromString(rs.getString("record_id")),
                        UUID.fromString(rs.getString("account_id")),
                        UUID.fromString(rs.getString("business_role_id")),
                        rs.getString("status"),
                        rs.getObject("created_at", LocalDateTime.class)
                ));
    }

    public List<AccountBusinessRolesHistory> getAllAccountBusinessRoleHistory(UUID accountId) {
        String sql = "SELECT * FROM default.account_business_roles_history WHERE default.account_business_roles_history.account_id == '" + accountId.toString() + "'";
        return clickHouseJdbcTemplate.query(sql,
                (rs, rowNum) -> new AccountBusinessRolesHistory(
                        UUID.fromString(rs.getString("record_id")),
                        UUID.fromString(rs.getString("account_id")),
                        UUID.fromString(rs.getString("business_role_id")),
                        rs.getString("status"),
                        rs.getObject("created_at", LocalDateTime.class)
                ));
    }

    public AccountBusinessRolesHistory addAccountBusinessRoleHistory(AccountBusinessRolesHistory data) {
        String sqlInsert = "INSERT INTO default.account_business_roles_history (record_id, account_id, business_role_id, status, created_at) VALUES (?, ?, ?, ?, now())";
        clickHouseJdbcTemplate.update(sqlInsert, data.record_id(), data.account_id(), data.business_role_id(), data.status());
        return data;
    }
}
