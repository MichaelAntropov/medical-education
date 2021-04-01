-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema doctor_management
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema doctor_management
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `doctor_management` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `doctor_management`;

-- -----------------------------------------------------
-- Table `doctor_management`.`certificate`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`certificate`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`certificate`
(
    `id`          INT          NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(128) NULL DEFAULT NULL,
    `description` TEXT         NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`country`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`country`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`country`
(
    `id`           INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `country_code` VARCHAR(2)   NOT NULL,
    `country_name` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 247
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_description`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_description`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_description`
(
    `id`   INT  NOT NULL AUTO_INCREMENT,
    `text` TEXT NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 42
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_image`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_image`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_image`
(
    `id`   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(128) NULL DEFAULT NULL,
    `type` VARCHAR(100) NOT NULL,
    `data` LONGBLOB     NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 37
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_detail`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_detail`
(
    `id`           INT          NOT NULL AUTO_INCREMENT,
    `image`        INT UNSIGNED NULL DEFAULT NULL,
    `video_url`    VARCHAR(255) NULL DEFAULT NULL,
    `start_course` DATETIME     NULL DEFAULT NULL,
    `end_course`   DATETIME     NULL DEFAULT NULL,
    `author`       VARCHAR(128) NULL DEFAULT NULL,
    `certificate`  INT          NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FK_CERTIFICATE_idx` (`certificate` ASC) VISIBLE,
    INDEX `image` (`image` ASC) VISIBLE,
    CONSTRAINT `course_detail_ibfk_1`
        FOREIGN KEY (`image`)
            REFERENCES `doctor_management`.`course_image` (`id`)
            ON DELETE CASCADE,
    CONSTRAINT `FK_COURSE_DETAILS_CERTIFICATE_ID`
        FOREIGN KEY (`certificate`)
            REFERENCES `doctor_management`.`certificate` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 43
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course`
(
    `id`                    INT          NOT NULL AUTO_INCREMENT,
    `name`                  VARCHAR(128) NOT NULL,
    `course_details_id`     INT          NULL     DEFAULT NULL,
    `course_description_id` INT          NULL     DEFAULT NULL,
    `lesson_count`          INT          NOT NULL DEFAULT '0',
    `creation_date`         DATETIME     NULL     DEFAULT NULL,
    `edit_date`             DATETIME     NULL     DEFAULT NULL,
    `active`                TINYINT(1)   NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    INDEX `FK_DETAILS_idx` (`course_details_id` ASC) VISIBLE,
    INDEX `FK_DESCRIPTION_idx` (`course_description_id` ASC) VISIBLE,
    CONSTRAINT `FK_DESCRIPTION_ID`
        FOREIGN KEY (`course_description_id`)
            REFERENCES `doctor_management`.`course_description` (`id`),
    CONSTRAINT `FK_DETAILS_ID`
        FOREIGN KEY (`course_details_id`)
            REFERENCES `doctor_management`.`course_detail` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 40
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_lesson`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_lesson`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_lesson`
(
    `id`           INT          NOT NULL AUTO_INCREMENT,
    `course_id`    INT          NOT NULL,
    `order_number` INT          NOT NULL,
    `title`        VARCHAR(128) NULL DEFAULT NULL,
    `content`      TEXT         NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `LESSON_COURSE_ID_idx` (`course_id` ASC) INVISIBLE,
    CONSTRAINT `LESSON_COURSE_ID`
        FOREIGN KEY (`course_id`)
            REFERENCES `doctor_management`.`course` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 31
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_lesson_media`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_lesson_media`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_lesson_media`
(
    `id`        INT          NOT NULL AUTO_INCREMENT,
    `lesson_id` INT          NOT NULL,
    `name`      VARCHAR(128) NULL DEFAULT NULL,
    `type`      VARCHAR(100) NOT NULL,
    `data`      LONGBLOB     NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `LESSON_MEDIA_LESSON_ID` (`lesson_id` ASC) VISIBLE,
    CONSTRAINT `LESSON_MEDIA_LESSON_ID`
        FOREIGN KEY (`lesson_id`)
            REFERENCES `doctor_management`.`course_lesson` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 11
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`speciality`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`speciality`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`speciality`
(
    `id`   INT          NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(128) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 35
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_speciality`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_speciality`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_speciality`
(
    `course_id`     INT NOT NULL,
    `speciality_id` INT NOT NULL,
    PRIMARY KEY (`course_id`, `speciality_id`),
    INDEX `FK_COURSE_idx` (`course_id` ASC) VISIBLE,
    INDEX `FK_SPECIALITY_idx` (`speciality_id` ASC) VISIBLE,
    CONSTRAINT `FK_COURSE_ID`
        FOREIGN KEY (`course_id`)
            REFERENCES `doctor_management`.`course` (`id`),
    CONSTRAINT `FK_SPECIALITY_ID`
        FOREIGN KEY (`speciality_id`)
            REFERENCES `doctor_management`.`speciality` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_test`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_test`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_test`
(
    `id`             INT          NOT NULL AUTO_INCREMENT,
    `course_id`      INT          NOT NULL,
    `order_number`   INT          NOT NULL,
    `required`       INT          NOT NULL DEFAULT '0',
    `required_score` INT          NOT NULL DEFAULT '0',
    `title`          VARCHAR(128) NULL     DEFAULT NULL,
    `content`        TEXT         NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `TEST_COURSE_ID_idx` (`course_id` ASC) VISIBLE,
    CONSTRAINT `TEST_COURSE_ID`
        FOREIGN KEY (`course_id`)
            REFERENCES `doctor_management`.`course` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 5
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_test_question`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_test_question`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_test_question`
(
    `id`                INT  NOT NULL AUTO_INCREMENT,
    `test_id`           INT  NOT NULL,
    `order_number`      INT  NOT NULL,
    `correct_answer_id` INT  NULL DEFAULT NULL,
    `content`           TEXT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `COURSE_QUESTION_TEST_ID_idx` (`test_id` ASC) VISIBLE,
    INDEX `COURSE_QUESTION_ANSWER_ID_idx` (`correct_answer_id` ASC) VISIBLE,
    CONSTRAINT `COURSE_QUESTION_ANSWER`
        FOREIGN KEY (`correct_answer_id`)
            REFERENCES `doctor_management`.`course_test_answer` (`id`),
    CONSTRAINT `COURSE_QUESTION_TEST_ID`
        FOREIGN KEY (`test_id`)
            REFERENCES `doctor_management`.`course_test` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 11
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_test_answer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_test_answer`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_test_answer`
(
    `id`           INT  NOT NULL AUTO_INCREMENT,
    `question_id`  INT  NOT NULL,
    `order_number` INT  NOT NULL,
    `content`      TEXT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `COURSE_ANSWER_QUESTION_ID_idx` (`question_id` ASC) VISIBLE,
    CONSTRAINT `COURSE_ANSWER_QUESTION_ID`
        FOREIGN KEY (`question_id`)
            REFERENCES `doctor_management`.`course_test_question` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 22
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_test_media`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_test_media`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_test_media`
(
    `id`      INT          NOT NULL AUTO_INCREMENT,
    `test_id` INT          NOT NULL,
    `name`    VARCHAR(128) NULL DEFAULT NULL,
    `type`    VARCHAR(100) NOT NULL,
    `data`    LONGBLOB     NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `COURSE_TEST_MEDIA_TEST_ID_idx` (`test_id` ASC) VISIBLE,
    CONSTRAINT `COURSE_TEST_MEDIA_TEST_ID`
        FOREIGN KEY (`test_id`)
            REFERENCES `doctor_management`.`course_test` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 21
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`user`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`user`
(
    `id`       INT         NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(80) NOT NULL,
    `password` VARCHAR(80) NOT NULL,
    `email`    VARCHAR(80) NOT NULL,
    `active`   TINYINT(1)  NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `username` (`username` ASC, `email` ASC) VISIBLE
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 4
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_user_lesson`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_user_lesson`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_user_lesson`
(
    `user_id`   INT NOT NULL,
    `lesson_id` INT NOT NULL,
    `course_id` INT NOT NULL,
    `completed` INT NOT NULL DEFAULT '0',
    PRIMARY KEY (`user_id`, `lesson_id`),
    INDEX `COURSE_USER_LESSON_LESSON_ID` (`lesson_id` ASC) VISIBLE,
    INDEX `COURSE_USER_LESSON_COURSE_ID_idx` (`course_id` ASC) VISIBLE,
    CONSTRAINT `COURSE_USER_LESSON_COURSE_ID`
        FOREIGN KEY (`course_id`)
            REFERENCES `doctor_management`.`course` (`id`),
    CONSTRAINT `COURSE_USER_LESSON_LESSON_ID`
        FOREIGN KEY (`lesson_id`)
            REFERENCES `doctor_management`.`course_lesson` (`id`),
    CONSTRAINT `COURSE_USER_LESSON_USER_ID`
        FOREIGN KEY (`user_id`)
            REFERENCES `doctor_management`.`user` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_user_record`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_user_record`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_user_record`
(
    `user_id`           INT NOT NULL,
    `course_id`         INT NOT NULL,
    `last_order_number` INT NOT NULL DEFAULT '1',
    PRIMARY KEY (`user_id`, `course_id`),
    INDEX `FK_USER_idx` (`user_id` ASC) VISIBLE,
    INDEX `FK_COURSE_idx` (`course_id` ASC) VISIBLE,
    CONSTRAINT `FK_COURSE_USER_ID`
        FOREIGN KEY (`course_id`)
            REFERENCES `doctor_management`.`course` (`id`),
    CONSTRAINT `FK_USER_COURSE_ID`
        FOREIGN KEY (`user_id`)
            REFERENCES `doctor_management`.`user` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`course_user_test`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`course_user_test`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`course_user_test`
(
    `user_id`   INT NOT NULL,
    `test_id`   INT NOT NULL,
    `course_id` INT NOT NULL,
    `completed` INT NOT NULL DEFAULT '0',
    `score`     INT NOT NULL DEFAULT '0',
    PRIMARY KEY (`test_id`, `user_id`),
    INDEX `COURSE_USER_TEST_USER_ID_idx` (`user_id` ASC) VISIBLE,
    INDEX `COURSE_USER_TEST_COURSE_ID` (`course_id` ASC) VISIBLE,
    CONSTRAINT `COURSE_USER_TEST_COURSE_ID`
        FOREIGN KEY (`course_id`)
            REFERENCES `doctor_management`.`course` (`id`),
    CONSTRAINT `COURSE_USER_TEST_TEST_ID`
        FOREIGN KEY (`test_id`)
            REFERENCES `doctor_management`.`course_test` (`id`),
    CONSTRAINT `COURSE_USER_TEST_USER_ID`
        FOREIGN KEY (`user_id`)
            REFERENCES `doctor_management`.`user` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`role`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`role`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name` (`name` ASC) VISIBLE
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 3
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`user_certificate`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`user_certificate`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`user_certificate`
(
    `user_id`        INT NOT NULL,
    `certificate_id` INT NOT NULL,
    PRIMARY KEY (`user_id`, `certificate_id`),
    INDEX `FK_USER_idx` (`user_id` ASC) VISIBLE,
    INDEX `FK_SERTIFICATE_idx` (`certificate_id` ASC) VISIBLE,
    CONSTRAINT `FK_CERTIFICATE_ID`
        FOREIGN KEY (`certificate_id`)
            REFERENCES `doctor_management`.`certificate` (`id`),
    CONSTRAINT `FK_USER_CERTIFICATE_ID`
        FOREIGN KEY (`user_id`)
            REFERENCES `doctor_management`.`user` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`user_profile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`user_profile`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`user_profile`
(
    `id`            INT          NOT NULL AUTO_INCREMENT,
    `user_id`       INT          NULL DEFAULT NULL,
    `first_name`    VARCHAR(128) NULL DEFAULT NULL,
    `last_name`     VARCHAR(128) NULL DEFAULT NULL,
    `middle_name`   VARCHAR(128) NULL DEFAULT NULL,
    `phone`         VARCHAR(20)  NULL DEFAULT NULL,
    `birth_date`    DATETIME     NULL DEFAULT NULL,
    `country_id`    INT UNSIGNED NULL DEFAULT NULL,
    `region_state`  VARCHAR(128) NULL DEFAULT NULL,
    `city`          VARCHAR(128) NULL DEFAULT NULL,
    `speciality_id` INT          NULL DEFAULT NULL,
    `workplace`     VARCHAR(128) NULL DEFAULT NULL,
    `position`      VARCHAR(128) NULL DEFAULT NULL,
    `filled`        TINYINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `user_id` (`user_id` ASC) VISIBLE,
    INDEX `country` (`country_id` ASC) VISIBLE,
    INDEX `speciality` (`speciality_id` ASC) VISIBLE,
    CONSTRAINT `user_profile_ibfk_1`
        FOREIGN KEY (`user_id`)
            REFERENCES `doctor_management`.`user` (`id`),
    CONSTRAINT `user_profile_ibfk_2`
        FOREIGN KEY (`country_id`)
            REFERENCES `doctor_management`.`country` (`id`),
    CONSTRAINT `user_profile_ibfk_3`
        FOREIGN KEY (`speciality_id`)
            REFERENCES `doctor_management`.`speciality` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 4
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `doctor_management`.`user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_management`.`user_role`;

CREATE TABLE IF NOT EXISTS `doctor_management`.`user_role`
(
    `user_id` INT NOT NULL,
    `role_id` INT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    INDEX `FK_ROLE_idx` (`role_id` ASC) VISIBLE,
    INDEX `FK_USER_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FK_ROLE_ID`
        FOREIGN KEY (`role_id`)
            REFERENCES `doctor_management`.`role` (`id`),
    CONSTRAINT `FK_USER_ID`
        FOREIGN KEY (`user_id`)
            REFERENCES `doctor_management`.`user` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
