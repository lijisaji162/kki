CREATE PROCEDURE get_fibi_award_funded_proposal(
IN av_award_id int)
BEGIN
select 
       t1.award_id,
       t1.award_number,
       t3.proposal_number,
       t4.full_name as pi,
       t3.lead_unit_number,
       t5.unit_name,
       t3.sponsor_code,
       t6.sponsor_name,
       date_format(t3.requested_start_date_initial,'%m/%d/%Y') as start_date,
       date_format(t3.requested_end_date_initial,'%m/%d/%Y') as end_date,
       t3.total_direct_cost_total
    from award t1
    inner join award_funding_proposals t2 on t1.award_id = t2.award_id
    inner join proposal t3 on t2.proposal_id = t3.proposal_id
    left outer join proposal_persons t4 on t3.proposal_id = t4.proposal_id and t4.contact_role_code = 'PI'
    inner join unit t5 on t3.lead_unit_number = t5.unit_number
    inner join sponsor t6 on t3.sponsor_code = t6.sponsor_code
    where t1.award_id = av_award_id;
END