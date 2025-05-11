-- Создание таблицы для Запросов доступов
CREATE TABLE role_request_table (
    id UUID PRIMARY KEY,
    role_id UUID NOT NULL,
    account_id UUID NOT NULL,
    reason TEXT NOT NULL,
    owner_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
