CREATE PROCEDURE get_fibi_award_special_review(
IN av_award_id int)
BEGIN
select 
    t1.award_id,
    t1.special_review_code,
    t2.description as special_review_type,
    t1.approval_type_code,
    t3.description,
    '' as approval_status,
    t1.protocol_number,
    date_format(t1.application_date,'%m/%d/%Y') as application_date,
    date_format(t1.approval_date,'%m/%d/%Y') as approval_date,
    date_format(t1.expiration_date,'%m/%d/%Y') as expiration_date
    from award_special_review t1
    left outer join special_review t2 on t1.special_review_code = t2.special_review_code
    left outer join sp_rev_approval_type t3 on t1.approval_type_code = t3.approval_type_code
    where t1.award_id = av_award_id;
END