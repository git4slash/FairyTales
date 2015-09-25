SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema fairytales
-- -----------------------------------------------------
-- Schema for Old man's stories project from Geniusbytes
DROP SCHEMA IF EXISTS `fairytales` ;
CREATE SCHEMA IF NOT EXISTS `fairytales` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `fairytales` ;

-- -----------------------------------------------------
-- Table `fairytales`.`story`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fairytales`.`story` ;

CREATE TABLE IF NOT EXISTS `fairytales`.`story` (
  `id_story` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `story_name` VARCHAR(45) NOT NULL,
  `story_text` TEXT NOT NULL,
  PRIMARY KEY (`id_story`))
ENGINE = InnoDB
COMMENT = 'maintain stories from respective authors';


-- -----------------------------------------------------
-- Table `fairytales`.`account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fairytales`.`account` ;

CREATE TABLE IF NOT EXISTS `fairytales`.`account` (
  `id_aacount` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NULL,
  `author_of` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_aacount`),
  INDEX `subscription` (`author_of` ASC),
  CONSTRAINT `fk_account_story`
    FOREIGN KEY (`author_of`)
    REFERENCES `fairytales`.`story` (`id_story`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'maintains account details';


-- -----------------------------------------------------
-- Table `fairytales`.`stories_in_subscription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fairytales`.`stories_in_subscription` ;

CREATE TABLE IF NOT EXISTS `fairytales`.`stories_in_subscription` (
  `subscriber` INT UNSIGNED NOT NULL,
  `subscribed_to` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`subscriber`, `subscribed_to`),
  INDEX `fk_account_has_story_story1_idx` (`subscribed_to` ASC),
  INDEX `fk_account_has_story_account1_idx` (`subscriber` ASC),
  CONSTRAINT `fk_account_has_story_account1`
    FOREIGN KEY (`subscriber`)
    REFERENCES `fairytales`.`account` (`id_aacount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_account_has_story_story1`
    FOREIGN KEY (`subscribed_to`)
    REFERENCES `fairytales`.`story` (`id_story`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
