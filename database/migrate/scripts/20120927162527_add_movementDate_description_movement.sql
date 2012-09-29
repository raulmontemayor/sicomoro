--// add movementDate description movement
ALTER TABLE `sicomoro`.`movement` ADD COLUMN `movementDate` DATETIME  NOT NULL AFTER `idContributor`,
 ADD COLUMN `description` VARCHAR(600)  AFTER `movementDate`;




--//@UNDO
ALTER TABLE `sicomoro`.`movement` DROP COLUMN `movementDate`, DROP COLUMN `description`;


