--3754;1328992;1329278
select * from ssf_addin_shifts_data WHERE period_start_date like '201705%' AND period_type = 'S';

select * from ssf_pump_sales WHERE end_date like '201706%' AND sale_id >= 1328992 AND sale_id <= 1329278;

select DISTINCT ppu, grade_id, level from ssf_pump_sales WHERE end_date like '201706%' AND sale_id >= 1328992 AND sale_id <= 1329278;

