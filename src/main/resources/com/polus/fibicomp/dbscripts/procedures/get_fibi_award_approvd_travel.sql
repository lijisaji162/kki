CREATE PROCEDURE get_fibi_award_approvd_travel(
IN av_award_id int)
BEGIN

select  
    t1.award_id,
    t1.award_number,
    t1.traveler_name,
    t1.destination,
    t1.start_date,
    t1.end_date,
    t1.amount
    from award_approved_foreign_travel t1
    where award_id = av_award_id;
END