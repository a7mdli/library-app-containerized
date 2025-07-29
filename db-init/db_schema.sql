-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema library_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema library_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `library_db` DEFAULT CHARACTER SET utf8 ;
USE `library_db` ;

-- -----------------------------------------------------
-- Table `library_db`.`USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_db`.`USER` (
  `id` VARCHAR(36) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- Insert default admin only if table is empty
INSERT INTO USER (id, name, role)
SELECT 'u1', 'root admin', 'admin'
WHERE NOT EXISTS (SELECT 1 FROM USER);


-- -----------------------------------------------------
-- Table `library_db`.`BOOK`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_db`.`BOOK` (
  `id` VARCHAR(36) NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `author` VARCHAR(100) NOT NULL DEFAULT 'N/A',
  `genre` VARCHAR(80) NOT NULL DEFAULT 'N/A',
  `available_copies` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_db`.`BORROWS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_db`.`BORROWS` (
  `user_id` VARCHAR(36) NOT NULL,
  `book_id` VARCHAR(36) NOT NULL,
  PRIMARY KEY (`user_id`, `book_id`),
  INDEX `book_id_idx` (`book_id` ASC) VISIBLE,
  CONSTRAINT `user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `library_db`.`USER` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book_id`
    FOREIGN KEY (`book_id`)
    REFERENCES `library_db`.`BOOK` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
