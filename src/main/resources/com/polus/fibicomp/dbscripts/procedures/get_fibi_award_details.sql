CREATE PROCEDURE get_fibi_award_details(
IN av_award_id int)
BEGIN

DECLARE ls_award_number varchar(12);
DECLARE ls_root_award_number varchar(12);

DECLARE li_count int;
DECLARE ls_awd_budget_exist varchar(1);
DECLARE li_latest_ver_nbr int;

select award_number into ls_award_number 
from award 
where award_id = av_award_id;

select root_award_number into  ls_root_award_number 
from AWARD_HIERARCHY 
where award_number = ls_award_number;

  
	/*select count(1) 
	into li_count
	from MITKC_AB_HEADER
	where award_id = av_award_id;

	if li_count > 0 then
		ls_awd_budget_exist := 'T';
	else
		ls_awd_budget_exist := 'F';
	end if;

	select max(version_number)
	into li_latest_ver_nbr
	from mitkc_ab_header 
	where award_id = av_award_id;
    */

         select 
			  t1.award_id,
              t1.award_number,
              t1.account_number,
              t1.document_number,
              t1.activity_type_code,
              t6.description as activity_type,
              t1.award_type_code,
              t7.description as award_type,
              t1.account_type_code,
              t8.description as account_type,
              t1.sponsor_award_number,
              t1.title,
              date_format(t1.award_effective_date,'%m/%d/%Y') as award_effective_date,
              date_format(t5.current_fund_effective_date,'%m/%d/%Y') as obligation_start,
              date_format(t5.obligation_expiration_date,'%m/%d/%Y') obligation_end,
              date_format(t1.notice_date,'%m/%d/%Y') as notice_date,
              concat('$',case 
              when t5.obli_distributable_amount is null then 0.00
			  when t5.obli_distributable_amount = 0 then 0.00
              else
				t5.obli_distributable_amount
			  end)as obligated_amount,
              
              concat('$',case 
				when t5.anticipated_total_amount is null then 0.00
                when t5.anticipated_total_amount = 0 then 0.00
			  else
				t5.anticipated_total_amount
              end) as anticipated_amount,
              
              t1.lead_unit_number,
              t3.unit_name as lead_unit_name,
              t1.sponsor_code,
              t2.sponsor_name,
              aws.description as award_status,
              date_format(t1.update_timestamp,'%m/%d/%Y') as last_update,
              ls_root_award_number as root_award_number,
              t9.person_id,
              t9.full_name,
              t10.prncpl_nm
            from award t1
            inner join award_status aws on t1.status_code = aws.status_code	
            left outer join sponsor t2 on t1.sponsor_code = t2.sponsor_code
            left outer join unit t3 on t1.lead_unit_number = t3.unit_number        
            left outer join award_amount_info t5 on t1.award_id = t5.award_id
            left outer join activity_type t6 on t1.activity_type_code = t6.activity_type_code
            left outer join award_type t7 on t1.award_type_code = t7.award_type_code
            left outer join account_type t8 on t1.account_type_code = t8.account_type_code		
            left outer join award_persons t9 on t1.award_id = t9.award_id and t9.contact_role_code='PI'
            left outer join krim_prncpl_t t10 on t9.person_id = t10.Prncpl_Id
            where t1.award_sequence_status = 'ACTIVE' 
            and   t5.award_amount_info_id in ( select max(award_amount_info_id) from award_amount_info where award_id = t1.award_id )
            and   t1.award_id = av_award_id;
END