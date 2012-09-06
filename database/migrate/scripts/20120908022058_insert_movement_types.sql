--// insert movement types
INSERT INTO sicomoro.catalog
( `group`, name, value, description, createDate, updateDate)
values
('SignoMovimiento', 'Positivo', '+', '',SYSDATE(), SYSDATE());
SELECT LAST_INSERT_ID() INTO @POSITIVE FROM DUAL;
INSERT INTO sicomoro.catalog
( `group`, name, value, description, createDate, updateDate)
values
('SignoMovimiento', 'Negativo', '-', '',SYSDATE(), SYSDATE());
SELECT LAST_INSERT_ID() INTO @NEGATIVE FROM DUAL;


INSERT INTO sicomoro.movementType (name, idcMovementSign, createDate, updateDate)
values('Alta', @POSITIVE, SYSDATE(), SYSDATE());
INSERT INTO sicomoro.movementType (name, idcMovementSign, createDate, updateDate)
values('Baja', @NEGATIVE, SYSDATE(), SYSDATE());
INSERT INTO sicomoro.movementType (name, idcMovementSign, createDate, updateDate)
values('Diezmo', @POSITIVE, SYSDATE(), SYSDATE());



--//@UNDO
delete from sicomoro.movementType;
delete from sicomoro.catalog;


