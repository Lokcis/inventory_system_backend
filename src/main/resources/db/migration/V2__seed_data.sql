-- ==========================================
-- V2__seed_data.sql
-- Initial seed data
-- ==========================================

-- =========================
-- ROLES
-- =========================
INSERT INTO roles (role_name, description)
VALUES ('ADMIN', 'System administrator'),
       ('MANAGER', 'Branch manager'),
       ('SELLER', 'Sales user');

-- =========================
-- BRANCHES
-- =========================
INSERT INTO branches (name, address, phone, is_active)
VALUES ('Main Branch', 'Centro - Sucursal Principal', '3009990000', TRUE);

-- =========================
-- USERS (ADMIN)
-- Password: 123456 (bcrypt)
-- =========================
INSERT INTO users (full_name,
                   email,
                   phone,
                   password_hash,
                   is_active,
                   two_factor_enabled,
                   role_id,
                   branch_id)
VALUES ('David Carreno',
        'davidcarreno767@gmail.com',
        '3009996083',
        '$2a$12$7.zH/zB3maRvZRrMInP1vuhBQEjkGXa.HEqwDAaCiWoE/Qbk5NAju',
        TRUE,
        FALSE,
        (SELECT role_id FROM roles WHERE role_name = 'ADMIN'),
        (SELECT branch_id FROM branches WHERE name = 'Main Branch'));
