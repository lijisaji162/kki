CREATE  PROCEDURE get_fibi_award_terms(
IN av_award_id int)
BEGIN
select  t1.award_id,
           t1.award_number,
           t2.sponsor_term_type_code,
           t3.description as sponsor_term_type,
           t2.description as sponsor_term
    from award_sponsor_term t1
    inner join sponsor_term t2 on t1.sponsor_term_id = t2.sponsor_term_id
    inner join sponsor_term_type t3 on t2.sponsor_term_type_code = t3.sponsor_term_type_code
    where t1.award_id = av_award_id;
END