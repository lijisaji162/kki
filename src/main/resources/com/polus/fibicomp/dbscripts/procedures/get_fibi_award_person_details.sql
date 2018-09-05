CREATE PROCEDURE get_fibi_award_person_details(
IN av_award_id int)
BEGIN
select 
      t1.award_id,
      t1.award_number,
      t2.person_id,
      t2.full_name,
      t2.contact_role_code,
      t3.unit_number,
      t4.unit_name,
      t5.prncpl_nm
      from award t1
      left outer join award_persons t2 on t1.award_id = t2.award_id
      left outer join award_person_units t3 on t2.award_person_id = t3.award_person_id 
      left outer join unit t4 on t3.unit_number = t4.unit_number
      left outer join krim_prncpl_t t5 on t2.person_id = T5.Prncpl_Id
      where t1.award_id = av_award_id;
END