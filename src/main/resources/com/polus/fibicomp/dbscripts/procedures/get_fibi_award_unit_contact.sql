CREATE  PROCEDURE get_fibi_award_unit_contact(
IN av_award_id int)
BEGIN
select 
    t1.award_id,
    t1.unit_administrator_type_code,
    t2.description as unit_administrator_type,
    t1.person_id,
    t1.full_name,
    t4.email_addr,
    t5.phone_nbr
    from award_unit_contacts t1
    left outer join unit_administrator_type t2 on t1.unit_administrator_type_code = t2.unit_administrator_type_code
    left outer join krim_prncpl_t t3 on t1.person_id = t3.prncpl_id
    left outer join krim_entity_email_t t4 on t3.entity_id = t4.entity_id
    left outer join krim_entity_phone_t t5 on t3.entity_id = t5.entity_id  and t5.dflt_ind = 'Y'
    where t1.award_id = av_award_id;
END