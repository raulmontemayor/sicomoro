--// table contributor
CREATE  TABLE `sicomoro`.`contributor` (
  `idContributor` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(60) NOT NULL ,
  `lastName` VARCHAR(60) NOT NULL ,
  `secondLastName` VARCHAR(60) NULL ,
  `createDate` DATETIME NOT NULL ,
  `updateDate` DATETIME NOT NULL ,
  PRIMARY KEY (`idContributor`) );




--//@UNDO
DROP TABLE `sicomoro`.`contributor`;


