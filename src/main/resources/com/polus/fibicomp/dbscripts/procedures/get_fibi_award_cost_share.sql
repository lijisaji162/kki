CREATE PROCEDURE get_fibi_award_cost_share(
IN av_award_id int)
BEGIN
select 
        t1.award_id,
        t1.award_number,
        t1.cost_share_percentage,
        t1.cost_share_type_code,
        t2.description as cost_share_type,
        t1.project_period,
        t1.commitment_amount
    from award_cost_share t1
    inner join cost_share_type t2 on t1.cost_share_type_code = t2.cost_share_type_code
    where t1.award_id = av_award_id;

END