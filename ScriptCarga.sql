
/*
--https://www.techonthenet.com/oracle/schemas/create_schema.php
CREATE USER ecoco IDENTIFIED BY ecoco;
GRANT ALL PRIVILEGES TO ecoco;
*/

/*
SELECT 'private'
|| CASE 
WHEN data_scale = 0 THEN ' Integer '
WHEN data_scale > 0 THEN ' Double '
WHEN data_type = 'DATE' THEN ' Date '
WHEN data_type = 'VARCHAR2' THEN ' String '
END ||
LOWER(column_name)||';' 
FROM all_tab_columns WHERE table_name = 'LECTURAFINAL' ORDER BY column_id;
*/

/*

DROP SEQUENCE efectivo_seq;
DROP SEQUENCE producto_seq;
DROP SEQUENCE estacion_seq;
DROP SEQUENCE usuario_seq;
DROP SEQUENCE turno_seq;
DROP SEQUENCE lectura_seq;
DROP SEQUENCE precio_seq;
DROP SEQUENCE arqueocaja_seq;
DROP SEQUENCE mediopago_seq;
DROP SEQUENCE estacion_conf_head_seq;


DROP TABLE efectivo;
DROP TABLE precio;
DROP TABLE lectura_detalle;
DROP TABLE lectura;
DROP TABLE lecturafinal;
DROP TABLE bomba_estacion;
DROP TABLE estacion_producto;
DROP TABLE mediopago_pais;
DROP TABLE estacion_pais;
DROP TABLE arqueocaja_bomba;
DROP TABLE arqueocaja_detalle;
DROP TABLE arqueocaja_producto;
DROP TABLE factelectronica_pos;
DROP TABLE arqueocaja;
DROP TABLE turno;
DROP TABLE estacion_conf;
DROP TABLE estacion_conf_head;
DROP TABLE estacion_usuario;
DROP TABLE rol_usuario;
DROP TABLE acceso_rol;
DROP TABLE tasacambio;

DROP TABLE bomba;
DROP TABLE producto;
DROP TABLE tipoproducto;
DROP TABLE tipodespacho;
DROP TABLE usuario;
DROP TABLE rol;
DROP TABLE acceso;
DROP TABLE estado;
DROP TABLE mediopago;
DROP TABLE unidadmedida;
DROP TABLE pais;
DROP TABLE estacion;
DROP TABLE dia;

select * from all_objects where owner = 'ECOCO';
select * from all_objects where owner = 'ECOCOS_PY';

*/

/********* CATALOGOS *********/

CREATE TABLE bomba (
    bomba_id NUMBER(2) PRIMARY KEY,
    nombre VARCHAR2(75) NOT NULL,
    isla NUMBER(2) NOT NULL,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE
);


CREATE TABLE tipoproducto (
    tipo_id NUMBER(1) PRIMARY KEY,
    nombre VARCHAR2(15) NOT NULL,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL
);


CREATE TABLE producto (
    producto_id NUMBER(2) PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL,
    codigo VARCHAR2(30),
    codigo_num NUMBER(8),
    tipo_id NUMBER(1) NOT NULL,
    orden_pos NUMBER(1) DEFAULT 0,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    CONSTRAINT producto_fk01 FOREIGN KEY (tipo_id) REFERENCES tipoproducto (tipo_id)
);
CREATE SEQUENCE producto_seq
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 99
    NOCYCLE
    ORDER;

CREATE TABLE pais (
    pais_id NUMBER(3) PRIMARY KEY,
    nombre VARCHAR2(30) NOT NULL,
    codigo VARCHAR2(5) NOT NULL,
    moneda_simbolo VARCHAR2(4) NOT NULL,
    vol_simbolo VARCHAR(4) NOT NULL,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE
);

CREATE TABLE pais_producto  (
    pais_id NUMBER(3) NOT NULL,
    producto_id VARCHAR2(30) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    CONSTRAINT pais_producto_id PRIMARY KEY (pais_id, producto_id)
);

CREATE TABLE estacion (
    estacion_id NUMBER(4) PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL, 
    codigo VARCHAR2(6) NOT NULL,
    bu VARCHAR2(6),
    deposito VARCHAR2(6),
    pais_id NUMBER(3) NOT NULL,
    datos_conexion VARCHAR2(50),
    clave_conexion VARCHAR2(50),
    fact_electronica VARCHAR2(1) DEFAULT 'N' NOT NULL,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE
);
CREATE SEQUENCE estacion_seq
    START WITH 100
    INCREMENT BY 1
    MAXVALUE 9999
    NOCYCLE
    ORDER;

CREATE TABLE tipodespacho (
    tipodespacho_id NUMBER(1) PRIMARY KEY,
    nombre VARCHAR2(20) NOT NULL,
    nombre_corto VARCHAR2(10),
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE
);


CREATE TABLE usuario (
    usuario_id NUMBER(4) PRIMARY KEY,
    username VARCHAR2(50) NOT NULL,
    clave VARCHAR2(50) NOT NULL,
    nombre VARCHAR2(50) NOT NULL,
    apellido VARCHAR2(50) NOT NULL,
    pais_id NUMBER(3),
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE
);
CREATE UNIQUE INDEX usuario_idx01 ON usuario (username);
CREATE UNIQUE INDEX usuario_idx01 ON usuario (clave);
ALTER TABLE usuario ADD FOREIGN KEY (pais_id) REFERENCES pais (pais_id);
CREATE SEQUENCE usuario_seq
    START WITH 172
    INCREMENT BY 1
    MAXVALUE 9999
    NOCYCLE
    ORDER;


CREATE TABLE rol (
    rol_id NUMBER(2) PRIMARY KEY,
    nombre VARCHAR2(30) NOT NULL,
    descripcion VARCHAR2(100) NOT NULL,
    rolpadre_id NUMBER(2),
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL,
    creado_por VARCHAR2(40) NOT NULL,
    creado_el DATE DEFAULT SYSDATE NOT NULL,
    modificado_por VARCHAR2(40),
    modificado_el DATE,
    CONSTRAINT rol_fk01 FOREIGN KEY (rolpadre_id) REFERENCES rol (rol_id)
);

CREATE TABLE acceso (
    acceso_id NUMBER(3) PRIMARY KEY,
    titulo VARCHAR2(50) NOT NULL,
    padre NUMBER(3),
    orden NUMBER(2) NOT NULL,
    recurso_interno VARCHAR2(50),
    descripcion VARCHAR2(100) NOT NULL,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    CONSTRAINT acceso_fk01 FOREIGN KEY (padre) REFERENCES acceso (acceso_id)
);

CREATE TABLE estado (
    estado_id NUMBER(2) PRIMARY KEY,
    nombre VARCHAR2(30) NOT NULL,
    descripcion VARCHAR2(100),
    creado_por VARCHAR2(40) NOT NULL,
    creado_el DATE DEFAULT SYSDATE NOT NULL,
    modificado_por VARCHAR2(40),
    modificado_el DATE
);

CREATE TABLE mediopago (
    mediopago_id NUMBER(2) PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL,
    tipo NUMBER(1) NOT NULL, 
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL,
    creado_por VARCHAR2(40) NOT NULL,
    creado_el DATE DEFAULT SYSDATE NOT NULL,
    modificado_por VARCHAR2(40),
    modificado_el DATE
);
CREATE SEQUENCE mediopago_seq
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 99
    NOCYCLE
    ORDER;

CREATE TABLE unidadmedida (
    unidadmedida_id NUMBER(2) PRIMARY KEY,
    nombre VARCHAR2(30) NOT NULL,
    simbolo VARCHAR(5) NOT NULL,
    pais_id NUMBER(3),
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    CONSTRAINT unidadmedida_fk01 FOREIGN KEY (pais_id) REFERENCES pais (pais_id)
);


CREATE TABLE tasacambio (
    anio NUMBER(4) NOT NULL,
    mes NUMBER(2) NOT NULL,
    pais_id NUMBER(3) NOT NULL,
    tasa NUMBER(8, 3) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    CONSTRAINT tasacambio_pk PRIMARY KEY (anio, mes, pais_id),
    CONSTRAINT tasacambio_fk01 FOREIGN KEY (pais_id) REFERENCES pais (pais_id)
);

/*CREATE TABLE moneda (
    moneda_id NUMBER(2) PRIMARY KEY,
    nombre VARCHAR2(30) NOT NULL,
    iso VARCHAR(4) NOT NULL,
    simbolo VARCHAR(2) NOT NULL,
    pais_id NUMBER(3),
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    CONSTRAINT moneda_fk01 FOREIGN KEY (pais_id) REFERENCES pais (pais_id)
);*/


/********* RELACIONALES *********/


CREATE TABLE bomba_estacion (
    bomba_id NUMBER(2) NOT NULL,
    estacion_id NUMBER(4) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT bomba_estacion_pk PRIMARY KEY (bomba_id, estacion_id),
    CONSTRAINT bomba_estacion_fk01 FOREIGN KEY (bomba_id) REFERENCES bomba (bomba_id),
    CONSTRAINT bomba_estacion_fk02 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id)
);

CREATE TABLE estacion_producto (
    estacion_id NUMBER(4) NOT NULL,
    producto_id NUMBER(2) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT estacion_producto_pk PRIMARY KEY (estacion_id, producto_id),
    CONSTRAINT estacion_producto_fk01 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id),
    CONSTRAINT estacion_producto_fk02 FOREIGN KEY (producto_id) REFERENCES producto (producto_id)
);

CREATE TABLE estacion_usuario (
    estacion_id NUMBER(4) NOT NULL,
    usuario_id NUMBER(4) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT estacion_usuario_pk PRIMARY KEY (estacion_id, usuario_id),
    CONSTRAINT estacion_usuario_fk01 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id),
    CONSTRAINT estacion_usuario_fk02 FOREIGN KEY (usuario_id) REFERENCES usuario (usuario_id)
);

CREATE TABLE rol_usuario (
    rol_id NUMBER(2) NOT NULL,
    usuario_id NUMBER(4) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT rol_usuario_pk PRIMARY KEY (rol_id, usuario_id),
    CONSTRAINT rol_usuario_fk01 FOREIGN KEY (rol_id) REFERENCES rol (rol_id),
    CONSTRAINT rol_usuario_fk02 FOREIGN KEY (usuario_id) REFERENCES usuario (usuario_id)
);    
    
CREATE TABLE mediopago_pais (
    mediopago_id NUMBER(2) NOT NULL,
    pais_id NUMBER(3) NOT NULL,
    orden NUMBER(2),
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT mediopago_pais_pk PRIMARY KEY (mediopago_id, pais_id),
    CONSTRAINT mediopago_pais_fk01 FOREIGN KEY (mediopago_id) REFERENCES mediopago (mediopago_id),
    CONSTRAINT mediopago_pais_fk02 FOREIGN KEY (pais_id) REFERENCES pais (pais_id)
);

CREATE TABLE estacion_pais (
    estacion_id NUMBER(4) NOT NULL,
    pais_id NUMBER(3) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT estacion_pais_pk PRIMARY KEY (estacion_id, pais_id),
    CONSTRAINT estacion_pais_fk01 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id),
    CONSTRAINT estacion_pais_fk02 FOREIGN KEY (pais_id) REFERENCES pais (pais_id)
);


CREATE TABLE estacion_conf_head (
    estacionconfhead_id NUMBER(4) PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL,
    estacion_id NUMBER(4) NOT NULL,
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL, creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    CONSTRAINT estacion_conf_head_fk01 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id)
);
CREATE SEQUENCE estacion_conf_head_seq
    START WITH 15
    INCREMENT BY 1
    MAXVALUE 9999
    NOCYCLE
    ORDER;

CREATE TABLE estacion_conf (
    estacionconfhead_id NUMBER(4),
    bomba_id NUMBER(2) NOT NULL,
    tipodespacho_id NUMBER(1) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT estacion_conf_pk PRIMARY KEY (estacionconfhead_id, bomba_id, tipodespacho_id),
    CONSTRAINT estacion_conf_fk01 FOREIGN KEY (estacionconfhead_id) REFERENCES estacion_conf_head (estacionconfhead_id),
    CONSTRAINT estacion_conf_fk02 FOREIGN KEY (bomba_id) REFERENCES bomba (bomba_id),
    CONSTRAINT estacion_conf_fk03 FOREIGN KEY (tipodespacho_id) REFERENCES tipodespacho (tipodespacho_id)
);


CREATE TABLE acceso_rol (
    acceso_id NUMBER(3) NOT NULL,
    rol_id NUMBER(2) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT acceso_rol_pk PRIMARY KEY (acceso_id, rol_id),
    CONSTRAINT acceso_rol_fk01 FOREIGN KEY (acceso_id) REFERENCES acceso (acceso_id),
    CONSTRAINT acceso_rol_fk02 FOREIGN KEY (rol_id) REFERENCES rol (rol_id)
);



/********* TRANSACCIONALES *********/


/*CREATE TABLE horario (
    horario_id NUMBER(11) PRIMARY KEY
    ,estacion_id NUMBER(4) NOT NULL
    ,hora_fin NUMBER(4) NOT NULL
    ,fecha DATE DEFAULT TRUNC(SYSDATE) NOT NULL
    ,estado_id NUMBER(2) NOT NULL
    ,creado_por VARCHAR2(40) NOT NULL
    ,creado_el DATE DEFAULT SYSDATE NOT NULL
    ,creado_persona VARCHAR2(60) NOT NULL
    ,modificado_por VARCHAR2(60)
    ,modificado_el DATE
    ,modificado_persona VARCHAR2(60)
    ,CONSTRAINT horario_fk01 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id)
    ,CONSTRAINT horario_fk02 FOREIGN KEY (estado_id) REFERENCES estado (estado_id)
);
CREATE UNIQUE INDEX horario_idx01 ON horario (estacion_id, hora_fin, fecha);
CREATE SEQUENCE horario_seq
    START WITH 45
    INCREMENT BY 1
    MAXVALUE 99999999999
    NOCYCLE
    ORDER;
*/

CREATE TABLE dia(
    estacion_id NUMBER(4) NOT NULL,
    fecha DATE DEFAULT TRUNC(SYSDATE) NOT NULL,
    estado_id NUMBER(2),
    creado_por VARCHAR2(60) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, creado_persona VARCHAR2(60) NOT NULL,
    modificado_por VARCHAR2(60), modificado_el DATE, modificado_persona VARCHAR2(60),
    CONSTRAINT dia_pk PRIMARY KEY (estacion_id, fecha),
    CONSTRAINT dia_fk01 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id)
);


CREATE TABLE turno (
    turno_id NUMBER(9) PRIMARY KEY,    
    estacion_id NUMBER(4) NOT NULL,
    usuario_id NUMBER(4) NOT NULL,
--    hora_fin NUMBER(4) NOT NULL,
    estado_id NUMBER(2),
--    horario_id NUMBER(11) NOT NULL,
    turno_fusion VARCHAR2(6),
    estacionconfhead_id NUMBER(4),
    fecha DATE NOT NULL,
    creado_por VARCHAR2(60) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, creado_persona VARCHAR2(60) NOT NULL, 
    modificado_por VARCHAR2(60), modificado_el DATE, modificado_persona VARCHAR2(60),
    CONSTRAINT turno_fk01 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id),
    CONSTRAINT turno_fk02 FOREIGN KEY (usuario_id) REFERENCES usuario (usuario_id),
    CONSTRAINT turno_fk03 FOREIGN KEY (estado_id) REFERENCES estado (estado_id),
    CONSTRAINT turno_fk04 FOREIGN KEY (estacionconfhead_id) REFERENCES estacion_conf_head (estacionconfhead_id)
--    CONSTRAINT turno_fk04 FOREIGN KEY (horario_id) REFERENCES horario (horario_id)
);
CREATE SEQUENCE turno_seq
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 999999999
    NOCYCLE
    ORDER;

/*CREATE TABLE bombaestacion_turno (
    estacion_id NUMBER(4) NOT NULL,
    bomba_id NUMBER(2) NOT NULL,
    turno_id NUMBER(9),
    CONSTRAINT bombaestacion_turno_pk PRIMARY KEY (estacion_id, bomba_id, turno_id),
    CONSTRAINT bombaestacion_turno_fk01 FOREIGN KEY (bomba_id, estacion_id) REFERENCES bomba_estacion (bomba_id, estacion_id),
    CONSTRAINT bombaestacion_turno_fk02 FOREIGN KEY (turno_id) REFERENCES turno (turno_id)
);*/


CREATE TABLE lectura (
    lectura_id NUMBER(9) PRIMARY KEY,
    estacion_id NUMBER(4) NOT NULL,
    turno_id NUMBER(9) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, creado_persona VARCHAR2(60) NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    CONSTRAINT lectura_fk01 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id),
    CONSTRAINT lectura_fk02 FOREIGN KEY (turno_id) REFERENCES turno (turno_id)
);
CREATE INDEX lectura_idx01 ON lectura (turno_id);
CREATE SEQUENCE lectura_seq
    START WITH 60
    INCREMENT BY 1
    MAXVALUE 999999999
    NOCYCLE
    ORDER;
    

--La columna estacion_id se coloca de nuevo para validar la relacion con bomba y producto
CREATE TABLE lectura_detalle (
    lectura_id NUMBER(9) NOT NULL,
    estacion_id NUMBER(4) NOT NULL,
    bomba_id NUMBER(2) NOT NULL,
    producto_id NUMBER(2) NOT NULL,
    tipodespacho_id NUMBER(1) NOT NULL,
    tipo VARCHAR2(1) NOT NULL,
    lectura_inicial NUMBER(10, 3) NOT NULL,
    lectura_final NUMBER(10, 3) NOT NULL,
    total NUMBER(10, 3) NOT NULL,
    calibracion NUMBER(10, 3),
    CONSTRAINT lectura_detalle_pk PRIMARY KEY (lectura_id, estacion_id, bomba_id, producto_id, tipo),
    CONSTRAINT lectura_detalle_fk01 FOREIGN KEY (lectura_id) REFERENCES lectura (lectura_id),
    CONSTRAINT lectura_detalle_fk02 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id),
    CONSTRAINT lectura_detalle_fk03 FOREIGN KEY (bomba_id) REFERENCES bomba (bomba_id),
    CONSTRAINT lectura_detalle_fk04 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id),
    CONSTRAINT lectura_detalle_fk05 FOREIGN KEY (producto_id) REFERENCES producto (producto_id),
    CONSTRAINT lectura_detalle_fk06 FOREIGN KEY (tipodespacho_id) REFERENCES tipodespacho (tipodespacho_id)
);


CREATE TABLE lecturafinal (
    estacion_id NUMBER(4) NOT NULL,
    bomba_id NUMBER(2) NOT NULL,
    producto_id NUMBER(2) NOT NULL,
    tipo VARCHAR2(1) NOT NULL,
    lectura_inicial NUMBER(10, 3) NOT NULL,
    lectura_final NUMBER(10, 3) NOT NULL,
    modificado_por VARCHAR2(40), modificado_el DATE, modificado_persona VARCHAR2(60),
    CONSTRAINT lecturafinal_fk01 FOREIGN KEY (estacion_id, bomba_id) REFERENCES bomba_estacion (estacion_id, bomba_id),
    CONSTRAINT lecturafinal_fk02 FOREIGN KEY (estacion_id, producto_id) REFERENCES estacion_producto (estacion_id, producto_id)
);




CREATE TABLE precio (
--    precio_id NUMBER(10) PRIMARY KEY,
    turno_id NUMBER(9) NOT NULL,    
--    lectura_id NUMBER(9) NOT NULL,
    producto_id NUMBER(2) NOT NULL,
    tipodespacho_id NUMBER(1) NOT NULL,
    precio NUMBER(8,2) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, creado_persona VARCHAR2(50) NOT NULL, 
    modificado_por VARCHAR2(40), modificado_el DATE, modificado_persona VARCHAR2(60),
    CONSTRAINT precio_pk PRIMARY KEY (turno_id, producto_id, tipodespacho_id),
    CONSTRAINT precio_fk01 FOREIGN KEY (turno_id) REFERENCES turno (turno_id),
--    CONSTRAINT precio_fk02 FOREIGN KEY (lectura_id) REFERENCES lectura (lectura_id),
    CONSTRAINT precio_fk03 FOREIGN KEY (producto_id) REFERENCES producto (producto_id),
    CONSTRAINT precio_fk04 FOREIGN KEY (tipodespacho_id) REFERENCES tipodespacho (tipodespacho_id)
);
CREATE SEQUENCE precio_seq
    START WITH 70
    INCREMENT BY 1
    MAXVALUE 9999999999
    NOCYCLE
    ORDER;



CREATE TABLE arqueocaja (
    arqueocaja_id NUMBER(9) PRIMARY KEY, 
estacion_id NUMBER(4) NOT NULL,
turno_id NUMBER(9) NOT NULL,
--    horario_id NUMBER(11) NOT NULL,
    fecha DATE NOT NULL,
    estado_id NUMBER(2),
    creado_por VARCHAR2(40) NOT NULL, creado_persona VARCHAR2(50) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_persona VARCHAR2(40), modificado_el DATE,
--    CONSTRAINT arqueocaja_fk01 FOREIGN KEY (horario_id) REFERENCES horario (horario_id)
    CONSTRAINT arqueocaja_fk02 FOREIGN KEY (estado_id) REFERENCES estado (estado_id),
    CONSTRAINT arqueocaja_fk03 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id),
	CONSTRAINT arqueocaja_fk04 FOREIGN KEY (turno_id) REFERENCES turno (turno_id)
);
CREATE SEQUENCE arqueocaja_seq
    START WITH 80
    INCREMENT BY 1
    MAXVALUE 999999999
    NOCYCLE
    ORDER;


CREATE TABLE arqueocaja_bomba (
    arqueocaja_id NUMBER(9) NOT NULL,
    bomba_id NUMBER(2) NOT NULL,
    turno_id NUMBER(9) NOT NULL,
    CONSTRAINT arqueocaja_bomba_pk PRIMARY KEY (arqueocaja_id, bomba_id),
    CONSTRAINT arqueocaja_bomba_fk01 FOREIGN KEY (arqueocaja_id) REFERENCES arqueocaja (arqueocaja_id),
    CONSTRAINT arqueocaja_bomba_fk02 FOREIGN KEY (bomba_id) REFERENCES bomba (bomba_id),
    CONSTRAINT arqueocaja_bomba_fk03 FOREIGN KEY (turno_id) REFERENCES turno (turno_id)
);


CREATE TABLE arqueocaja_detalle (
--    arqueocajadetalle_id NUMBER(11) PRIMARY KEY,
    arqueocaja_id NUMBER(9) NOT NULL,
    mediopago_id NUMBER(2) NOT NULL,
    doctos NUMBER(3) NOT NULL,
    monto NUMBER(10,2) NOT NULL,
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE,
    CONSTRAINT arqueocaja_detalle_pk PRIMARY KEY (arqueocaja_id, mediopago_id),
    CONSTRAINT arqueocaja_detalle_fk01 FOREIGN KEY (arqueocaja_id) REFERENCES arqueocaja (arqueocaja_id),
    CONSTRAINT arqueocaja_detalle_fk02 FOREIGN KEY (mediopago_id) REFERENCES mediopago (mediopago_id)
);
/*CREATE SEQUENCE arqueocaja_detalle_seq
    START WITH 90
    INCREMENT BY 1
    MAXVALUE 99999999999
    NOCYCLE
    ORDER;*/


CREATE TABLE efectivo (
    efectivo_id NUMBER(10) PRIMARY KEY,
    arqueocaja_id NUMBER(9) NOT NULL,
    mediopago_id NUMBER(2) NOT NULL,
    orden NUMBER(7),
    monto NUMBER(9,2) NOT NULL,
    tasa NUMBER(8, 3),
    mon_extranjera NUMBER(9,2),
    --CONSTRAINT efectivo_pk PRIMARY KEY (arqueocaja_id, mediopago_id, orden)
    CONSTRAINT efectivo_fk01 FOREIGN KEY (arqueocaja_id) REFERENCES arqueocaja (arqueocaja_id),
    CONSTRAINT efectivo_fk02 FOREIGN KEY (mediopago_id) REFERENCES mediopago (mediopago_id)
);
CREATE INDEX efectivo_idx01 ON efectivo (arqueocaja_id, mediopago_id);
CREATE SEQUENCE efectivo_seq
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 9999999999
    NOCYCLE
    ORDER;


CREATE TABLE arqueocaja_producto (
    arqueocaja_id NUMBER(9) NOT NULL,
    producto_id NUMBER(2) NOT NULL,
    monto NUMBER(9,2) NOT NULL,
    CONSTRAINT arqueocaja_producto_pk PRIMARY KEY (arqueocaja_id, producto_id)
    ,CONSTRAINT arqueocaja_producto_fk01 FOREIGN KEY (arqueocaja_id) REFERENCES arqueocaja (arqueocaja_id)
    ,CONSTRAINT arqueocaja_producto_fk02 FOREIGN KEY (producto_id) REFERENCES producto (producto_id)
);


CREATE TABLE factelectronica_pos (
    arqueocaja_id NUMBER(9) NOT NULL,
    producto_id NUMBER(2) NOT NULL,
    galones NUMBER(7,3) NOT NULL,
    monto NUMBER(9,2) NOT NULL,
    CONSTRAINT factelectronica_pos_pk PRIMARY KEY (arqueocaja_id, producto_id)
    ,CONSTRAINT factelectronica_pos_fk01 FOREIGN KEY (arqueocaja_id) REFERENCES arqueocaja (arqueocaja_id)
    ,CONSTRAINT factelectronica_pos_fk02 FOREIGN KEY (producto_id) REFERENCES producto (producto_id)
);
--CREATE INDEX efectivo_idx01 ON efectivo (arqueocaja_id, mediopago_id);

CREATE TABLE inventario_coco (
    fecha DATE NOT NULL,
	estacion_id NUMBER(4) NOT NULL,
    producto_id NUMBER(2) NOT NULL,
	inicial NUMBER(11,2) NOT NULL,
	final NUMBER(11,2) NOT NULL,
	compras NUMBER(11,2),
    creado_por VARCHAR2(40) NOT NULL, creado_el DATE DEFAULT SYSDATE NOT NULL, creado_persona VARCHAR2(40) NOT NULL, modificado_por VARCHAR2(40), modificado_el DATE, modificado_persona VARCHAR2(40),
	CONSTRAINT inventario_coco_pk PRIMARY KEY (fecha, estacion_id, producto_id),
    CONSTRAINT inventario_coco_fk01 FOREIGN KEY (estacion_id) REFERENCES estacion (estacion_id),
    CONSTRAINT inventario_coco_fk02 FOREIGN KEY (producto_id) REFERENCES producto (producto_id)
);







/*** INSERTS ***/
/***************/



INSERT INTO rol (rol_id, nombre, descripcion, rolpadre_id, creado_por) VALUES (1, 'SYSADMIN', 'Rol de administrador del sistema, ve TODAS las panallas y tiene TODOS los privilegios.', NULL, 'hgbarrientos');
INSERT INTO rol (rol_id, nombre, descripcion, rolpadre_id, creado_por) VALUES (2, 'ADMINISTRATIVO', 'Rol para los usuarios de oficinas centrales.', NULL, 'hgbarrientos');
INSERT INTO rol (rol_id, nombre, descripcion, rolpadre_id, creado_por) VALUES (3, 'SUPERVISOR', 'Define el rol de administrador de una estacion.', 2, 'hgbarrientos');
INSERT INTO rol (rol_id, nombre, descripcion, rolpadre_id, creado_por) VALUES (4, 'CAJERO', 'Rol para los cajeros de una estacion.', 3, 'hgbarrientos');
INSERT INTO rol (rol_id, nombre, descripcion, rolpadre_id, creado_por) VALUES (5, 'GERENTE', 'Rol para usuarios que tendrán posibilidad de modificar cualquier cuadre de cualquier dia', NULL, 'hgbarrientos');
INSERT INTO rol (rol_id, nombre, descripcion, rolpadre_id, creado_por) VALUES (6, 'REPORTES', 'Rol para usuarios que tendrán posibilidad de generar reportes', NULL, 'hgbarrientos');
INSERT INTO rol (rol_id, nombre, descripcion, rolpadre_id, creado_por) VALUES (7, 'JEFE_PAIS', 'Rol para usuarios que sean jefe de pais. NO tiene accesos relacionados, es relacionado a perfiles.', NULL, 'hgbarrientos');

--50
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (usuario_seq.NEXTVAL, 'hgbarrientos', 'hgbarrientos', 'Henry', 'Barrientos', 'hgbarrientos');
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (1, usuario_seq.CURRVAL, 'hgbarrientos');

INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (usuario_seq.NEXTVAL, 'cmeier', 'cmeier', 'Christoph', 'Meier', 'hgbarrientos');
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (1, usuario_seq.CURRVAL, 'hgbarrientos');

INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (usuario_seq.NEXTVAL, 'amedrano', 'amedrano', 'Antonio', 'Medrano', 'hgbarrientos');
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (1, usuario_seq.CURRVAL, 'hgbarrientos');

INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (usuario_seq.NEXTVAL, 'aturcios', 'aturcios', 'Alberto', 'Turcios', 'hgbarrientos');
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, usuario_seq.CURRVAL, 'hgbarrientos');

INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (usuario_seq.NEXTVAL, 'cajero1', 'cajero1', 'Cajero', 'Uno', 'hgbarrientos');
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, usuario_seq.CURRVAL, 'hgbarrientos');
--55
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (55, 'cajero320100', 'cajero', 'Cajero', 'Guatemala', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 55, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (56, 'cajero320101', 'cajero', 'Cajero', 'Guatemala', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 56, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (57, 'cajero320102', 'cajero', 'Cajero', 'Guatemala', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 57, 'hgbarrientos');
		
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (58, 'cajero340100', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 58, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (59, 'cajero340101', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 59, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (60, 'cajero340102', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 60, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (61, 'cajero340103', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 61, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (62, 'cajero340104', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 62, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (63, 'cajero340105', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 63, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (64, 'cajero340106', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 64, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (65, 'cajero340107', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 65, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (66, 'cajero340108', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 66, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (67, 'cajero340109', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 67, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (68, 'cajero340110', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 68, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (69, 'cajero340111', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 69, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (70, 'cajero340112', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 70, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (71, 'cajero340113', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 71, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (72, 'cajero340114', 'cajero', 'Cajero', 'Honduras', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 72, 'hgbarrientos');
		
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (73, 'cajero222100', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 73, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (74, 'cajero222101', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 74, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (75, 'cajero222102', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 75, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (76, 'cajero222103', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 76, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (77, 'cajero222104', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 77, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (78, 'cajero222105', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 78, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (79, 'cajero222106', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 79, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (80, 'cajero222107', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 80, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (81, 'cajero222108', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 81, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (82, 'cajero222109', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 82, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (83, 'cajero222110', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 83, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (84, 'cajero222111', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 84, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (85, 'cajero222112', 'cajero', 'Cajero', 'Salvador', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 85, 'hgbarrientos');
		
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (86, 'cajero558100', 'cajero', 'Cajero', 'Nicaragua', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 86, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (87, 'cajero558101', 'cajero', 'Cajero', 'Nicaragua', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 87, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (88, 'cajero558102', 'cajero', 'Cajero', 'Nicaragua', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 88, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (89, 'cajero558103', 'cajero', 'Cajero', 'Nicaragua', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 89, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (90, 'cajero558104', 'cajero', 'Cajero', 'Nicaragua', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 90, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (91, 'cajero558105', 'cajero', 'Cajero', 'Nicaragua', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 91, 'hgbarrientos');
		
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (92, 'cajero188100', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 92, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (93, 'cajero188101', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 93, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (94, 'cajero188102', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 94, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (95, 'cajero188103', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 95, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (96, 'cajero188104', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 96, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (97, 'cajero188105', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 97, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (98, 'cajero188106', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 98, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (99, 'cajero188107', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 99, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (100, 'cajero188108', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 100, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (101, 'cajero188109', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 101, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (102, 'cajero188110', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 102, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (103, 'cajero188111', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 103, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (104, 'cajero188112', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 104, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (105, 'cajero188113', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 105, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (106, 'cajero188114', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 106, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (107, 'cajero188115', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 107, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (108, 'cajero188116', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 108, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (109, 'cajero188117', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 109, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (110, 'cajero188118', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 110, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (111, 'cajero188119', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 111, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (112, 'cajero188120', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 112, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (113, 'cajero188121', 'cajero', 'Cajero', 'CostaR', 'hgbarrientos');		INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (4, 113, 'hgbarrientos');





--Codigos basados en el standard ISO3166-1
INSERT INTO pais (pais_id, codigo, creado_por, nombre, moneda_simbolo, vol_simbolo) VALUES (188, 'CRI', 'hgbarrientos', 'COSTA RICA', 'CRC', 'L');
INSERT INTO pais (pais_id, codigo, creado_por, nombre, moneda_simbolo, vol_simbolo) VALUES (222, 'SLV', 'hgbarrientos', 'EL SALVADOR', 'USD', 'L');
INSERT INTO pais (pais_id, codigo, creado_por, nombre, moneda_simbolo, vol_simbolo) VALUES (320, 'GTM', 'hgbarrientos', 'GUATEMALA', 'GTQ', 'AG');
INSERT INTO pais (pais_id, codigo, creado_por, nombre, moneda_simbolo, vol_simbolo) VALUES (340, 'HND', 'hgbarrientos', 'HONDURAS', 'HNL', 'L');
INSERT INTO pais (pais_id, codigo, creado_por, nombre, moneda_simbolo, vol_simbolo) VALUES (558, 'NIC', 'hgbarrientos', 'NICARAGUA', 'NIO', 'L');
    


INSERT INTO tipodespacho (tipodespacho_id, nombre, nombre_corto, creado_por) VALUES (1, 'Autoservicio', 'AUTO', 'hgbarrientos');
INSERT INTO tipodespacho (tipodespacho_id, nombre, nombre_corto, creado_por) VALUES (2, 'Servicio completo', 'FULL', 'hgbarrientos');



INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (1, 'Bomba 1', 1, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (2, 'Bomba 2', 1, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (3, 'Bomba 3', 2, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (4, 'Bomba 4', 2, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (5, 'Bomba 5', 3, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (6, 'Bomba 6', 3, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (7, 'Bomba 7', 4, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (8, 'Bomba 8', 4, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (9, 'Bomba 9', 5, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (10, 'Bomba 10', 5, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (11, 'Bomba 11', 6, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (12, 'Bomba 12', 6, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (13, 'Bomba 13', 7, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (14, 'Bomba 14', 7, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (15, 'Bomba 15', 8, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (16, 'Bomba 16', 8, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (17, 'Bomba 17', 9, 'hgbarrientos');    
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (18, 'Bomba 18', 9, 'hgbarrientos');
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (19, 'Bomba 19', 10, 'hgbarrientos');
INSERT INTO bomba (bomba_id, nombre, isla, creado_por) VALUES (20, 'Bomba 20', 10, 'hgbarrientos');
    

    
--100
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por, fact_electronica) VALUES (estacion_seq.NEXTVAL, 'Flores del lago', '100100', 320, 'hgbarrientos', 'S');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por, fact_electronica) VALUES (estacion_seq.NEXTVAL, 'San Juan Sacatepequez', '100102', 320, 'hgbarrientos', 'S');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por, fact_electronica) VALUES (estacion_seq.NEXTVAL, 'Jutiapa', '100104', 320, 'hgbarrientos', 'S');
--103
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO / PISTA TEPEYAC', '340100', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO PRESIDENCIAL', '340101', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO / PISTA PARQUE EMPRESARIAL', '340102', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO AEROPUERTO TONCONTÍN', '340103', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA FLASHMART RAMON VILLEDA (Aeropuerto SPS)', '340104', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO / PISTA  FLORENCIA', '340105', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA FLASHMART / PISTA UNO EL EDEN', '340106', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'PISTA UNO TERMINAL DE BUSES', '340107', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA FLASHMART LA PUERTA', '340108', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO / PISTA MANCHEN', '340109', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO / PISTA UNIVERSITARIA', '340110', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO CIRCUNVALACIÓN', '340111', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'ESTACIÓN JUAN PABLO II', '340112', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO CENTRO', '340113', 340, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Bodega Central', '340114', 340, 'hgbarrientos');
--118
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Prolongación Juan Pablo', '222100', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO San Jacinto', '222101', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Las Delicias', '222102', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Los Proceres', '222103', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Transal', '222104', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Carretera de Oro', '222105', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Arboledas', '222106', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Santa Elena', '222107', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Roosevelt', '222108', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Autopista Sur', '222109', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Zona Rosa', '222110', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Constitución', '222111', 222, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'UNO Miramonte', '222112', 222, 'hgbarrientos');
--131
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO / PISTA LAS COLINAS', '558100', 558, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'LUBRICENTRO LAS COLINAS', '558101', 558, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO / PISTA LOMAS DE GUADALUPE', '558102', 558, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'TIENDA PRONTO / PISTA SANTA CLARA', '558103', 558, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'INTERNACIONAL', '558104', 558, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'PISTA SAN DONATO', '558105', 558, 'hgbarrientos');
--137
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Aguas Zarcas', '188100', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Uno Cariari II  (Zona Atlántica)', '188101', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Limon', '188102', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Pocora', '188103', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Siquirres', '188104', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Ticaban', '188105', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'La Rita', '188106', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Uruca 1', '188107', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Uruca 2', '188108', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Hatillo', '188109', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Lahman', '188110', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Tibas', '188111', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'San Sebastian I', '188112', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Esparza', '188113', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'San Miguel', '188114', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Guadalupe', '188115', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Barreal', '188116', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Cariari I', '188117', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'La Begonia/ Paseo de los Estudiantes', '188118', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'San Sebasstian II', '188119', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'La Irma / Abangares', '188120', 188, 'hgbarrientos');
INSERT INTO estacion (estacion_id, nombre, codigo, pais_id, creado_por) VALUES (estacion_seq.NEXTVAL, 'Pital', '188121', 188, 'hgbarrientos');



INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (100, 55, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (101, 56, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (102, 57, 'hgbarrientos');

INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (103, 58, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (104, 59, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (105, 60, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (106, 61, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (107, 62, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (108, 63, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (109, 64, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (110, 65, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (111, 66, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (112, 67, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (113, 68, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (114, 69, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (115, 70, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (116, 71, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (117, 72, 'hgbarrientos');

INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (118, 73, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (119, 74, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (120, 75, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (121, 76, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (122, 77, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (123, 78, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (124, 79, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (125, 80, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (126, 81, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (127, 82, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (128, 83, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (129, 84, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (130, 85, 'hgbarrientos');

INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (131, 86, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (132, 87, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (133, 88, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (134, 89, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (135, 90, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (136, 91, 'hgbarrientos');

INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (137, 92, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (138, 93, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (139, 94, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (140, 95, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (141, 96, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (142, 97, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (143, 98, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (144, 99, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (145, 100, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (146, 101, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (147, 102, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (148, 103, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (149, 104, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (150, 105, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (151, 106, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (152, 107, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (153, 108, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (154, 109, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (155, 110, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (156, 111, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (157, 112, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (158, 113, 'hgbarrientos');



--Usuarios de tipo Supervisor
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (114, 'supervisor340100', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 114, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (103, 114, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (115, 'supervisor340101', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 115, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (104, 115, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (116, 'supervisor340102', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 116, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (105, 116, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (117, 'supervisor340103', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 117, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (106, 117, 'hgbarrientos');

INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (118, 'supervisor340104', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 118, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (107, 118, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (119, 'supervisor340105', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 119, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (108, 119, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (120, 'supervisor340106', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 120, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (109, 120, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (121, 'supervisor340107', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 121, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (110, 121, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (122, 'supervisor340108', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 122, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (111, 122, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (123, 'supervisor340109', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 123, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (112, 123, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (124, 'supervisor340110', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 124, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (113, 124, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (125, 'supervisor340111', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 125, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (114, 125, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (126, 'supervisor340112', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 126, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (115, 126, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (127, 'supervisor340113', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 127, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (116, 127, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (128, 'supervisor340114', 'super', 'Supervisor', 'HND', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 128, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (117, 128, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (129, 'supervisor222100', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 129, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (118, 129, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (130, 'supervisor222101', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 130, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (119, 130, 'hgbarrientos');

INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (131, 'supervisor222102', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 131, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (120, 131, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (132, 'supervisor222103', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 132, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (121, 132, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (133, 'supervisor222104', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 133, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (122, 133, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (134, 'supervisor222105', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 134, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (123, 134, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (135, 'supervisor222106', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 135, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (124, 135, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (136, 'supervisor222107', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 136, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (125, 136, 'hgbarrientos');

INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (137, 'supervisor222108', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 137, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (126, 137, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (138, 'supervisor222109', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 138, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (127, 138, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (139, 'supervisor222110', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 139, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (128, 139, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (140, 'supervisor222111', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 140, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (129, 140, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (141, 'supervisor222112', 'super', 'Supervisor', 'SLV', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 141, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (130, 141, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (142, 'supervisor558100', 'super', 'Supervisor', 'NIC', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 142, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (131, 142, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (143, 'supervisor558101', 'super', 'Supervisor', 'NIC', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 143, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (132, 143, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (144, 'supervisor558102', 'super', 'Supervisor', 'NIC', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 144, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (133, 144, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (145, 'supervisor558103', 'super', 'Supervisor', 'NIC', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 145, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (134, 145, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (146, 'supervisor558104', 'super', 'Supervisor', 'NIC', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 146, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (135, 146, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (147, 'supervisor558105', 'super', 'Supervisor', 'NIC', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 147, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (136, 147, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (148, 'supervisor188100', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 148, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (137, 148, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (149, 'supervisor188101', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 149, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (138, 149, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (150, 'supervisor188102', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 150, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (139, 150, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (151, 'supervisor188103', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 151, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (140, 151, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (152, 'supervisor188104', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 152, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (141, 152, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (153, 'supervisor188105', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 153, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (142, 153, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (154, 'supervisor188106', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 154, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (143, 154, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (155, 'supervisor188107', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 155, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (144, 155, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (156, 'supervisor188108', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 156, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (145, 156, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (157, 'supervisor188109', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 157, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (146, 157, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (158, 'supervisor188110', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 158, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (147, 158, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (159, 'supervisor188111', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 159, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (148, 159, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (160, 'supervisor188112', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 160, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (149, 160, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (161, 'supervisor188113', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 161, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (150, 161, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (162, 'supervisor188114', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 162, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (151, 162, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (163, 'supervisor188115', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 163, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (152, 163, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (164, 'supervisor188116', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 164, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (153, 164, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (165, 'supervisor188117', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 165, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (154, 165, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (166, 'supervisor188118', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 166, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (155, 166, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (167, 'supervisor188119', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 167, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (156, 167, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (168, 'supervisor188120', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 168, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (157, 168, 'hgbarrientos');
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (169, 'supervisor188121', 'super', 'Supervisor', 'CRI', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (3, 169, 'hgbarrientos');
INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por) VALUES (158, 169, 'hgbarrientos');

--Usuarios de tipo Administrativo
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (170, 'vlopez', 'vlopez', 'Vivian', 'López', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (2, 170, 'hgbarrientos');




INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 103, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 103, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 103, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 103, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 103, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 103, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 103, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 103, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (13, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (14, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (15, 104, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (16, 104, 'hgbarrientos');
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 105, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 105, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 105, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 105, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 105, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 105, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 105, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 105, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (13, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (14, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (15, 106, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (16, 106, 'hgbarrientos');
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (13, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (14, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (15, 107, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (16, 107, 'hgbarrientos');
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 108, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 108, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 108, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 108, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 108, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 108, 'hgbarrientos');										
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 109, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 109, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 109, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 109, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 109, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 109, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 109, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 109, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 109, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 109, 'hgbarrientos');						
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 110, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 110, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 110, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 110, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 110, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 110, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 110, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 110, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 110, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 110, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 110, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 110, 'hgbarrientos');				
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (13, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (14, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (15, 111, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (16, 111, 'hgbarrientos');
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 112, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 112, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 112, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 112, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 112, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 112, 'hgbarrientos');										
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 113, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 113, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 113, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 113, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 113, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 113, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 113, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 113, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 113, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 113, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 113, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 113, 'hgbarrientos');				
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (13, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (14, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (15, 114, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (16, 114, 'hgbarrientos');
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 115, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 115, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 115, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 115, 'hgbarrientos');												
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (13, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (14, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (15, 116, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (16, 116, 'hgbarrientos');
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (13, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (14, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (15, 117, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (16, 117, 'hgbarrientos');
															
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 118, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 118, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 118, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 118, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 118, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 118, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 118, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 118, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 118, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 118, 'hgbarrientos');						
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 119, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 119, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 119, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 119, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 119, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 119, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 119, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 119, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 119, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 119, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 119, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 119, 'hgbarrientos');				
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 120, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 120, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 120, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 120, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 120, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 120, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 120, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 120, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 120, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 120, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 120, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 120, 'hgbarrientos');				
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 121, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 121, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 121, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 121, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 121, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 121, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 121, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 121, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 122, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 122, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 122, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 122, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 122, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 122, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 122, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 122, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 122, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 122, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 122, 'hgbarrientos');					
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 123, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (13, 123, 'hgbarrientos');			
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 124, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 124, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 124, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 124, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 124, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 124, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 124, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 124, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 124, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 124, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 124, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 124, 'hgbarrientos');				
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 125, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 125, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 125, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 125, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 125, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 125, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 125, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 125, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 126, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 126, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 126, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 126, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 126, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 126, 'hgbarrientos');										
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 127, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 127, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 127, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 127, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 127, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 127, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 127, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 127, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 127, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 127, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 127, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 127, 'hgbarrientos');				
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 128, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 128, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 128, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 128, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 128, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 128, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 128, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 128, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 129, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 129, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 129, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 129, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 129, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 129, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 129, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 129, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 129, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 129, 'hgbarrientos');						
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 130, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 130, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 130, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 130, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 130, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 130, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 130, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 130, 'hgbarrientos');								
															
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 131, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 131, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 131, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 131, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 131, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 131, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 131, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 131, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 131, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 131, 'hgbarrientos');						
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 132, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 132, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 132, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 132, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 132, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 132, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 132, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 132, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 132, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 132, 'hgbarrientos');						
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 133, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 133, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 133, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 133, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 133, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 133, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 133, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 133, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 134, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 134, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 134, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 134, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 134, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 134, 'hgbarrientos');										
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 135, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 135, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 135, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 135, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 135, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 135, 'hgbarrientos');										
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 136, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 136, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 136, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 136, 'hgbarrientos');												
															
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 137, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 137, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 137, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 137, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 137, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 137, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 137, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 137, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 138, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 138, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 138, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 138, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 138, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 138, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 138, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 138, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 139, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 139, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 139, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 139, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 139, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 139, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 139, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 139, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 139, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 139, 'hgbarrientos');						
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 140, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 140, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 140, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 140, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 140, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 140, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 140, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 140, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 140, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 140, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 140, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 140, 'hgbarrientos');				
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 141, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 141, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 141, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 141, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 141, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 141, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 141, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 141, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 141, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 141, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 141, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 141, 'hgbarrientos');				
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 142, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 142, 'hgbarrientos');														
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 143, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 143, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 143, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 143, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 143, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 143, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 143, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 143, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 144, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 144, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 144, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 144, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 144, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 144, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 144, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 144, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 144, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 144, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 144, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 144, 'hgbarrientos');				
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 145, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 145, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 145, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 145, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 145, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 145, 'hgbarrientos');										
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 146, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 146, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 146, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 146, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 146, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 146, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 146, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 146, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 146, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 146, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 146, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 146, 'hgbarrientos');				
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 147, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 147, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 147, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 147, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 147, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 147, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 147, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 147, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 147, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 147, 'hgbarrientos');						
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 148, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 148, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 148, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 148, 'hgbarrientos');												
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 149, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 149, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 149, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 149, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 149, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 149, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 149, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 149, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 149, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 149, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (11, 149, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (12, 149, 'hgbarrientos');				
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 150, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 150, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 150, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 150, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 150, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 150, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 150, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 150, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 151, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 151, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 151, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 151, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 151, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 151, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 151, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 151, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (9, 151, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (10, 151, 'hgbarrientos');						
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 152, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 152, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 152, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 152, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 152, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 152, 'hgbarrientos');										
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 153, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 153, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 153, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 153, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 153, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 153, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 153, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 153, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 154, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 154, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 154, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 154, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 154, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 154, 'hgbarrientos');										
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 155, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 155, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 155, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 155, 'hgbarrientos');												
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 156, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 156, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 156, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 156, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 156, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 156, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (7, 156, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (8, 156, 'hgbarrientos');								
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 157, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 157, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 157, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 157, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 157, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 157, 'hgbarrientos');										
INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (1, 158, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (2, 158, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (3, 158, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (4, 158, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (5, 158, 'hgbarrientos');	INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por) VALUES (6, 158, 'hgbarrientos');										




INSERT INTO tipoproducto (tipo_id, nombre, creado_por) VALUES (1, 'Combustibles', 'hgbarrientos');
INSERT INTO tipoproducto (tipo_id, nombre, creado_por) VALUES (2, 'Lubricantes', 'hgbarrientos');
INSERT INTO tipoproducto (tipo_id, nombre, creado_por) VALUES (3, 'Miscelaneos', 'hgbarrientos');


-- 001
INSERT INTO producto (producto_id, orden_pos, nombre, codigo, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 3, 'Super', 'SUP000', 1, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, codigo, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 4, 'Regular', 'REG000', 1, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, codigo, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 1, 'Diesel', 'DIE000', 1, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, codigo, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 2, 'Diesel ULS', 'DIEULS', 1, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, codigo, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Lubricantes', 'LUBS', 2, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, codigo, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Miscelaneos', 'MISC', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, codigo, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Diesel con Dynamax', 'DIEDYMX', 1, 'hgbarrientos');

-- 008
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Descuento s/Venta Lubricantes', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Descuentos s/Venta Tienda Exenta', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Descuentos s/Venta Tienda Gravada 15%', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Descuentos s/Venta Tienda Gravada 18%', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Venta Bruta Lubricantes', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Venta Bruta Tienda Exenta', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Venta Bruta Tienda Gravada 15%', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Venta Bruta Tienda Gravada 18%', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Venta Tienda ISV 15%', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Venta Tienda ISV 18%', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Ventas Lubricantes ISV 15%', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Miscelaneos', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Ventas Llantas', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Ventas Lubricantes', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Ventas Servicentro', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Ventas Tienda exenta', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Ventas Tienda gravada', 3, 'hgbarrientos');
INSERT INTO producto (producto_id, orden_pos, nombre, tipo_id, creado_por) VALUES (producto_seq.NEXTVAL, 0, 'Ventas Tienda', 3, 'hgbarrientos');








INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (103, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (104, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (105, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (106, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (107, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (108, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (109, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (110, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (111, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (112, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (113, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (114, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (115, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (116, 17, 'hgbarrientos');	 						
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 3, 'hgbarrientos');	 	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 7, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 8, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 9, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 10, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 11, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 12, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 13, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 14, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 15, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 16, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (117, 17, 'hgbarrientos');	 						
			 	 																		
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (118, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (118, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (118, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (118, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (118, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (118, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (119, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (119, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (119, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (119, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (119, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (119, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (120, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (120, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (120, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (120, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (120, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (120, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (121, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (121, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (121, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (121, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (121, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (121, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (122, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (122, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (122, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (122, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (122, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (122, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (123, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (123, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (123, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (123, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (123, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (123, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (124, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (124, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (124, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (124, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (124, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (124, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (125, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (125, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (125, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (125, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (125, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (125, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (126, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (126, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (126, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (126, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (126, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (126, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (127, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (127, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (127, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (127, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (127, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (127, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (128, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (128, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (128, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (128, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (128, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (128, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (129, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (129, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (129, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (129, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (129, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (129, 23, 'hgbarrientos');
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (130, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (130, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (130, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (130, 17, 'hgbarrientos');	 	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (130, 19, 'hgbarrientos');	 			INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (130, 23, 'hgbarrientos');
			 	 																		
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (131, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (131, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (131, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (131, 17, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (131, 18, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (131, 19, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (131, 20, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (131, 21, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (131, 22, 'hgbarrientos');	 
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (132, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (132, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (132, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (132, 17, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (132, 18, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (132, 19, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (132, 20, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (132, 21, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (132, 22, 'hgbarrientos');	 
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (133, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (133, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (133, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (133, 17, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (133, 18, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (133, 19, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (133, 20, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (133, 21, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (133, 22, 'hgbarrientos');	 
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (134, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (134, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (134, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (134, 17, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (134, 18, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (134, 19, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (134, 20, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (134, 21, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (134, 22, 'hgbarrientos');	 
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (135, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (135, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (135, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (135, 17, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (135, 18, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (135, 19, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (135, 20, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (135, 21, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (135, 22, 'hgbarrientos');	 
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (136, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (136, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (136, 3, 'hgbarrientos');	 	 	 											INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (136, 17, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (136, 18, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (136, 19, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (136, 20, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (136, 21, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (136, 22, 'hgbarrientos');	 
			 	 																		
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (137, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (137, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (137, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (138, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (138, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (138, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (139, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (139, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (139, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (140, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (140, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (140, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (141, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (141, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (141, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (142, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (142, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (142, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (143, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (143, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (143, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (144, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (144, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (144, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (145, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (145, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (145, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (146, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (146, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (146, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (147, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (147, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (147, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (148, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (148, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (148, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (149, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (149, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (149, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (150, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (150, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (150, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (151, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (151, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (151, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (152, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (152, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (152, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (153, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (153, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (153, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (154, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (154, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (154, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (155, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (155, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (155, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (156, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (156, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (156, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (157, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (157, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (157, 3, 'hgbarrientos');	 	 	 																	
INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (158, 1, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (158, 2, 'hgbarrientos');	INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) VALUES (158, 3, 'hgbarrientos');	 	 	 																	






INSERT INTO estado (estado_id, nombre, descripcion, creado_por) VALUES (1, 'INGRESADO', 'Estado que identifica el estado recien ingresado de un registro.', 'hgbarrientos');
INSERT INTO estado (estado_id, nombre, descripcion, creado_por) VALUES (2, 'CERRADO', 'Estado que identifica el estado cerrado/finalizado de un registro..', 'hgbarrientos');

INSERT INTO estado (estado_id, nombre, descripcion, creado_por) VALUES (3, 'Completado cajero', 'Estado que identifica que un cajero termino un proceso', 'hgbarrientos');
INSERT INTO estado (estado_id, nombre, descripcion, creado_por) VALUES (4, 'Completado supervisor', 'Estado que identifica que un supervisor termino un proceso', 'hgbarrientos');



INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 103, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 104, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 13, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 14, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 15, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 16, 2, 'hgbarrientos');
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 105, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 106, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 13, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 14, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 15, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 16, 2, 'hgbarrientos');
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 107, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 13, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 14, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 15, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 16, 2, 'hgbarrientos');
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 108, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');										
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 109, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');						
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 110, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');				
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 111, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 13, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 14, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 15, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 16, 2, 'hgbarrientos');
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 112, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');										
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 113, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');				
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 114, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 13, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 14, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 15, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 16, 2, 'hgbarrientos');
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 115, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');												
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 116, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 13, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 14, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 15, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 16, 2, 'hgbarrientos');
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 117, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 13, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 14, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 15, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 16, 2, 'hgbarrientos');
																
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 118, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');						
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 119, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');				
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 120, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');				
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 121, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 122, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');					
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 123, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 13, 2, 'hgbarrientos');			
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 124, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');				
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 125, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 126, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');										
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 127, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');				
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 128, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 129, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');						
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 130, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
																
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 131, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');						
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 132, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');						
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 133, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 134, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');										
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 135, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');										
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 136, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');												
																
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 137, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 138, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 139, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');						
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 140, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');				
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 141, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');				
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 142, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');														
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 143, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 144, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');				
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 145, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');										
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 146, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');				
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 147, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');						
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 148, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');												
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 149, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 11, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 12, 2, 'hgbarrientos');				
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 150, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 151, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 9, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 10, 2, 'hgbarrientos');						
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 152, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');										
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 153, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 154, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');										
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 155, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');												
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 156, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 7, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 8, 2, 'hgbarrientos');								
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 157, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');										
INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, creado_por) VALUES (estacion_conf_head_seq.NEXTVAL, 'FULL', 158, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 1, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 2, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 3, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 4, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 5, 2, 'hgbarrientos');	INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por) VALUES (estacion_conf_head_seq.CURRVAL, 6, 2, 'hgbarrientos');										




--Tipos generales
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Cupón Combustible');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 340, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 222, 'hgbarrientos');
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Cupón Tienda');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 340, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 222, 'hgbarrientos');
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Descuento 8% TC');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 340, 'hgbarrientos');            
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 2, 'hgbarrientos', 'Efectivo Combustible');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 340, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 222, 'hgbarrientos');                
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 2, 'hgbarrientos', 'Efectivo Lubricante');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 340, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 222, 'hgbarrientos');
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 2, 'hgbarrientos', 'Efectivo Tienda');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 340, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 222, 'hgbarrientos');
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta de crédito BAC');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 340, 'hgbarrientos');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 222, 'hgbarrientos');
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta de crédito Ficohsa');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 340, 'hgbarrientos');            
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Ventas de crédito combustibles');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 340, 'hgbarrientos');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 222, 'hgbarrientos');
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Ventas Lubricantes');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 340, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 222, 'hgbarrientos');
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Ventas Tienda');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 340, 'hgbarrientos');            
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 222, 'hgbarrientos');
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Retención');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta de crédito Banco Nacional');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta de crédito BCR');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta de crédito Credomatic');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta de crédito Davivienda');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta de crédito Fleet Magic SB');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta de crédito Versatec');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta Flota BAC');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta Flota BCR');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Ventas Prepago Combustibles');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Ventas UNO Plus');        
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 188, 'hgbarrientos');        
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Autoconsumo (Gasto)');            
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Retención');            
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta de crédito BANPRO');            
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Ventas B');            
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Ventas C');            
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Ventas Handling');            
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Ventas Prepago Combustibles');            
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Ventas Subsidio');            
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');    
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Tarjeta de crédito ATH');                
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 2, 'hgbarrientos', 'Efectivo en dólares');    
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (32, 340, 'hgbarrientos');
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (32, 188, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (32, 558, 'hgbarrientos');    INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (32, 222, 'hgbarrientos');
INSERT INTO mediopago (mediopago_id, tipo, creado_por, nombre) VALUES (mediopago_seq.NEXTVAL, 1, 'hgbarrientos', 'Ventas clientes anticipo');                
INSERT INTO mediopago_pais (mediopago_id, pais_id, creado_por) VALUES (mediopago_seq.CURRVAL, 558, 'hgbarrientos');





INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 08, 188, 559.55, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 07, 188, 559.50, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 09, 188, 560.00, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 10, 188, 560.10, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 11, 188, 560.20, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 12, 188, 560.30, 'hgbarrientos');

INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 07, 222, 1, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 08, 222, 1, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 09, 222, 1, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 10, 222, 1, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 11, 222, 1, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 12, 222, 1, 'hgbarrientos');

INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 07, 320, 7.20, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 08, 320, 7.25, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 09, 320, 7.31, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 10, 320, 7.31, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 11, 320, 7.32, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 12, 320, 7.33, 'hgbarrientos');

INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 07, 340, 23.45, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 08, 340, 23.50, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 09, 340, 23.54, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 10, 340, 23.55, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 11, 340, 23.56, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 12, 340, 23.57, 'hgbarrientos');

INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 07, 558, 30.20, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 08, 558, 30.25, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 09, 558, 30.29, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 10, 558, 30.30, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 11, 558, 30.31, 'hgbarrientos');
INSERT INTO tasacambio (anio, mes, pais_id, tasa, creado_por) VALUES (2017, 12, 558, 30.32, 'hgbarrientos');


INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (01, 'hgbarrientos', 'Principal', 	NULL, 1, NULL, 'Opción raíz del módulo principal');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (02, 'hgbarrientos', 'Mantenimiento',	NULL, 2, NULL, 'Opción raíz del módulo de mantenimientos');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (03, 'hgbarrientos', 'Reportes', 		NULL, 3, NULL, 'Opción raíz del módulo de reportes');

INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (04, 'hgbarrientos', 'Turno', 		01, 01, 'PR_TURN', 'Pantalla para creación de turno');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (05, 'hgbarrientos', 'Lecturas', 		01, 02, 'PR_READING', 'Pantalla para ingreso de lecturas');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (06, 'hgbarrientos', 'Cuadre', 		01, 03, 'PR_SQUAREUP', 'Pantalla para para registro de cuadre');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (07, 'hgbarrientos', 'Cerrar turno',	01, 04, 'PR_TURN_CLOSE', 'Pantalla para cierre de turno');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (08, 'hgbarrientos', 'Cerrar día',	01, 05, 'PR_DAY_CLOSE', 'Pantalla para cierre de día');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (09, 'hgbarrientos', 'Última lectura',01, 06, 'PR_CHANGELASTREAD', 'Pantalla para modificación de última lectura');

INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (10, 'hgbarrientos', 'Estación', 		02, 01, 'MNT_ESTACION', 'Pantalla para mantenimiento de estación');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (11, 'hgbarrientos', 'Medio pago', 	02, 02, 'MNT_MEDIOSPAGO', 'Pantalla para mantenimiento de medios de pago');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (12, 'hgbarrientos', 'Usuario', 		02, 03, 'MNT_USER', 'Pantalla para mantenimiento de usuarios');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (13, 'hgbarrientos', 'Tasa cambio',	02, 04, 'MNT_CAMBIO', 'Pantalla para mantenimiento de tasa de cambio');

INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (14, 'hgbarrientos', 'Medio pago', 03, 01, 'RPT_MEDIOPAGO', 'Pantalla de generación de reporte de medios de pago');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (15, 'hgbarrientos', 'Volúmenes', 03, 02, 'RPT_VOLUMEN', 'Pantalla de generación de reporte de volumenes.');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (16, 'hgbarrientos', 'MTD', 03, 03, 'RPT_MTD', 'Pantalla de generación de reporte MTD.');
INSERT INTO acceso (acceso_id, creado_por, titulo, padre, orden, recurso_interno, descripcion) VALUES (17, 'hgbarrientos', 'Conf bomba', 02, 05, 'MNT_CONF_BOMBA_ESTACION', 'Pantalla para crear configuraciones de servicio en estaciones');


--Cajero
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (01, 4, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (04, 4, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (05, 4, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (06, 4, 'hgbarrientos');
--Supervisor
    INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (01, 3, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (04, 3, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (05, 3, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (06, 3, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (07, 3, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (08, 3, 'hgbarrientos');
    INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (03, 3, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (16, 3, 'hgbarrientos');
--Administrativo
    INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (01, 2, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (04, 2, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (05, 2, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (06, 2, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (07, 2, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (08, 2, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (09, 2, 'hgbarrientos');
    INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (02, 2, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (10, 2, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (11, 2, 'hgbarrientos');
--INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (12, 2, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (13, 2, 'hgbarrientos');
--Gerente
    INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (01, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (04, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (05, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (06, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (07, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (08, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (09, 5, 'hgbarrientos');
    INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (02, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (10, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (11, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (12, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (13, 5, 'hgbarrientos');
    INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (03, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (14, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (15, 5, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (17, 5, 'hgbarrientos');
--Reportes
    INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (03, 6, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (14, 6, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (15, 6, 'hgbarrientos');
INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) VALUES (16, 6, 'hgbarrientos');



--Usuarios para Ericka Garrido
INSERT INTO usuario (usuario_id, username, clave, nombre, apellido, creado_por) VALUES (171, 'egarrido', 'egarrido', 'Ericka', 'Garrido', 'hgbarrientos');  
INSERT INTO rol_usuario (rol_id, usuario_id, creado_por) VALUES (5, 171, 'hgbarrientos');


UPDATE usuario SET username = 'cajero201117' WHERE usuario_id = 86;
UPDATE usuario SET username = 'supervisor201117' WHERE usuario_id = 142;
UPDATE usuario SET username = 'cajero201308' WHERE usuario_id = 88;
UPDATE usuario SET username = 'supervisor201308' WHERE usuario_id = 144;
UPDATE usuario SET username = 'cajero201309' WHERE usuario_id = 89;
UPDATE usuario SET username = 'supervisor201309' WHERE usuario_id = 145;
UPDATE usuario SET username = 'cajero201118' WHERE usuario_id = 90;
UPDATE usuario SET username = 'supervisor201118' WHERE usuario_id = 146;
UPDATE usuario SET username = 'cajero201693' WHERE usuario_id = 91;
UPDATE usuario SET username = 'supervisor201693' WHERE usuario_id = 147;
UPDATE usuario SET username = 'cajero851303' WHERE usuario_id = 92;
UPDATE usuario SET username = 'supervisor851303' WHERE usuario_id = 148;
UPDATE usuario SET username = 'cajero851112' WHERE usuario_id = 93;
UPDATE usuario SET username = 'supervisor851112' WHERE usuario_id = 149;
UPDATE usuario SET username = 'cajero851185' WHERE usuario_id = 94;
UPDATE usuario SET username = 'supervisor851185' WHERE usuario_id = 150;
UPDATE usuario SET username = 'cajero851302' WHERE usuario_id = 95;
UPDATE usuario SET username = 'supervisor851302' WHERE usuario_id = 151;
UPDATE usuario SET username = 'cajero851253' WHERE usuario_id = 96;
UPDATE usuario SET username = 'supervisor851253' WHERE usuario_id = 152;
UPDATE usuario SET username = 'cajero851301' WHERE usuario_id = 97;
UPDATE usuario SET username = 'supervisor851301' WHERE usuario_id = 153;
UPDATE usuario SET username = 'cajero851300' WHERE usuario_id = 98;
UPDATE usuario SET username = 'supervisor851300' WHERE usuario_id = 154;
UPDATE usuario SET username = 'cajero850203' WHERE usuario_id = 99;
UPDATE usuario SET username = 'supervisor850203' WHERE usuario_id = 155;
UPDATE usuario SET username = 'cajero850204' WHERE usuario_id = 100;
UPDATE usuario SET username = 'supervisor850204' WHERE usuario_id = 156;
UPDATE usuario SET username = 'cajero850205' WHERE usuario_id = 101;
UPDATE usuario SET username = 'supervisor850205' WHERE usuario_id = 157;
UPDATE usuario SET username = 'cajero850206' WHERE usuario_id = 102;
UPDATE usuario SET username = 'supervisor850206' WHERE usuario_id = 158;
UPDATE usuario SET username = 'cajero850207' WHERE usuario_id = 103;
UPDATE usuario SET username = 'supervisor850207' WHERE usuario_id = 159;
UPDATE usuario SET username = 'cajero850208' WHERE usuario_id = 104;
UPDATE usuario SET username = 'supervisor850208' WHERE usuario_id = 160;
UPDATE usuario SET username = 'cajero850209' WHERE usuario_id = 105;
UPDATE usuario SET username = 'supervisor850209' WHERE usuario_id = 161;
UPDATE usuario SET username = 'cajero850210' WHERE usuario_id = 106;
UPDATE usuario SET username = 'supervisor850210' WHERE usuario_id = 162;
UPDATE usuario SET username = 'cajero850211' WHERE usuario_id = 107;
UPDATE usuario SET username = 'supervisor850211' WHERE usuario_id = 163;
UPDATE usuario SET username = 'cajero850212' WHERE usuario_id = 108;
UPDATE usuario SET username = 'supervisor850212' WHERE usuario_id = 164;
UPDATE usuario SET username = 'cajero850213' WHERE usuario_id = 109;
UPDATE usuario SET username = 'supervisor850213' WHERE usuario_id = 165;
UPDATE usuario SET username = 'cajero851513' WHERE usuario_id = 110;
UPDATE usuario SET username = 'supervisor851513' WHERE usuario_id = 166;
UPDATE usuario SET username = 'cajero851511' WHERE usuario_id = 111;
UPDATE usuario SET username = 'supervisor851511' WHERE usuario_id = 167;
UPDATE usuario SET username = 'cajero851512' WHERE usuario_id = 112;
UPDATE usuario SET username = 'supervisor851512' WHERE usuario_id = 168;
UPDATE usuario SET username = 'cajero852944' WHERE usuario_id = 113;
UPDATE usuario SET username = 'supervisor852944' WHERE usuario_id = 169;

/*
delete from estacion_usuario a where estacion_id not in (120,125, 103,113, 131,133, 148,152);
delete from rol_usuario where usuario_id not in (50, 51, 52, 58,114,68,124,75,131,80,136,86,142,88,144,103,159,107,163, 170, 171);
delete from usuario where usuario_id not in (50, 51, 52, 58,114,68,124,75,131,80,136,86,142,88,144,103,159,107,163, 170, 171);
*/

UPDATE ACCESO SET TITULO = 'Tipo de Cambio' WHERE ACCESO_ID = 13;
DELETE FROM ACCESO_ROL WHERE ACCESO_ID = 17;
DELETE FROM ACCESO WHERE ACCESO_ID = 17;
ALTER TABLE PRODUCTO ADD  sku VARCHAR2(13);

Insert into ACCESO (ACCESO_ID,TITULO,PADRE,ORDEN,RECURSO_INTERNO,DESCRIPCION,ESTADO,CREADO_POR,CREADO_EL,MODIFICADO_POR,MODIFICADO_EL) 
values (26,'Empleados',2,11,'MNT_EMPLEADO','Pantalla para mentenimiento de empleado','A','hgbarrientos',to_date('22-AUG-19','DD-MON-RR'),null,null);
Insert into ACCESO_ROL (ACCESO_ID,ROL_ID,CREADO_POR,CREADO_EL) values (26,2,'hgbarrientos',to_date('22-AUG-19','DD-MON-RR'));
ALTER TABLE EMPLEADO ADD  modificado_por VARCHAR2(60);
ALTER TABLE EMPLEADO ADD  modificado_el DATE;
ALTER TABLE EMPLEADO MODIFY empleado_id NUMBER(6);
CREATE SEQUENCE empleado_seq
    START WITH 10
    INCREMENT BY 1
    MAXVALUE 99999
    NOCYCLE
    ORDER;
COMMIT;



/*
CREATE TABLE (
    estado VARCHAR2(1) DEFAULT 'A' NOT NULL,
    creado_por VARCHAR2(40) NOT NULL,
    creado_el DATE DEFAULT SYSDATE NOT NULL,
    modificado_por VARCHAR2(40),
    modificado_el DATE,
    CONSTRAINT _pk PRIMARY KEY (_id),
    CONSTRAINT _fk0 FOREIGN KEY () REFERENCES  (_id),
);
*/

