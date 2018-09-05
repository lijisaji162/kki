CREATE PROCEDURE get_fibi_award_paymnt_schedule(
IN av_award_id int)
BEGIN
select 
        t1.award_id, 
        t1.award_number,
        t1.award_report_term_desc as schedule_details,
        t1.due_date,
        t1.submitted_by_person_id,
        concat(t4.last_nm,", ",t4.first_nm," ",t4.middle_nm)  as full_name,
        t1.report_status_code,
        t2.description as status,
        t1.status_description as comments,
        t1.amount
    from award_payment_schedule t1
    left outer join report_status t2 on t1.report_status_code = t2.report_status_code
    left outer join krim_prncpl_t t3 on t1.submitted_by_person_id = t3.prncpl_id
     left outer join krim_entity_nm_t t4 on t3.entity_id = t4.entity_id
    where t1.award_id = av_award_id;
END