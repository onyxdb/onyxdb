CREATE TABLE my_first_table
(
    user_id   UInt32,
    message   String,
    timestamp DateTime,
    metric    Float32
)
    ENGINE = MergeTree
        PRIMARY KEY (user_id, timestamp);

INSERT INTO my_first_table (user_id, message, timestamp, metric)
VALUES (101, 'Привет, ClickHouse!', now(), -1.0),
       (102, 'Вставить много строк за раз', yesterday(), 1.41421),
       (102, 'Сортируйте ваши данные на основе часто используемых запросов', today(), 2.718),
       (101, 'Гранулы — это самые маленькие части данных, которые читаются', now() + 5, 3.14159);

SELECT *
FROM my_first_table
ORDER BY timestamp

