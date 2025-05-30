-- MySQL Workbench Synchronization
-- Mejoras aplicadas al modelo

CREATE SCHEMA IF NOT EXISTS `nottoday_db` DEFAULT CHARACTER SET utf8;

CREATE TABLE IF NOT EXISTS `nottoday_db`.`users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(250) NOT NULL,
    `password` VARCHAR(250) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `pic` LONGBLOB NULL DEFAULT NULL,
    `description` VARCHAR(255) NULL DEFAULT NULL,
    `area` VARCHAR(255) NULL DEFAULT NULL,
    UNIQUE INDEX `email_unique` (`email` ASC),
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `nottoday_db`.`teams` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `team_code` VARCHAR(10) NOT NULL UNIQUE,
    `name` VARCHAR(255) NOT NULL,
    `imagen` LONGBLOB NULL,
    `creation_date` DATE NOT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `nottoday_db`.`tasks` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL DEFAULT NULL,
    `limit_date` DATE NOT NULL,
    `limit_hour` TIME NOT NULL,
    `state` VARCHAR(100) NOT NULL,
    `team_id` BIGINT NOT NULL,
    `creator_id` BIGINT NOT NULL,
    `assigned_id` BIGINT NOT NULL,
    `created_at` DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `fk_tasks_teams_idx` (`team_id` ASC),
    INDEX `fk_tasks_users1_idx` (`creator_id` ASC),
    INDEX `fk_tasks_users2_idx` (`assigned_id` ASC),
    CONSTRAINT `fk_tasks_teams`
    FOREIGN KEY (`team_id`)
    REFERENCES `nottoday_db`.`teams` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `fk_tasks_users1`
    FOREIGN KEY (`creator_id`)
    REFERENCES `nottoday_db`.`users` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `fk_tasks_users2`
    FOREIGN KEY (`assigned_id`)
    REFERENCES `nottoday_db`.`users` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `nottoday_db`.`bills` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `amount` DECIMAL(10,2) NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    `team_id` BIGINT NOT NULL,
    `payer_id` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `fk_bills_teams_idx` (`team_id` ASC),
    INDEX `fk_bills_users_idx` (`payer_id` ASC),
    CONSTRAINT `fk_bills_teams`
    FOREIGN KEY (`team_id`)
    REFERENCES `nottoday_db`.`teams` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `fk_bills_users`
    FOREIGN KEY (`payer_id`)
    REFERENCES `nottoday_db`.`users` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `nottoday_db`.`users_teams` (
    `user_id` BIGINT NOT NULL,
    `team_id` BIGINT NOT NULL,
    `role` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`user_id`, `team_id`),
    INDEX `fk_users_teams_teams_idx` (`team_id` ASC),
    INDEX `fk_users_teams_users_idx` (`user_id` ASC),
    CONSTRAINT `fk_users_teams_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `nottoday_db`.`users` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `fk_users_teams_teams`
    FOREIGN KEY (`team_id`)
    REFERENCES `nottoday_db`.`teams` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `nottoday_db`.`bills_users` (
    `bill_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `owed` DECIMAL(10,2) NOT NULL,
    `paid` DECIMAL(10,2) NULL DEFAULT 0,
    `payment_date` DATE NULL DEFAULT NULL,
    PRIMARY KEY (`bill_id`, `user_id`),
    INDEX `fk_bills_users_users_idx` (`user_id` ASC),
    INDEX `fk_bills_users_bills_idx` (`bill_id` ASC),
    CONSTRAINT `fk_bills_users_bills`
    FOREIGN KEY (`bill_id`)
    REFERENCES `nottoday_db`.`bills` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `fk_bills_users_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `nottoday_db`.`users` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `nottoday_db`.`comments` (
    `user_id` BIGINT NOT NULL,
    `commenter_id` BIGINT NOT NULL,
    `score` INT NOT NULL,
    `comment` VARCHAR(255) NOT NULL,
    `comment_date` DATE NOT NULL,
    `anonymous` BOOLEAN NOT NULL DEFAULT 0,
    PRIMARY KEY (`user_id`, `commenter_id`),
    INDEX `fk_comments_users2_idx` (`commenter_id` ASC),
    CONSTRAINT `fk_comments_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `nottoday_db`.`users` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `fk_comments_users2`
    FOREIGN KEY (`commenter_id`)
    REFERENCES `nottoday_db`.`users` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;