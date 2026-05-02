-- =====================================================
-- UrlShortCut: Схема базы данных
-- Версия: 1.0
-- =====================================================

-- 1. Таблица зарегистрированных сайтов
CREATE TABLE IF NOT EXISTS sites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Первичный ключ',
    site_name VARCHAR(255) NOT NULL UNIQUE COMMENT 'Название сайта (например, job4j.ru)',
    login VARCHAR(255) NOT NULL UNIQUE COMMENT 'Уникальный логин для API',
    password_hash VARCHAR(255) NOT NULL COMMENT 'Хеш пароля (пока в открытом виде)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Дата регистрации'
);

-- 2. Таблица сокращённых ссылок
CREATE TABLE IF NOT EXISTS url_mappings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Первичный ключ',
    original_url VARCHAR(2048) NOT NULL COMMENT 'Исходная длинная ссылка',
    short_code VARCHAR(10) NOT NULL UNIQUE COMMENT 'Уникальный короткий код (7-10 символов)',
    site_id BIGINT NOT NULL COMMENT 'Владелец ссылки',
    clicks BIGINT NOT NULL DEFAULT 0 COMMENT 'Счётчик переходов',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Дата создания',
    FOREIGN KEY (site_id) REFERENCES sites(id) ON DELETE CASCADE
);

-- 3. Индексы для производительности
CREATE INDEX idx_url_mappings_short_code ON url_mappings(short_code);
CREATE INDEX idx_url_mappings_site_id ON url_mappings(site_id);
CREATE INDEX idx_sites_login ON sites(login);

-- 4. Комментарии к таблицам
COMMENT ON TABLE sites IS 'Зарегистрированные сайты-клиенты';
COMMENT ON TABLE url_mappings IS 'Сокращённые ссылки и статистика';