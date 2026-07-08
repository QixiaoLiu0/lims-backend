-- ============================================================
-- ARIS LIMS - Full Schema
-- Tables: user, sample_type, test_type, coc, sample, parameter,
--         test, result
--
-- Run against a fresh database:
--   mysql -u root -p aris_lims < schema.sql
--
-- Safe to re-run: drops tables first, in child-before-parent
-- order, so foreign keys never block the drop.
-- ============================================================

CREATE DATABASE IF NOT EXISTS `aris_lims`;
USE `aris_lims`;

-- Drop order: children first (tables holding the FKs), then parents
DROP TABLE IF EXISTS `result`;
DROP TABLE IF EXISTS `test`;
DROP TABLE IF EXISTS `sample`;
DROP TABLE IF EXISTS `coc`;
DROP TABLE IF EXISTS `parameter`;
DROP TABLE IF EXISTS `test_type`;
DROP TABLE IF EXISTS `sample_type`;
DROP TABLE IF EXISTS `user`;

-- ------------------------------------------------------------
-- USER
-- Self-referencing FK (created_by_user_id -> user_id) is allowed
-- within a single CREATE TABLE since the column and the table's
-- own PK are declared together.
-- ------------------------------------------------------------
CREATE TABLE `user` (
    user_id                CHAR(36)     NOT NULL PRIMARY KEY,
    created_by_user_id     CHAR(36)     NULL,
    email                  VARCHAR(254) NOT NULL,
    pwd_hash               VARCHAR(255) NOT NULL,
    first_name             VARCHAR(50)  NOT NULL,
    last_name              VARCHAR(50)  NOT NULL,
    role                   VARCHAR(30)  NOT NULL,
    status                 TINYINT      NOT NULL DEFAULT 1,
    must_change_password   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at             DATETIME     NOT NULL,
    CONSTRAINT fk_user_created_by
        FOREIGN KEY (created_by_user_id) REFERENCES `user`(user_id)
);

-- ------------------------------------------------------------
-- SAMPLE_TYPE (lookup table)
-- ------------------------------------------------------------
CREATE TABLE `sample_type` (
    sample_type_id   INT AUTO_INCREMENT PRIMARY KEY,
    sample_type_name VARCHAR(50) NOT NULL
);

-- ------------------------------------------------------------
-- TEST_TYPE (main table, already in use from LIMS-102)
-- ------------------------------------------------------------
CREATE TABLE `test_type` (
    test_type_id     INT AUTO_INCREMENT PRIMARY KEY,
    type_name        VARCHAR(100)  NOT NULL,
    description      VARCHAR(255)  NOT NULL,
    required_volume  DECIMAL(10,2) NOT NULL,
    bg_color         VARCHAR(10)   NOT NULL,
    icon_color       VARCHAR(10)   NOT NULL,
    border_color     VARCHAR(10)   NOT NULL,
    is_active        TINYINT       NOT NULL
);

-- ------------------------------------------------------------
-- COC (Chain of Custody)
-- ------------------------------------------------------------
CREATE TABLE `coc` (
    coc_id                CHAR(36)     NOT NULL PRIMARY KEY,
    coc_number            VARCHAR(30)  NOT NULL,
    project_name          VARCHAR(100) NOT NULL,
    report_to_name1       VARCHAR(100) NOT NULL,
    report_to_email1      VARCHAR(254) NOT NULL,
    report_to_name2       VARCHAR(100) NULL,
    report_to_email2      VARCHAR(254) NULL,
    date_required         DATETIME     NOT NULL,
    is_rush               TINYINT      NOT NULL,
    date_for_rush         DATETIME     NULL,
    received_by           VARCHAR(100) NULL,
    received_time         DATETIME     NULL,
    relinquished_by       VARCHAR(100) NULL,
    relinquished_time     DATETIME     NULL,
    number_of_containers  INT          NOT NULL,
    special_instructions  VARCHAR(500) NULL,
    created_by_user_id    CHAR(36)     NOT NULL,
    created_at             DATETIME    NOT NULL,
    status                VARCHAR(30)  NOT NULL DEFAULT 'In-Progress',
    CONSTRAINT fk_coc_created_by
        FOREIGN KEY (created_by_user_id) REFERENCES `user`(user_id)
);

-- ------------------------------------------------------------
-- SAMPLE
-- ------------------------------------------------------------
CREATE TABLE `sample` (
    sample_id                  CHAR(36)     NOT NULL PRIMARY KEY,
    coc_id                     CHAR(36)     NOT NULL,
    sample_type_id             INT          NOT NULL,
    sample_client_id           VARCHAR(100) NOT NULL,
    sampled_time               DATETIME     NOT NULL,
    sampling_point             VARCHAR(30)  NOT NULL,
    matrix                     VARCHAR(50)  NOT NULL,
    number_of_containers       INT          NULL DEFAULT 1,
    remarks                    VARCHAR(255) NOT NULL,
    initial_volume             DECIMAL(10,2) NOT NULL,
    remaining_volume           DECIMAL(10,2) NOT NULL,
    created_at                 DATETIME     NOT NULL,
    is_filtered                TINYINT      NOT NULL DEFAULT 0,
    is_preserved               TINYINT      NOT NULL DEFAULT 0,
    is_filtered_and_preserved  TINYINT      NOT NULL DEFAULT 0,
    status                     VARCHAR(30)  NOT NULL DEFAULT 'In-Progress',
    CONSTRAINT fk_sample_coc
        FOREIGN KEY (coc_id) REFERENCES `coc`(coc_id),
    CONSTRAINT fk_sample_sample_type
        FOREIGN KEY (sample_type_id) REFERENCES `sample_type`(sample_type_id)
);

-- ------------------------------------------------------------
-- PARAMETER (sub-table, FK -> test_type, already in use)
-- `limit` kept as-is per team decision (not renamed to sample_limit)
-- ------------------------------------------------------------
CREATE TABLE `parameter` (
    parameter_id    INT AUTO_INCREMENT PRIMARY KEY,
    test_type_id    INT          NOT NULL,
    parameter_name  VARCHAR(50)  NOT NULL,
    unit            VARCHAR(50)  NOT NULL,
    `limit`         VARCHAR(50)  NULL,
    CONSTRAINT fk_parameter_test_type
        FOREIGN KEY (test_type_id) REFERENCES test_type(test_type_id)
);

-- ------------------------------------------------------------
-- TEST
-- ------------------------------------------------------------
CREATE TABLE `test` (
    test_id        CHAR(36)     NOT NULL PRIMARY KEY,
    sample_id      CHAR(36)     NOT NULL,
    test_type_id   INT          NOT NULL,
    status         VARCHAR(30)  NOT NULL DEFAULT 'In-Progress',
    created_at     DATETIME     NOT NULL,
    run_number     INT          NOT NULL DEFAULT 0,
    retest_reason  VARCHAR(255) NULL,
    CONSTRAINT fk_test_sample
        FOREIGN KEY (sample_id) REFERENCES `sample`(sample_id),
    CONSTRAINT fk_test_test_type
        FOREIGN KEY (test_type_id) REFERENCES test_type(test_type_id)
);

-- ------------------------------------------------------------
-- RESULT
-- Note: result_id is CHAR(36) (UUID generated in application code),
-- so it cannot use AUTO_INCREMENT -- that only applies to integer
-- types in MySQL. The ERD's {A++} notation refers to app-level
-- generation, not a DB-level identity column.
-- ------------------------------------------------------------
CREATE TABLE `result` (
    result_id            CHAR(36)     NOT NULL PRIMARY KEY,
    test_id              CHAR(36)     NOT NULL,
    parameter_id         INT          NOT NULL,
    value                VARCHAR(50)  NULL,
    created_at           DATETIME     NOT NULL,
    created_by_user_id   CHAR(36)     NOT NULL,
    updated_by_user_id   CHAR(36)     NULL,
    updated_at           DATETIME     NULL,
    qualifier            VARCHAR(10)  NOT NULL,
    CONSTRAINT fk_result_test
        FOREIGN KEY (test_id) REFERENCES `test`(test_id),
    CONSTRAINT fk_result_parameter
        FOREIGN KEY (parameter_id) REFERENCES parameter(parameter_id),
    CONSTRAINT fk_result_created_by
        FOREIGN KEY (created_by_user_id) REFERENCES `user`(user_id),
    CONSTRAINT fk_result_updated_by
        FOREIGN KEY (updated_by_user_id) REFERENCES `user`(user_id)
);
