--// table movement movementtype tax cut catalog
DROP TABLE `sicomoro`.`corte`;

-- Tabla de catalogo
CREATE  TABLE `sicomoro`.`catalog` (
  `idCatalog` INT NOT NULL AUTO_INCREMENT ,
  `group` VARCHAR(60) NOT NULL ,
  `name` VARCHAR(60) NOT NULL ,
  `value` VARCHAR(90) NOT NULL ,
  `description` VARCHAR(400) NULL ,
  `createDate` DATETIME NOT NULL ,
  `updateDate` DATETIME NOT NULL ,
  PRIMARY KEY (`idCatalog`) ,
  UNIQUE INDEX `uniqueKey` (`group` ASC, `name` ASC) )
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Tabla de corte de movimiento
CREATE  TABLE `sicomoro`.`movementCut` (
  `idMovementCut` INT NOT NULL AUTO_INCREMENT ,
  `cutDate` DATETIME NOT NULL ,
  `previousBalance` DECIMAL(10,4) NOT NULL ,
  `currentBalance` DECIMAL(10,4) NOT NULL ,
  `description` VARCHAR(400) NOT NULL ,
  `createDate` DATETIME NOT NULL ,
  `updateDate` DATETIME NOT NULL ,
  PRIMARY KEY (`idMovementCut`) )
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Tabla de tipos de movimiento
CREATE  TABLE `sicomoro`.`movementType` (
  `idMovementType` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(60) NOT NULL ,
  `idcMovementSign` INT NOT NULL ,
  `createDate` DATETIME NOT NULL ,
  `updateDate` DATETIME NOT NULL ,
  PRIMARY KEY (`idMovementType`) ,
  INDEX `fk_movementSign_1_idx` (`idcMovementSign` ASC) ,
  CONSTRAINT `fk_movementSign`
    FOREIGN KEY (`idcMovementSign` )
    REFERENCES `sicomoro`.`catalog` (`idCatalog` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Tabla de Movimientos
CREATE  TABLE `sicomoro`.`movement` (
  `idMovement` INT NOT NULL AUTO_INCREMENT ,
  `idMovementType` INT NOT NULL ,
  `amount` DECIMAL(10,4) NOT NULL ,
  `idMovementCut` INT NULL ,
  `createDate` DATETIME NOT NULL ,
  `updateDate` DATETIME NOT NULL ,
  PRIMARY KEY (`idMovement`) ,
  INDEX `fk_movement_1_idx` (`idMovementCut` ASC) ,
  CONSTRAINT `fk_movementCut`
    FOREIGN KEY (`idMovementCut` )
    REFERENCES `sicomoro`.`movementCut` (`idMovementCut` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

ALTER TABLE `sicomoro`.`movement` 
  ADD CONSTRAINT `fk_movementType`
  FOREIGN KEY (`idMovementType` )
  REFERENCES `sicomoro`.`movementType` (`idMovementType` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_movementType_idx` (`idMovementType` ASC) ;


ALTER TABLE `sicomoro`.`movement` ADD COLUMN `idContributor` INT(11) NULL  AFTER `idMovementCut` , 
  ADD CONSTRAINT `fk_movementContributor`
  FOREIGN KEY (`idContributor` )
  REFERENCES `sicomoro`.`contributor` (`idContributor` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_movement_1_idx1` (`idContributor` ASC) ;





-- Tabla de impuestos
CREATE  TABLE `sicomoro`.`tax` (
  `idTax` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(60) NOT NULL ,
  `rate` DECIMAL(10,4) NOT NULL ,
  `createDate` DATETIME NOT NULL ,
  `updateDate` DATETIME NOT NULL ,
  PRIMARY KEY (`idTax`) )
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;






--//@UNDO
-- SQL to undo the change goes here.
CREATE TABLE IF NOT EXISTS `sicomoro`.`corte` (
  `idCorte` INT NOT NULL ,
  `fechaCorte` DATETIME NOT NULL ,
  `saldoAnterior` DECIMAL(10,4) NOT NULL ,
  `saldoActual` DECIMAL(10,4) NOT NULL ,
  `usuarioCreacion` VARCHAR(60) NOT NULL ,
  `fechaCreacion` DATETIME NOT NULL ,
  `descripcion` VARCHAR(600) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL ,
  PRIMARY KEY (`idCorte`) )
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


DROP TABLE `sicomoro`.`movement`;
DROP TABLE `sicomoro`.`movementCut`;
DROP TABLE `sicomoro`.`movementType`;
DROP TABLE `sicomoro`.`tax`;
DROP TABLE `sicomoro`.`catalog`;


