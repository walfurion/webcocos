/*Para ampliacion de las estaciones COCO*/
ALTER TABLE lectura ADD nombre_pistero VARCHAR2(50);
ALTER TABLE lectura ADD nombre_jefe VARCHAR2(50);
UPDATE lectura set nombre_pistero = 'name', nombre_jefe = 'nameJefe';
COMMIT;
ALTER TABLE lectura MODIFY nombre_pistero VARCHAR2(50) NOT NULL;
ALTER TABLE lectura MODIFY nombre_jefe VARCHAR2(50) NOT NULL;

CREATE TABLE parametro (
    parametro_id NUMBER(2) PRIMARY KEY NOT NULL,
    nombre VARCHAR2(50) NOT NULL,
    valor VARCHAR2(350) NOT NULL,
    descripcion VARCHAR2(350),
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL
);
INSERT INTO parametro (parametro_id, nombre, valor, creado_por, descripcion) VALUES (10, 'CORREO_NOTIFICACION_IP_PORT', '192.200.107.34||25', 'hgbarrientos', 'Datos de IP y puerto del servidor, separados con DOBLE PIPE, desde el cual se enviaran correos de notificacion de la aplicacion Web COCO.');
INSERT INTO parametro (parametro_id, nombre, valor, creado_por, descripcion) VALUES (11, 'CORREO_NOTIFICACION_AUTH', 'notificaciones@uno-terra.com||Fl0t@.15', 'hgbarrientos', 'Datos de la cuenta de correo y clave, separados con DOBLE PIPE, desde al cual se enviaran los correos de notificacion de la Web COCO.');
INSERT INTO parametro (parametro_id, nombre, valor, creado_por, descripcion) VALUES (12, 'CORREO_CALIBRACIONES_COSTARICA', 'calibraciones.cr@uno-terra.com,hgbarrientos@fundamentalsoftwr.com', 'hgbarrientos', 'Lista de correos separados por coma, a los cuales se enviaran los correos de calibraciones para COSTA RICA.');
INSERT INTO parametro (parametro_id, nombre, valor, creado_por, descripcion) VALUES (13, 'CORREO_CALIBRACIONES_NICARAGUA', 'hgbarrientos@fundamentalsoftwr.com', 'hgbarrientos', 'Lista de correos separados por coma, a los cuales se enviaran los correos de calibraciones para NICARAGUA.');
COMMIT;



DROP TABLE mediopago_pais;
ALTER TABLE mediopago ADD pais_id NUMBER(3);
UPDATE mediopago SET pais_id=188;
ALTER TABLE mediopago MODIFY pais_id NUMBER(3) NOT NULL;
ALTER TABLE mediopago ADD orden NUMBER(2) DEFAULT 99 NOT NULL;
ALTER TABLE mediopago ADD tipoprod_id NUMBER(2);
UPDATE mediopago SET tipoprod_id = 1;
ALTER TABLE mediopago MODIFY tipoprod_id NUMBER(2) NOT NULL;
ALTER TABLE mediopago MODIFY mediopago_id NUMBER(3) NOT NULL;
ALTER TABLE mediopago ADD partidacont_por NUMBER(6,3) DEFAULT 0 NOT NULL;
ALTER TABLE mediopago ADD partidacont NUMBER(1) DEFAULT 0 NOT NULL;

DROP SEQUENCE mediopago_seq;
CREATE SEQUENCE mediopago_seq MINVALUE 35 MAXVALUE 999 INCREMENT BY 1 START WITH 35 CACHE 10 ORDER  NOCYCLE;

--delete from arqueocaja_detalle; delete from ARQUEOCAJA_BOMBA; delete from efectivo; delete from  arqueocaja;
delete from mediopago;

INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre, partidacont) VALUES (05, 188, 1, 1, 'hgbarrientos', 1, 'Ventas al credito', 1);
INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre, partidacont) VALUES (06, 188, 1, 1, 'hgbarrientos', 12, 'Ventas prepago', 1);
INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre, partidacont) VALUES (07, 188, 1, 1, 'hgbarrientos', 13, 'CxC empleado', 1);
INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre, partidacont) VALUES (08, 188, 1, 2, 'hgbarrientos', 2, 'Efectivo USD', 1);
	INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (10, 222, 1, 1, 'hgbarrientos', 1, 'Ventas al credito');
	INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (11, 222, 1, 1, 'hgbarrientos', 2, 'CxC empleado');
	INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (12, 222, 1, 1, 'hgbarrientos', 3, 'Ventas prepago');
	INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (13, 222, 1, 2, 'hgbarrientos', 2, 'Efectivo USD');
INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (15, 320, 1, 1, 'hgbarrientos', 1, 'Ventas al credito');
INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (16, 320, 1, 1, 'hgbarrientos', 2, 'CxC empleado');
INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (17, 320, 1, 1, 'hgbarrientos', 3, 'Ventas prepago');
INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (18, 320, 1, 2, 'hgbarrientos', 2, 'Efectivo USD');
	INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (20, 340, 1, 1, 'hgbarrientos', 1, 'Ventas al credito');
	INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (21, 340, 1, 1, 'hgbarrientos', 2, 'CxC empleado');
	INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (22, 340, 1, 1, 'hgbarrientos', 3, 'Ventas prepago');
	INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (23, 340, 1, 2, 'hgbarrientos', 2, 'Efectivo USD');
INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (25, 558, 1, 1, 'hgbarrientos', 1, 'Ventas al credito');
INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (26, 558, 1, 1, 'hgbarrientos', 2, 'CxC empleado');
INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (27, 558, 1, 1, 'hgbarrientos', 3, 'Ventas prepago');
INSERT INTO mediopago (mediopago_id, pais_id, tipoprod_id, tipo, creado_por, orden, nombre) VALUES (28, 558, 1, 2, 'hgbarrientos', 2, 'Efectivo USD');

INSERT INTO mediopago (mediopago_id, nombre, tipo, creado_por, pais_id, orden, tipoprod_id, partidacont_por, partidacont) VALUES (mediopago_seq.NEXTVAL, 'Ventas Tarjeta Credomatic', 1, 'hgbarrientos', 188, 2, 1, 0.85, 1);
INSERT INTO mediopago (mediopago_id, nombre, tipo, creado_por, pais_id, orden, tipoprod_id, partidacont_por, partidacont) VALUES (mediopago_seq.NEXTVAL, 'Ventas T/C Banco Nacional', 1, 'hgbarrientos', 188, 3, 1, 0.6, 1);
INSERT INTO mediopago (mediopago_id, nombre, tipo, creado_por, pais_id, orden, tipoprod_id, partidacont_por, partidacont) VALUES (mediopago_seq.NEXTVAL, 'Ventas T/C BCR', 1, 'hgbarrientos', 188, 4, 1, 0.5, 1);
INSERT INTO mediopago (mediopago_id, nombre, tipo, creado_por, pais_id, orden, tipoprod_id, partidacont_por, partidacont) VALUES (mediopago_seq.NEXTVAL, 'Ventas T/C Fleet Magic SB', 1, 'hgbarrientos', 188, 5, 1, 0.85, 1);
INSERT INTO mediopago (mediopago_id, nombre, tipo, creado_por, pais_id, orden, tipoprod_id, partidacont_por, partidacont) VALUES (mediopago_seq.NEXTVAL, 'Ventas T/C FM - Davivienda', 1, 'hgbarrientos', 188, 6, 1, 0.85, 1);
INSERT INTO mediopago (mediopago_id, nombre, tipo, creado_por, pais_id, orden, tipoprod_id, partidacont_por, partidacont) VALUES (mediopago_seq.NEXTVAL, 'Ventas T/C Versatec', 1, 'hgbarrientos', 188, 7, 1, 1, 1);
INSERT INTO mediopago (mediopago_id, nombre, tipo, creado_por, pais_id, orden, tipoprod_id, partidacont_por, partidacont) VALUES (mediopago_seq.NEXTVAL, 'Ventas T/Flota BCR', 1, 'hgbarrientos', 188, 8, 1, 0.85, 1);
INSERT INTO mediopago (mediopago_id, nombre, tipo, creado_por, pais_id, orden, tipoprod_id, partidacont_por, partidacont) VALUES (mediopago_seq.NEXTVAL, 'Ventas T/Flota BAC', 1, 'hgbarrientos', 188, 9, 1, 0.85, 1);
INSERT INTO mediopago (mediopago_id, nombre, tipo, creado_por, pais_id, orden, tipoprod_id, partidacont_por, partidacont) VALUES (mediopago_seq.NEXTVAL, 'Ventas T/Uno Plus', 1, 'hgbarrientos', 188, 10, 1, 0, 0);
INSERT INTO mediopago (mediopago_id, nombre, tipo, creado_por, pais_id, orden, tipoprod_id, partidacont_por, partidacont) VALUES (mediopago_seq.NEXTVAL, 'Cupón', 1, 'hgbarrientos', 188, 11, 1, 0, 0);
INSERT INTO mediopago (mediopago_id, nombre, tipo, creado_por, pais_id, orden, tipoprod_id) VALUES (mediopago_seq.NEXTVAL, 'Efectivo colones', 2, 'hgbarrientos', 188, 1, 1);
COMMIT;

CREATE TABLE cliente (
    cliente_id NUMBER(5,0) NOT NULL, 
    codigo VARCHAR2(6) NOT NULL, 
    nombre VARCHAR2(75) NOT NULL, 
	tipo VARCHAR2(1) NOT NULL, 
    estacion_id NUMBER(4,0) NOT NULL, 
	codigo_envoy VARCHAR2(10) NOT NULL,
	cedula_juridica VARCHAR2(10),	
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    PRIMARY KEY (CLIENTE_ID),
    CONSTRAINT CLIENTE_FK01 FOREIGN KEY (ESTACION_ID) REFERENCES ESTACION (ESTACION_ID)
);
COMMENT ON COLUMN cliente.tipo IS 'Esta columna puede tener dos valores C o P (Credito o Prepago).';
CREATE SEQUENCE cliente_seq MINVALUE 50 MAXVALUE 99999 INCREMENT BY 1 START WITH 50 CACHE 20 ORDER  NOCYCLE;

/*CREATE SEQUENCE cliente_venta_seq MINVALUE 800 MAXVALUE 999999999 INCREMENT BY 1 START WITH 800 CACHE 10 ORDER  NOCYCLE;
CREATE TABLE cliente_venta (
    clienteventa_id NUMBER(9) NOT NULL PRIMARY KEY,
    cliente_id NUMBER(5) NOT NULL,
    arqueocaja_id NUMBER(9) NOT NULL,
    venta NUMBER(8,2) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL
);
ALTER TABLE cliente_venta ADD CONSTRAINT cliente_venta_fk01 FOREIGN KEY (cliente_id) REFERENCES cliente (cliente_id);
ALTER TABLE cliente_venta ADD CONSTRAINT cliente_venta_fk02 FOREIGN KEY (arqueocaja_id) REFERENCES arqueocaja (arqueocaja_id);*/



DROP SEQUENCE producto_seq;
CREATE SEQUENCE producto_seq MINVALUE 50 MAXVALUE 9999 INCREMENT BY 1 START WITH 50 CACHE 20 ORDER  NOCYCLE;
ALTER TABLE producto MODIFY producto_id NUMBER(4);
ALTER TABLE producto MODIFY nombre VARCHAR2(75);
ALTER TABLE producto ADD presentacion VARCHAR2(25);
ALTER TABLE producto ADD codigo_barras VARCHAR2(25);
ALTER TABLE producto MODIFY orden_pos NUMBER(3);
ALTER TABLE estacion_producto MODIFY producto_id NUMBER(4);

CREATE SEQUENCE lubricanteprecio_seq MINVALUE 200 MAXVALUE 9999999 INCREMENT BY 1 START WITH 200 CACHE 10 ORDER NOCYCLE;
CREATE TABLE lubricanteprecio (
    lubricanteprecio NUMBER(7) NOT NULL PRIMARY KEY,
    pais_id NUMBER(3) NOT NULL,
    estacion_id NUMBER(4) NOT NULL,
    producto_id NUMBER(4) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    precio NUMBER(9,3) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE
);
ALTER TABLE lubricanteprecio ADD CONSTRAINT lubricanteprecio_fk01 FOREIGN KEY (pais_id) REFERENCES pais (pais_id);
ALTER TABLE lubricanteprecio ADD CONSTRAINT lubricanteprecio_fk02 FOREIGN KEY (producto_id) REFERENCES producto (producto_id);
ALTER TABLE lubricanteprecio ADD CONSTRAINT lubricanteprecio_fk03 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id);

CREATE SEQUENCE arqueocaja_det_cxcprep_seq MINVALUE 50 MAXVALUE 999999999 INCREMENT BY 1 START WITH 50 CACHE 10 ORDER  NOCYCLE;
CREATE TABLE arqueocaja_det_cxcprep (
    idpk NUMBER(9) NOT NULL PRIMARY KEY,
    arqueocaja_id NUMBER(9) NOT NULL,
    cliente_id NUMBER(5) NOT NULL,
    monto NUMBER(9,2) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL
);
ALTER TABLE arqueocaja_det_cxcprep ADD CONSTRAINT arqueocaja_det_cxcprep_fk01 FOREIGN KEY (arqueocaja_id) REFERENCES arqueocaja (arqueocaja_id);
ALTER TABLE arqueocaja_det_cxcprep ADD CONSTRAINT arqueocaja_det_cxcprep_fk02 FOREIGN KEY (cliente_id) REFERENCES cliente (cliente_id);
CREATE SEQUENCE arqueocaja_det_lub_seq MINVALUE 75 MAXVALUE 999999999 INCREMENT BY 1 START WITH 75 CACHE 10 ORDER  NOCYCLE;
CREATE TABLE arqueocaja_det_lub (
    idpk NUMBER(9) NOT NULL PRIMARY KEY,
    arqueocaja_id NUMBER(9) NOT NULL,
    producto_id NUMBER(4) NOT NULL,
    cantidad NUMBER(6) NOT NULL,
    precio NUMBER(9,3) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL
);
ALTER TABLE arqueocaja_det_lub ADD CONSTRAINT arqueocaja_det_lub_fk01 FOREIGN KEY (arqueocaja_id) REFERENCES arqueocaja (arqueocaja_id);
ALTER TABLE arqueocaja_det_lub ADD CONSTRAINT arqueocaja_det_lub_fk02 FOREIGN KEY (producto_id) REFERENCES producto (producto_id);

DELETE FROM estacion_producto;
DELETE FROM producto where producto_id < 50 and producto_id not in (1,2,3,4,5,27,28);
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (6, 3, 1, 'Tienda', NULL, NULL, NULL, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (7, 3, 2, 'Pista - Otros', NULL, NULL, NULL, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (8, 3, 3, 'Servicentro', NULL, NULL, NULL, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (9, 3, 4, 'Lubricantes', NULL, NULL, NULL, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (10, 3, 5, 'Lubricantes - UNO', NULL, NULL, NULL, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (11, 3, 6, 'Misceláneos', NULL, NULL, NULL, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (12, 3, 7, 'Llantas', NULL, NULL, NULL, 'hgbarrientos');

INSERT INTO estacion_producto (estacion_id, producto_id, creado_por, creado_el) VALUES (147, 6, 'hgbarrientos', SYSDATE);
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por, creado_el) VALUES (147, 7, 'hgbarrientos', SYSDATE);
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por, creado_el) VALUES (147, 8, 'hgbarrientos', SYSDATE);
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por, creado_el) VALUES (147, 9, 'hgbarrientos', SYSDATE);
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por, creado_el) VALUES (147, 10, 'hgbarrientos', SYSDATE);
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por, creado_el) VALUES (147, 11, 'hgbarrientos', SYSDATE);
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por, creado_el) VALUES (147, 12, 'hgbarrientos', SYSDATE);

INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 1, 'UNO Forza 15W-40     1 AQ', 'FOR15W40201', 'LITRO', '740105421023 1', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 4000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 2, 'UNO Forza 15W-40     1 AG', 'FOR15W40211', 'GALON', '740105421024 8', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 16000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 3, 'UNO Forza 25W-60     1 AQ', 'FOR25W60201', 'LITRO', '740105421029 3', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 4000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 4, 'UNO Forza 25W-60     1 AG', 'FOR25W60211', 'GALON', '740105421030 9', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 15000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 5, 'UNO Impulse 2T     1 AQ', 'IMP2T201', 'LITRO', '740105421044 6', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3800, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 6, 'UNO Impulse 2T     1 AG', 'IMP2T211', 'GALON', '740105421045 3', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 13000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 7, 'UNO Impulse TC-W3     1 AQ', 'IMPTCW3201', 'LITRO', '740105421048 4', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3800, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 8, 'UNO Impulse TC-W3     1 AG', 'IMPTCW3211', 'GALON', '740105421049 1', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 13000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 9, 'UNO Synchron 80W-90     1 AQ', 'SYN80W90201', 'LITRO', '740105421033 0', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3800, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 10, 'UNO Synchron 80W-90     1 AG', 'SYN80W90211', 'GALON', '740105421034 7', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 14500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 11, 'UNO Synchron 85W-140     1 AQ', 'SYN85W140201', 'LITRO', '740105421037 8', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3800, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 12, 'UNO Synchron 85W-140     1 AG', 'SYN85W140211', 'GALON', '740105421038 5', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 14500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 13, 'UNO Synchron ATF      1 AQ', 'SYNATF201', 'LITRO', '740105421041 5', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 4000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 14, 'UNO Synchron ATF      1 AG', 'SYNATF211', 'GALON', '740105421100 9', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 15000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 15, 'UNO Ultra 10W-30     1 AQ', 'ULT10W30201', 'LITRO', '740105421005 7', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 4200, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 16, 'UNO Ultra 10W-30     1 AG', 'ULT10W30211', 'GALON', '740105421006 4', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 15000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 17, 'UNO Ultra 20W-50     1 AQ', 'ULT20W50201', 'LITRO', '740105421001 9', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 4000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 18, 'UNO Ultra 20W-50     1 AG', 'ULT20W50211', 'GALON', '740105421002 6', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 15000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 19, 'UNO Ultra 40     1 AQ', 'ULT40201', 'LITRO', '740105421015 6', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 4000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 20, 'UNO Ultra 40     1 AG', 'ULT40211', 'GALON', '740105421016 3', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 14500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 21, 'UNO Ultra 50     1 AQ', 'ULT50201', 'LITRO', '740105421019 4', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 4000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 22, 'UNO Ultra 50     1 AG', 'ULT50211', 'GALON', '740105421020 0', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 14500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 23, 'UNO Axxis MP2 Tub 14 Oz', 'AXXMP2417', 'TUBO', '740105421101 6', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 2300, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 24, 'UNO Axxis EP2 Tub 14 Oz', 'AXXEP2417', 'TUBO', '740105421102 3', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 2500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 25, 'TP Brake Fluid 12 Oz.', 'TPBFDOT3433', '12 OZ', '740105421090  3 ', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 1800, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 26, 'TP Diesel Treatment 12 Oz', 'TPDT433', '12 OZ', '740105421091  0', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 1750, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 27, 'TP Fuel Injector Treat 12 Oz', 'TPFIT433', '12 OZ', '740105421092  7', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 2000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 28, 'TP Gasoline Treatment 12 Oz', 'TPGT433', '12 OZ', '740105421093  4', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 1750, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 29, 'TP Octane Booster 12 Oz', 'TPOB433', '12 OZ', '740105421098  9 ', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 2000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 30, 'TP Power Steering Fluid 12 Oz', 'TPPSF433', '12 OZ', '740105421099  6', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 2000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 31, 'UNO Impulse 4T 20W-50 1 AQ', 'IMP4T20W50201', 'LITRO', '740105421104 7', 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 4000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 32, 'UNO Ultra 40 P5X1 AG', 'ULT40221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 39550, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 33, 'UNO AW Oil 46 P5X1 AG', 'AWOIL46221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 33900, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 34, 'UNO AW Oil 68 P5X1 AG', 'AWOIL68221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 33900, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 35, 'UNO AW 32 SUPER 3000 P5X1 AG', 'AWOIL32221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 33900, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 36, 'UNO HTF P5X1 AG', 'HTF221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 33900, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 37, 'UNO Forza 15W-40 P5X1 AG', 'FOR15W40221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 41700, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 38, 'UNO Forza 25W-60 P5X1 AG', 'FOR25W60221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 42950, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 39, 'UNO Synchron ATF  P5X1 AG', 'SYNATF221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 44100, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 40, 'UNO Synchron 80W-90 P5X1 AG', 'SYN80W90221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 46350, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 41, 'UNO Synchron 85W-140 P5X1 AG', 'SYN85W140221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 46350, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 42, 'UNO Ultra 10W P5X1 AG', 'ULT10W221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 39550, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 43, 'UNO Ultra 50 P5X1 AG', 'ULT50221', 'CUBETA', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 39550, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 44, 'UNO AW 32 SUPER 3000 D55X1 AG', 'AWOIL32241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 343800, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 45, 'UNO AW Oil 46 D55X1 AG', 'AWOIL46241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 343800, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 46, 'UNO AW Oil 68 D55X1 AG', 'AWOIL68241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 343800, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 47, 'UNO Forza 15W-40 D55X1 AG', 'FOR15W40241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 478350, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 48, 'UNO Forza 25W-60 D55X1 AG', 'FOR25W60241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 491500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 49, 'UNO HTF D55X1 AG', 'HTF241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 352950, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 50, 'UNO Synchron 80W-90 D55X1 AG', 'SYN80W90241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 489700, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 51, 'UNO Synchron 85W-140 D55X1 AG', 'SYN85W140241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 489700, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 52, 'UNO Synchron ATF D55X1 AG', 'SYNATF241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 489700, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 53, 'UNO Ultra 10W D55X1 AG', 'ULT10W241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 473900, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 54, 'UNO Ultra 10W-30 D55X1 AG', 'ULT10W30241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 473900, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 55, 'UNO Ultra 20W-50 D55X1 AG', 'ULT20W50241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 473900, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 56, 'UNO Ultra 40 D55X1 AG', 'ULT40241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 473900, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 57, 'UNO Ultra 50 D55X1 AG', 'ULT50241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 473900, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 58, 'UNO AW Oil 68 1AG GRANEL', 'AWOIL68000', 'AG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 9500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 59, 'UNO Forza 15W-40 1AG GRANEL', 'FOR15W40000', 'AG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 13500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 60, 'UNO Forza 25W-60 1AG GRANEL', 'FOR25W60000', 'AG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 12000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 61, 'UNO HTF 1AG GRANEL', 'HTF000', 'AG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 9800, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 62, 'UNO Synchron 80W-90 1AG GRANEL', 'SYN80W90000', 'AG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 12300, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 63, 'UNO Synchron 85W-140 1AG GRANEL', 'SYN85W140000', 'AG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 12300, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 64, 'UNO Ultra 10W-30 1AG GRANEL', 'ULT10W30000', 'AG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 12700, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 65, 'UNO Ultra 20W-50 1AG GRANEL', 'ULT20W50000', 'AG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 12700, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 66, 'UNO Ultra 40 1AG GRANEL', 'ULT40000', 'AG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 12300, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 67, 'UNO AXXIS EP2 KE120 LB', 'AXXEP2407', 'KE', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 239700, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 68, 'UNO AXXIS MP2 KE120 LB', 'AXXMP2407', 'KE', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 239700, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 69, 'UNO AXXIS EP2 P35 LB', 'AXXEP2406', 'PL', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 70500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 70, 'UNO AXXIS MP2 P35 LB', 'AXXMP2406', 'PL', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 61250, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 71, 'UNO AW Oil 68 1QT GRANEL', 'AWOIL68111', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 2500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 72, 'UNO Forza 15W-40 1QT GRANEL', 'FOR15W40111', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 73, 'UNO Impulse TC-W3 1QT GRANEL', 'IMPTCW3111', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 74, 'UNO Synchron 80W-90 1QT GRANEL', 'SYN80W90111', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 75, 'UNO Synchron 85W-140 1QT GRANEL', 'SYN85W140111', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 76, 'UNO Ultra 20W-50 1QT GRANEL', 'ULT20W50111', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 77, 'UNO Ultra 40 1QT GRANEL', 'ULT40111', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 78, 'UNO Ultra 10W 1QT GRANEL', 'ULT10W111', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 79, 'TP COOLANT 50/50 1 AQ', 'TPCOL50/50201', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 2000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 80, 'TP COOLANT 50/50 1 AG', 'TPCOL50/50211', 'AG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 8000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 81, 'UNO ULTRA FULL SYNTHETIC 1QT', 'ULTFS5W30201', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 6000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 82, 'UNO Forza 25W-60 1QT', 'FOR25W60111', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 83, 'UNO Forza 50 1 AQ', 'FOR50201', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 4000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 84, 'UNO Forza 50 1 AG', 'FOR50211', 'AG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 14500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 85, 'UNO Forza 50 P5X1AG', 'FOR50221', 'PL', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 39500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 86, 'UNO Forza 50 D55X1 AG', 'FOR50241', 'DR', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 473950, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 87, 'UNO Forza 50 1QT GRANEL', 'FOR50111', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 3000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 88, 'UNO EP 220 P5X1 AG', 'EP220221', 'PL', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 71585.5, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 89, 'UNO Forza 20W-50 P5X1 AG', 'FOR20W50221', 'PL', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 41700, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 90, 'UNO Impulse 4T 10W-40', 'IMP4T10W40201', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 4200, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 91, 'FORZA EURO SAE 5W-40 C12X1 QT', 'FOREU5W40201', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 6000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 92, 'UNO AXXIS EP2 0.5 LB', 'AXXEP2106', 'BT', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 1000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 93, 'UNO Impulse 2T Pinta 16ONZ', 'IMP2T438', 'LB', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 2200, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 94, 'UNO Impulse TC-W3 Pinta 16ONZ', 'IMPTCW3438', 'LB', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 2200, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 95, 'TP Brake Fluid DOT 4 12 OZ', 'TPBFDOT4433', 'BT', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 2100, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 96, 'UNO Forza PRO 15W-40 1 AQ', 'FORPRO15W40201', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 4400, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 97, 'SYNCHRON ATF FS M/V 1QT', 'SYNATFMV201', 'AQ', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 5500, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 98, 'UNO Forza PRO 15W-40 5/4 U.S.', 'FORPRO15W40500', 'IG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 21000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 99, 'UNO Ultra 20W-50 5/4 U.S.', 'ULT20W50500', 'IG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 18000, 'hgbarrientos');
INSERT INTO producto (producto_id, tipo_id, orden_pos, nombre, codigo, presentacion, codigo_barras, creado_por) VALUES (producto_seq.NEXTVAL, 2, 100, 'UNO Ultra SYN BLD 10W-30 5/4 U.S.', 'ULT10W30500', 'IG', null, 'hgbarrientos');	INSERT INTO lubricanteprecio (lubricanteprecio,pais_id,producto_id,fecha_inicio,fecha_fin,precio,creado_por) VALUES (lubricanteprecio_seq.NEXTVAL, 188, producto_seq.CURRVAL, SYSDATE, SYSDATE+30, 18000, 'hgbarrientos');

COMMIT;



CREATE TABLE empleado (
    empleado_id NUMBER(2) NOT NULL PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL
);
CREATE TABLE bomba_empleado_estacion (
    estacion_id NUMBER(4) NOT NULL,
    empleado_id NUMBER(2) NOT NULL,
    bomba_id NUMBER(2) NOT NULL,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    PRIMARY KEY (estacion_id, empleado_id, bomba_id)
);
ALTER TABLE bomba_empleado_estacion ADD CONSTRAINT bomba_empleado_estacion_fk1 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id);
ALTER TABLE bomba_empleado_estacion ADD CONSTRAINT bomba_empleado_estacion_fk2 FOREIGN KEY (empleado_id) REFERENCES empleado (empleado_id);
ALTER TABLE bomba_empleado_estacion ADD CONSTRAINT bomba_empleado_estacion_fk3 FOREIGN KEY (bomba_id) REFERENCES bomba (bomba_id);
ALTER TABLE lectura ADD empleado_id NUMBER(2) NOT NULL;
ALTER TABLE lectura ADD CONSTRAINT lectura_fk03 FOREIGN KEY (empleado_id) REFERENCES empleado (empleado_id);
ALTER TABLE arqueocaja ADD empleado_id NUMBER(2) NOT NULL;
ALTER TABLE arqueocaja ADD CONSTRAINT arqueocaja_fk05 FOREIGN KEY (empleado_id) REFERENCES empleado (empleado_id);
ALTER TABLE arqueocaja ADD nombre_pistero VARCHAR2(50) NOT NULL;
ALTER TABLE arqueocaja ADD nombre_jefe VARCHAR2(50) NOT NULL;


INSERT INTO empleado (empleado_id, nombre, creado_por) VALUES (1, 'Pistero 1', 'hgbarrientos');
INSERT INTO empleado (empleado_id, nombre, creado_por) VALUES (2, 'Pistero 2', 'hgbarrientos');
INSERT INTO empleado (empleado_id, nombre, creado_por) VALUES (3, 'Pistero 3', 'hgbarrientos');
INSERT INTO empleado (empleado_id, nombre, creado_por) VALUES (4, 'Cajero 1', 'hgbarrientos');
INSERT INTO empleado (empleado_id, nombre, creado_por) VALUES (5, 'Cajero 2', 'hgbarrientos');
INSERT INTO empleado (empleado_id, nombre, creado_por) VALUES (6, 'Cajero 3', 'hgbarrientos');
INSERT INTO acceso (acceso_id, titulo, padre, orden, recurso_interno, estado, creado_por, descripcion)
VALUES (18, 'Clientes credito', 2, 6, 'MNT_CUST_CREDITO', 'A', 'hgbarrientos', 'Pantalla de mantenimiento para los clientes credito de una estacion');
INSERT INTO acceso (acceso_id, titulo, padre, orden, recurso_interno, estado, creado_por, descripcion)
VALUES (19, 'Clientes prepago', 2, 7, 'MNT_CUST_PREPAGO', 'A', 'hgbarrientos', 'Pantalla de mantenimiento para los clientes prepago de una estacion');
INSERT INTO acceso (acceso_id, titulo, padre, orden, recurso_interno, estado, creado_por, descripcion)
VALUES (20, 'Precio lubricantes', 2, 8, 'MNT_PRICE_LUBS', 'A', 'hgbarrientos', 'Pantalla de mantenimiento para los precios de lubricantes');
/*INSERT INTO bomba_empleado_estacion (creado_por, estacion_id, empleado_id, bomba_id) VALUES ('hgbarrientos', 147, 1, 1); INSERT INTO bomba_empleado_estacion (creado_por, estacion_id, empleado_id, bomba_id) VALUES ('hgbarrientos', 147, 1, 2);
INSERT INTO bomba_empleado_estacion (creado_por, estacion_id, empleado_id, bomba_id) VALUES ('hgbarrientos', 147, 2, 3); INSERT INTO bomba_empleado_estacion (creado_por, estacion_id, empleado_id, bomba_id) VALUES ('hgbarrientos', 147, 2, 4);
*/COMMIT;    




ALTER TABLE mediopago ADD is_tcredito NUMBER(1) DEFAULT 0 NOT NULL;
DROP TABLE bomba_empleado_estacion;
CREATE TABLE turno_empleado_bomba (
    turno_id NUMBER(9) NOT NULL,
    empleado_id NUMBER(2) NOT NULL,
    bomba_id NUMBER(2) NOT NULL,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    PRIMARY KEY (turno_id, empleado_id, bomba_id)
);
ALTER TABLE turno_empleado_bomba ADD CONSTRAINT turno_empleado_bomba_fk1 FOREIGN KEY (turno_id) REFERENCES turno (turno_id);
ALTER TABLE turno_empleado_bomba ADD CONSTRAINT turno_empleado_bomba_fk2 FOREIGN KEY (empleado_id) REFERENCES empleado (empleado_id);
ALTER TABLE turno_empleado_bomba ADD CONSTRAINT turno_empleado_bomba_fk3 FOREIGN KEY (bomba_id) REFERENCES bomba (bomba_id);



INSERT INTO acceso (acceso_id, titulo, padre, orden, recurso_interno, estado, creado_por, descripcion)
VALUES (21, 'Horarios', 2, 9, 'MNT_SCHEDULE', 'A', 'hgbarrientos', 'Pantalla de mantenimiento para horarios');
INSERT INTO acceso (acceso_id, titulo, padre, orden, recurso_interno, estado, creado_por, descripcion)
VALUES (22, 'Modo servicio', 2, 10, 'MNT_MODOSERV', 'A', 'hgbarrientos', 'Pantalla de mantenimiento para horarios');
ALTER TABLE estacion_conf_head DROP COLUMN estacion_id;
ALTER TABLE estacion_conf_head ADD estacion_id NUMBER(4,0);


CREATE SEQUENCE horario_seq MINVALUE 75 MAXVALUE 999999 INCREMENT BY 1 START WITH 75 CACHE 10 ORDER  NOCYCLE;
CREATE TABLE horario (
    horario_id NUMBER(6) NOT NULL,
    nombre VARCHAR2(75) NOT NULL,
    descripcion VARCHAR2(150),
    hora_inicio VARCHAR2(5) NOT NULL,
    hora_fin VARCHAR2(5) NOT NULL,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE
);
ALTER TABLE horario ADD CONSTRAINT horario_pk PRIMARY KEY (horario_id);

CREATE TABLE estacion_horario (
    horario_id NUMBER(6) NOT NULL,
    estacion_id NUMBER(4) NOT NULL,	
	estacionconfhead_id NUMBER(4) NOT NULL,
	paisestacion_id NUMBER(3) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL
);
ALTER TABLE estacion_horario ADD CONSTRAINT estacion_horario_pk PRIMARY KEY (horario_id, estacion_id, estacionconfhead_id);
ALTER TABLE estacion_horario ADD CONSTRAINT estacion_horario_fk01 FOREIGN KEY (horario_id) REFERENCES horario (horario_id);
ALTER TABLE estacion_horario ADD CONSTRAINT estacion_horario_fk02 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id);
--ALTER TABLE estacion_horario ADD estacionconfhead_id NUMBER(4);
ALTER TABLE estacion_horario ADD CONSTRAINT estacion_horario_fk03 FOREIGN KEY (estacionconfhead_id) REFERENCES estacion_conf_head (estacionconfhead_id);
ALTER TABLE estacion_horario ADD CONSTRAINT estacion_horario_fk04 FOREIGN KEY (paisestacion_id) REFERENCES pais (pais_id);
COMMENT ON COLUMN estacion_horario.paisestacion_id IS 'Esta columna se usa solo para tener facilidad en al editar horarios y siempre se debe mandar al crear o modificar horarios.';

DROP TABLE tasacambio;
CREATE SEQUENCE tasacambio_seq MINVALUE 100 MAXVALUE 999999999 INCREMENT BY 1 START WITH 100 CACHE 5 ORDER  NOCYCLE;
CREATE TABLE tasacambio (
    tasacambio_id NUMBER(9) NOT NULL,
	pais_id NUMBER(3) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    tasa NUMBER(8,3) DEFAULT 0 NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE
);
ALTER TABLE tasacambio ADD CONSTRAINT tasacambio_pk PRIMARY KEY (tasacambio_id);
ALTER TABLE tasacambio ADD CONSTRAINT tasacambio_fk01 FOREIGN KEY (pais_id) REFERENCES pais (pais_id);

ALTER TABLE lubricanteprecio DROP CONSTRAINT LUBRICANTEPRECIO_FK03;
ALTER TABLE lubricanteprecio DROP COLUMN estacion_id;
ALTER TABLE estacion ADD codigo_envoy VARCHAR2(7) DEFAULT '0' NOT NULL;
ALTER TABLE estacion ADD id_marca NUMBER(4) NOT NULL;
ALTER TABLE bomba_estacion ADD corr_pista NUMBER(3) DEFAULT '0' NOT NULL;

CREATE TABLE marca (
    id_marca NUMBER(4) NOT NULL,
    nombre VARCHAR2(75) NOT NULL,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL
);
ALTER TABLE marca ADD CONSTRAINT marca_pk PRIMARY KEY (id_marca);
INSERT INTO marca (id_marca, nombre, creado_por) VALUES (100, 'Uno', 'hgbarrientos');
INSERT INTO marca (id_marca, nombre, creado_por) VALUES (700, 'Shell', 'hgbarrientos');
INSERT INTO marca (id_marca, nombre, creado_por) VALUES (2100, 'Texaco', 'hgbarrientos');
ALTER TABLE producto ADD id_marca NUMBER(4);
ALTER TABLE producto ADD CONSTRAINT producto_fk02 FOREIGN KEY (id_marca) REFERENCES marca (id_marca);

INSERT INTO acceso (acceso_id, titulo, padre, orden, recurso_interno, estado, creado_por, descripcion)
VALUES (23, 'Roles', 2, 11, 'MNT_ROL', 'A', 'hgbarrientos', 'Pantalla de mantenimiento para los roles de la aplicacion.');
INSERT INTO acceso (acceso_id, titulo, padre, orden, recurso_interno, estado, creado_por, descripcion)
VALUES (24, 'Productos', 2, 12, 'MNT_PRODUCTO', 'A', 'hgbarrientos', 'Pantalla de mantenimiento para productos.');
CREATE SEQUENCE rol_seq MINVALUE 10 MAXVALUE 99 INCREMENT BY 1 START WITH 10 CACHE 5 ORDER  NOCYCLE;
ALTER TABLE usuario ADD correo VARCHAR2(100);
ALTER TABLE producto ADD codigo_envoy VARCHAR2(10);


update acceso set estado='I' where acceso_id=19; 
update acceso set titulo='Clientes' where acceso_id=18; 
commit;
delete from estacion_conf;
COMMIT;
ALTER TABLE estacion_conf ADD estacion_id NUMBER(4) NOT NULL;
DELETE FROM estacion_conf_head WHERE estacion_id IS NOT NULL;
COMMIT;
ALTER TABLE estacion_conf DROP CONSTRAINT ESTACION_CONF_PK;
ALTER TABLE estacion_conf ADD CONSTRAINT ESTACION_CONF_PK1 PRIMARY KEY (ESTACIONCONFHEAD_ID, BOMBA_ID, TIPODESPACHO_ID, estacion_id);
CREATE TABLE pais_producto  (
    pais_id NUMBER(3) NOT NULL,
    producto_id VARCHAR2(30) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    CONSTRAINT pais_producto_id PRIMARY KEY (pais_id, producto_id)
);



/*
ALTER TABLE estacion_conf DROP CONSTRAINT ESTACION_CONF_PK;
ALTER TABLE estacion_conf DROP CONSTRAINT ESTACION_CONF_PK1;
drop index ESTACION_CONF_PK;
ALTER TABLE estacion_conf ADD CONSTRAINT ESTACION_CONF_PK PRIMARY KEY (ESTACIONCONFHEAD_ID, BOMBA_ID, TIPODESPACHO_ID, estacion_id);
CREATE UNIQUE INDEX ESTACION_CONF_PK ON ESTACION_CONF (ESTACIONCONFHEAD_ID, BOMBA_ID, TIPODESPACHO_ID, estacion_id) ;
*/


UPDATE ACCESO SET TITULO = 'Tipo de Cambio' WHERE ACCESO_ID = 13;
DELETE FROM ACCESO_ROL WHERE ACCESO_ID = 17;
DELETE FROM ACCESO WHERE ACCESO_ID = 17;
ALTER TABLE PRODUCTO ADD  sku VARCHAR2(13);
COMMIT;

INSERT INTO acceso (acceso_id, titulo, padre, orden, recurso_interno, descripcion, creado_por) VALUES (25, 'Configuracion', null, 4, null, 'Opción raíz para el módulo de configuraciones', 'hgbarrientos');
UPDATE acceso SET orden = 4 WHERE acceso_id = 3;
UPDATE acceso SET padre = 25 WHERE acceso_id IN (12, 13, 23, 24);
commit;

ALTER TABLE inventario ADD inv_fisico NUMBER(7,2);
ALTER TABLE inventario ADD lectura_veeder NUMBER(7,2);
ALTER TABLE inventario ADD compartimiento VARCHAR2(10);
ALTER TABLE inventario ADD vol_facturado NUMBER(7,2);
ALTER TABLE inventario ADD pulgadas VARCHAR2(15);
ALTER TABLE inventario ADD galones_cisterna NUMBER(7,2);

ALTER TABLE inventario_coco ADD inv_fisico NUMBER(7,2);
ALTER TABLE inventario_coco ADD lectura_veeder NUMBER(7,2);
ALTER TABLE inventario_coco ADD compartimiento VARCHAR2(10);
ALTER TABLE inventario_coco ADD vol_facturado NUMBER(7,2);
ALTER TABLE inventario_coco ADD pulgadas VARCHAR2(15);
ALTER TABLE inventario_coco ADD galones_cisterna NUMBER(7,2);

CREATE SEQUENCE inv_recepcion_seq MINVALUE 175 MAXVALUE 9999999 INCREMENT BY 1 START WITH 175 CACHE 5 ORDER  NOCYCLE;
CREATE TABLE inv_recepcion (
    invrecepcion_id NUMBER(7) NOT NULL,
    fecha DATE,
    pais_id NUMBER(3) NOT NULL,
    estacion_id NUMBER(4) NOT NULL,	
    piloto VARCHAR2(30),
    unidad VARCHAR2(15),
    factura VARCHAR2(30),
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL
);
ALTER TABLE inv_recepcion ADD CONSTRAINT inv_recepcion_pk PRIMARY KEY (invrecepcion_id);
INSERT INTO acceso (acceso_id, titulo, padre, orden, recurso_interno, estado, creado_por, descripcion)
VALUES (26, 'Reporte WSM', 3, 4, 'RPT_WSM', 'A', 'hgbarrientos', 'Pantalla para reporte WSM.');

ALTER TABLE turno ADD horario_id NUMBER(6);
ALTER TABLE turno ADD CONSTRAINT horario_fk03 FOREIGN KEY (horario_id) REFERENCES horario (horario_id);
INSERT INTO producto VALUES (13, 'Diferencia cuadre', null, 3, 4, 'A', 'hgbarrientos', sysdate, null, null, null, null, null, null, null, null);
INSERT INTO estacion_producto VALUES (323, 13, 'hgbarrientos', sysdate);
update acceso set titulo='Configuraciones generales' where acceso_id=25;
update acceso set titulo='Mantenimiento de sitios' where acceso_id=2;
update acceso set titulo='Operación diaria' where acceso_id=1;
ALTER TABLE arqueocaja_detalle modify MEDIOPAGO_ID  NUMBER(3);
commit;


--select LOWER(column_name)||',' from all_tab_columns where owner='ECOCO' and table_name = 'PRODUCTO';





