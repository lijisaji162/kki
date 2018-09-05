CREATE  FUNCTION fn_fibi_award_get_pi_full_name(av_award_number varchar(90)) RETURNS varchar(90) CHARSET utf8
BEGIN
declare ls_full_name varchar(90);

SELECT full_name
	INTO ls_full_name
	FROM award_persons t1
	INNER JOIN award t2 on t1.award_id = t2.award_id
	WHERE t2.award_number = av_award_number
	AND t2.award_sequence_status = 'ACTIVE'
	AND t1.contact_role_code = 'PI';
    
RETURN ls_full_name;
END