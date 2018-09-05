CREATE PROCEDURE get_fibi_award_payment(
IN av_award_id int)
BEGIN
 select t1.award_id,
       t1.award_number ,
       t1.basis_of_payment_code,
       t2.description as payment_basis,
       t1.method_of_payment_code,
       t3.description as payment_method
    from award t1
    left outer join award_basis_of_payment t2 on t1.basis_of_payment_code = t2.basis_of_payment_code
    left outer join award_method_of_payment t3 on t1.method_of_payment_code = t3.method_of_payment_code
    where t1.award_id=av_award_id;
END