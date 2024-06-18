CREATE SCHEMA IF NOT EXISTS `biblioteca` DEFAULT CHARACTER SET utf8 ;
USE `biblioteca` ;

-- -----------------------------------------------------
-- Table `biblioteca`.`Dono`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`Dono` (
  `Nome` VARCHAR(45) NOT NULL,
  `CPF` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`CPF`));

-- -----------------------------------------------------
-- Table `biblioteca`.`Biblioteca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`Biblioteca` (
  `Nome` VARCHAR(45) NOT NULL,
  `Cidade` VARCHAR(45) NOT NULL,
  `Dono_CPF` VARCHAR(45) NOT NULL,
  `idBiblioteca` INT NOT NULL,
  PRIMARY KEY (`idBiblioteca`),
  CONSTRAINT `fk_Livraria_Dono1`
    FOREIGN KEY (`Dono_CPF`)
    REFERENCES `biblioteca`.`Dono` (`CPF`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `biblioteca`.`Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`Cliente` (
  `Nome` VARCHAR(45) NOT NULL,
  `Matricula` INT NOT NULL,
  PRIMARY KEY (`Matricula`));


-- -----------------------------------------------------
-- Table `biblioteca`.`Livro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`Livro` (
  `Numero` INT NOT NULL,
  `Nome` VARCHAR(45) NOT NULL,
  `Cliente_Matricula` INT NOT NULL,
  PRIMARY KEY (`Numero`),
  CONSTRAINT `fk_Livro_Cliente1`
    FOREIGN KEY (`Cliente_Matricula`)
    REFERENCES `biblioteca`.`Cliente` (`Matricula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table `biblioteca`.`Biblioteca_has_Livro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`Biblioteca_has_Livro` (
  `Biblioteca_idBiblioteca` INT NOT NULL,
  `Livro_Numero` INT NOT NULL,
  PRIMARY KEY (`Biblioteca_idBiblioteca`, `Livro_Numero`),
  CONSTRAINT `fk_Biblioteca_has_Livro_Biblioteca1`
    FOREIGN KEY (`Biblioteca_idBiblioteca`)
    REFERENCES `biblioteca`.`Biblioteca` (`idBiblioteca`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Biblioteca_has_Livro_Livro1`
    FOREIGN KEY (`Livro_Numero`)
    REFERENCES `biblioteca`.`Livro` (`Numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
