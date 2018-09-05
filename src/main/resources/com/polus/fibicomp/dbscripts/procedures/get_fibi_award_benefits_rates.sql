CREATE PROCEDURE get_fibi_award_benefits_rates(
IN av_award_id int)
BEGIN

select 
        t1.award_id,
        t1.award_number,
        t1.special_eb_rate_off_campus as off_campus,
        t1.special_eb_rate_on_campus as on_campus,
        t2.comments
    from award t1
    inner join award_comment t2 on t1.award_id = t2.award_id and t2.comment_type_code = '20'
    where t1.award_id = av_award_id;
END