CREATE PROCEDURE get_fibi_award_spnsor_contact(
IN av_award_id int)
BEGIN
select 
       t1.award_id,
       t1.award_number,
       t1.full_name,
       t2.address_line_1,
       t2.address_line_2,
       t2.address_line_3,
       t2.email_address,
       t2.city,
       t2.postal_code,
       t1.contact_role_code,
       t3.description as contact_type
       from award_sponsor_contacts t1
       inner join rolodex t2 on t1.rolodex_id = t2.rolodex_id
       left outer join contact_type t3 on t1.contact_role_code=t3.contact_type_code
       where t1.award_id = av_award_id;
END