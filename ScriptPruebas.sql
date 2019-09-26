/*
delete from lectura_detalle;
delete from lectura;
delete from precio;
delete from turno;
delete from horario;
commit;
*/

/*
delete from factelectronica_pos;
delete from efectivo;
delete from arqueocaja_detalle;
delete from arqueocaja_producto;
delete from arqueocaja;
commit;
*/

SELECT * FROM turno t WHERE t.hora_fin = 500 AND TRUNC(creado_el) = TO_DATE('22/05/2017', 'dd/mm/yyyy') AND estado_id = 1;

select rowid, a.* from horario a order by horario_id desc;

select rowid, a.* from turno a order by turno_id desc;

select rowid, a.* from precio a order by turno_id desc, tipodespacho_id, producto_id;

select rowid, a.* from lectura a order by lectura_id desc;

select rowid, a.* from lectura_detalle a order by lectura_id desc, bomba_id, tipo, producto_id;

select * from arqueocaja;

select * from arqueocaja_detalle;

select * from arqueocaja_producto;

select * from efectivo;

select * from factelectronica_pos;

select rowid, a.* from mediopago a;

select * from producto where tipo_id =1 order by orden_pos;

select ld.* from lectura_detalle ld, lectura la WHERE la.lectura_id = ld.lectura_id AND ;

select rowid, a.* from estacion a;
select rowid, a.* from estacion_producto a;

select rowid, a.* from estacion_conf a where estacion_id = 102 order by bomba_id, hora_fin;

select rowid, a.* from lectura_detalle a where bomba_id = 4;

/*
Cuadre de bombas por tipo
*/
SELECT pro.nombre, td.nombre, SUM(ld.total), SUM(ld.total*p.precio), pro.producto_id, ld.tipodespacho_id 
FROM turno t, precio p, lectura l, lectura_detalle ld, producto pro, tipodespacho td, estacion_conf ec 
WHERE p.turno_id = t.turno_id 
AND p.tipodespacho_id = ld.tipodespacho_id 
AND p.producto_id = ld.producto_id 
AND l.turno_id = t.turno_id AND l.lectura_id = ld.lectura_id 
AND pro.producto_id = p.producto_id AND td.tipodespacho_id = p.tipodespacho_id 
AND ld.bomba_id = ec.bomba_id AND ec.estacion_id = t.estacion_id AND ec.hora_fin = t.hora_fin 
                    AND ld.tipo = 'M' AND t.turno_id IN (1000) AND ld.bomba_id IN (1) 
GROUP BY td.nombre, pro.nombre, pro.producto_id, ld.tipodespacho_id 
ORDER BY td.nombre, pro.producto_id;
                    


SELECT pro.nombre, td.nombre, ld.total, p.precio, ld.total*p.precio 
FROM turno t, precio p, lectura l, lectura_detalle ld, producto pro, tipodespacho td  
WHERE p.turno_id = t.turno_id AND t.turno_id = 1000
AND p.tipodespacho_id = 1
AND ld.tipo = 'M' 
AND p.producto_id = ld.producto_id    
AND l.turno_id = t.turno_id AND l.lectura_id = ld.lectura_id
AND pro.producto_id = p.producto_id
AND TD.TIPODESPACHO_ID = p.tipodespacho_id


UNION
SELECT pro.nombre, td.nombre, ld.total, p.precio, ld.total*p.precio 
FROM turno t, precio p, lectura l, lectura_detalle ld, producto pro, tipodespacho td  
WHERE p.turno_id = t.turno_id AND t.turno_id = 70
AND p.tipodespacho_id = 2
AND ld.tipo = 'M' 
AND p.producto_id = ld.producto_id    
AND l.turno_id = t.turno_id AND l.lectura_id = ld.lectura_id
AND pro.producto_id = p.producto_id
AND TD.TIPODESPACHO_ID = p.tipodespacho_id
order by 2;


