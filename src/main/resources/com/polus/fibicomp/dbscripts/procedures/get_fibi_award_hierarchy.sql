CREATE PROCEDURE get_fibi_award_hierarchy(IN av_award_number varchar(12))
BEGIN


 select 
 t2.award_id,
 t1.root_award_number,
 t1.award_number,
 t1.lvl as level,
 t1.parent_award_number,
 t1.pi_name,
 t2.account_number, 
 t2.status_code 
 from (
	 select 
	 t.root_award_number,
	 t.award_number,
	 fn_fibi_get_hierarchy_level(t.award_number) as lvl,
	 t.parent_award_number,
	 fn_fibi_award_get_pi_full_name(t.award_number) as pi_name
	 from award_hierarchy t
	 where t.ROOT_AWARD_NUMBER =av_award_number
 )t1
 inner join award t2 on t1.award_number = t2.award_number
 where upper(t2.award_sequence_status) = 'ACTIVE'
 order by  t1.lvl,t1.parent_award_number,t1.award_number;
 
END