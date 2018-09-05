CREATE PROCEDURE get_fibi_award_fa_rates(
IN av_award_id int)
BEGIN

select 
    t1.award_id,
    t1.award_number,
    t1.applicable_idc_rate,
    t1.idc_rate_type_code,
    t2.description as idc_rate_type,
    t1.fiscal_year,
    t1.start_date,
	case when t1.on_campus_flag = 'N' then 'on'
		  when t1.on_campus_flag = 'F' then 'off'
	end as campus
from award_idc_rate t1 
inner join idc_rate_type t2 on t1.idc_rate_type_code = t2.idc_rate_type_code
where award_id = av_award_id;

END