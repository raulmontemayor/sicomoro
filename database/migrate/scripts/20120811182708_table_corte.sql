--// table corte
CREATE  TABLE `sicomoro`.`corte` (
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




--//@UNDO
DROP TABLE `sicomoro`.`corte`;


