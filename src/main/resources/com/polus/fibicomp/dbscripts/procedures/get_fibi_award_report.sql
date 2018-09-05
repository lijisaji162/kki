CREATE PROCEDURE get_fibi_award_report(
IN av_award_id int)
BEGIN
select 
        t1.award_id,
        t1.award_number,
        t1.report_class_code,
        t2.description as report_class,
        t1.report_code,
        t3.description as report,
        t1.frequency_code,
        t4.description as frequency,
        t1.frequency_base_code,
        t5.description as frequency_base,
        t1.osp_distribution_code,
        t6.description as osp_distribution,
        t1.due_date
    from award_report_terms t1
    inner join report_class t2 on t1.report_class_code = t2.report_class_code
    inner join report t3 on t1.report_code = t3.report_code -- and t3.final_report_flag = 'Y'
    inner join frequency t4 on t1.frequency_code = t4.frequency_code
    inner join frequency_base t5 on t1.frequency_base_code = t5.frequency_base_code
    inner join distribution t6 on t1.osp_distribution_code = t6.osp_distribution_code
    where t1.award_id = av_award_id and t1.report_class_code not in(select k.val from krcr_parm_t k where k.parm_nm='reportClassForPaymentsAndInvoices');
END