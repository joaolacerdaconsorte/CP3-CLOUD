-- ========================================
-- VaultBank — Script de inicialização MySQL
-- Cria a estrutura caso não exista
-- ========================================

USE vaultbank;

-- A criação das tabelas é gerenciada pelo Hibernate (ddl-auto=update)
-- Este script serve para customizações adicionais, se necessário

-- Garantir charset UTF-8
ALTER DATABASE vaultbank CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
