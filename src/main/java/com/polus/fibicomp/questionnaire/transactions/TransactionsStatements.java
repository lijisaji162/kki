package com.polus.fibicomp.questionnaire.transactions;

public class TransactionsStatements {

	public static final String GET_QUESTIONNAIRE_DATA = "select * from OSP$QUESTIONNAIRE_QUESTIONS  WHERE QUESTIONNAIRE_ID = <<QUESTIONNAIRE_ID>> AND QUESTION_ID = <<QUESTION_ID>>";

	public static final String GET_QUESTIONNAIRE_DATA_MYSQL = "select * from QUESTIONNAIRE_QUESTIONS  WHERE QUESTIONNAIRE_REF_ID_FK = <<QUESTIONNAIRE_ID>> AND QUESTION_REF_ID_FK = <<QUESTION_ID>>";

	public static final String GET_QUESTIONNAIRE_ANSWER = " SELECT t1.questionnaire_answer_id, t1.question_id,  t1.answer_number, t1.answer,  t1.answer_lookup_code,  t1.explanation ,  T3.Questionnaire_Answer_Att_Id AS Attachment_id, T2.Questionnaire_Ans_Header_Id "+
														  " FROM  mitkc_questionnaire_answer t1 INNER JOIN mitkc_questionnaire_ans_header t2 ON t1.questionnaire_ans_header_id = t2.questionnaire_ans_header_id "+
														  " Left Outer JOIN mitkc_questionnaire_answer_att t3 ON T3.Questionnaire_Answer_Id = T1.Questionnaire_Answer_Id "+
														  " ORDER BY t1.questionnaire_answer_id,t1.question_id ";
	
	
	public static final String GET_QUESTIONNAIRE_QUESTION = " SELECT question_id, question, description, help_link, answer_type, answer_length, no_of_answers, lookup_type, lookup_name, lookup_field, group_name, group_label,"+
															" has_condition   FROM   mitkc_questionnaire_question  WHERE  questionnaire_id = <<AV_QUESTIONNAIRE_ID>>    ORDER  BY sort_order ";
	
	
	public static final String GET_QUESTIONNAIRE_QUESTION_ANSWER =
																	" SELECT t1.questionnaire_answer_id, "+
																	" t1.question_id, "+
																	" t1.answer_number, "+
																	" t1.answer, "+
																	" t1.answer_lookup_code, "+
																	" t1.explanation ,"+
																	" T3.Questionnaire_Answer_Att_Id AS Attachment_id,"+
																	" T2.Questionnaire_Ans_Header_Id"+
																	" FROM  mitkc_questionnaire_answer t1 "+
																	" INNER JOIN mitkc_questionnaire_ans_header t2 ON t1.questionnaire_ans_header_id = t2.questionnaire_ans_header_id"+
																	" Left Outer JOIN mitkc_questionnaire_answer_att t3 ON T3.Questionnaire_Answer_Id=T1.Questionnaire_Answer_Id"+
																	" WHERE t2.module_item_id = <<AV_MODULE_ITEM_ID>> "+
																	" and T2.Module_Item_Code = <<AV_MODULE_CODE>>" +
																	" ORDER BY t1.questionnaire_answer_id,t1.question_id";

	public static final String GET_QUESTIONNAIRE_QUESTION_OPTIONS =		" SELECT t1.question_option_id, "+ 
																		" t1.question_id,  "+
																		" t1.option_number,  "+
																		" t1.option_label,  "+
																		" t1.require_explanation, "+ 
																		" t1.explantion_label  "+
																	    " FROM   mitkc_qnr_question_option t1  "+
																		" INNER  JOIN mitkc_questionnaire_question t2 ON t1.question_id = t2.question_id "+ 
																		" WHERE  t2.questionnaire_id = <<AV_QUESTIONNAIRE_ID>> "+
																		" ORDER  BY t1.question_option_id,t1.question_id,t1.option_number ";
	
	
	
	public static final String GET_QUESTIONNAIRE_QUESTION_CONDITIONS =  " SELECT t1.question_condition_id, "+ 
																		" t1.question_id,  "+
																		" t1.condition_type,  "+
																		" t1.condition_value,  "+
																		" t1.group_name  "+
																		" FROM  mitkc_qnr_question_condition t1  "+
																		" INNER JOIN mitkc_questionnaire_question t2 ON t1.question_id = t2.question_id  "+
																		" WHERE t2.questionnaire_id = <<AV_QUESTIONNAIRE_ID>> "+
																		" ORDER BY to_number(substr(t1.group_name,2))";

	
	
	
	public static final String GET_MAX_QUESTIONNAIRE_ANS_ID =  " select max(questionnaire_answer_id) as questionnaire_answer_id  from mitkc_questionnaire_answer ";
	
	
	public static final String INSERT_QUESTIONNAIRE_ANSWER = 	" INSERT INTO mitkc_questionnaire_answer "+
																" (questionnaire_answer_id,  "+
																"  questionnaire_ans_header_id,  "+
																"  question_id,  "+
																" answer_number,  "+
																" answer,  "+
																" answer_lookup_code,  "+
																" explanation,  "+
																" update_timestamp,  "+
																" update_user)  "+
																"  VALUES   ( "+
																"  <<QUESTIONNAIRE_ANSWER_ID>>, "+ 
																" <<QUESTIONNAIRE_ANS_HEADER_ID>>,  "+
																"  <<QUESTION_ID>>,  "+
																"  <<ANSWER_NUMBER>>,  "+
																"  <<ANSWER>>,  "+
																"  <<ANSWER_LOOKUP_CODE>>,  "+
																"  <<EXPLANATION>>,  "+
																"  <<UPDATE_TIMESTAMP>>,  "+
																"  <<UPDATE_USER>>)  ";

	
	public static final String DELETE_QUESTIONNAIRE_ANSWER = " delete from mitkc_questionnaire_answer where questionnaire_answer_id = <<QUESTIONNAIRE_ANSWER_ID>>";

	
	public static final String UPDATE_QUESTIONNAIRE_ANSWER = 	" UPDATE  mitkc_questionnaire_answer "+
																"  SET  questionnaire_ans_header_id = <<QUESTIONNAIRE_ANS_HEADER_ID>>,  "+
																"       question_id = <<QUESTION_ID>>,  "+
																"       answer_number = <<ANSWER_NUMBER>>,  "+
																"       answer = <<ANSWER>>,  "+
																"       answer_lookup_code = <<ANSWER_LOOKUP_CODE>>,  "+
																"       explanation = <<EXPLANATION>>, "+ 
																"       update_timestamp = <<UPDATE_TIMESTAMP>>,  "+
																"       update_user = <<UPDATE_USER>>  "+
																" WHERE questionnaire_answer_id = <<QUESTIONNAIRE_ANSWER_ID>> ";
	
	public static final String UPDATE_QUESTIONNAIRE_COMPLETE_FLAG = " UPDATE mitkc_questionnaire_ans_header SET questionnaire_completed_flag = <<QUESTIONNAIRE_COMPLETED_FLAG>> " +
																	" WHERE questionnaire_ans_header_id = <<QUESTIONNAIRE_ANS_HEADER_ID>> "; 
	
	
	public static final String GET_QUESTIONNAIRE_ANS_HEADER_ID  =   " SELECT questionnaire_ans_header_id  FROM   mitkc_questionnaire_ans_header "+
																	" WHERE  module_item_id = <<MODULE_ITEM_ID>> "+
																	" AND module_item_code = <<MODULE_ITEM_CODE>>" +
																	" AND questionnaire_id = <<QUESTIONNAIRE_ID>> ";

	public static final String INSERT_QUESTIONNAIRE_ANS_HEADER  = 	" INSERT INTO mitkc_questionnaire_ans_header "+
																	" (questionnaire_ans_header_id, "+
																	" questionnaire_id,"+
																	" module_item_code,"+
																	" module_item_id,"+
																	" questionnaire_completed_flag,"+
																	" update_timestamp,"+
																	" update_user) "+
																	" VALUES (<<QUESTIONNAIRE_ANS_HEADER_ID>>,"+
																	" <<QUESTIONNAIRE_ID>>,"+
																	" <<MODULE_ITEM_CODE>>,"+
																	" <<MODULE_ITEM_ID>>,"+
																	" <<QUESTIONNAIRE_COMPLETED_FLAG>>,"+
																	" <<UPDATE_TIMESTAMP>>,"+
																	" <<UPDATE_USER>>)";
																	
	
	public static final String GET_MAX_QUESTIONNAIRE_ANS_HEADER_ID =  " select max(questionnaire_ans_header_id) as questionnaire_ans_header_id  from mitkc_questionnaire_ans_header ";
	
	public static final String GET_APPLICABLE_QUESTIONNAIRE = 		" select t2.questionnaire_id,"
																	+ "t2.questionnaire,"
																	+ "t3.questionnaire_ans_header_id,"
																	+ "t3.questionnaire_completed_flag,"
																	+ "t1.rule_id from mitkc_questionnaire_usage t1 "
																	+ "inner join mitkc_questionnaire t2 on t1.QUESTIONNAIRE_ID = t2.questionnaire_id "
																	+ "left outer join mitkc_questionnaire_ans_header t3 on t1.QUESTIONNAIRE_ID = t3.questionnaire_id and t1.MODULE_ITEM_CODE = t3.module_item_code and t3.module_item_id = <<MODULE_ITEM_ID>> "
														                                                
														           + "where t1.module_item_code = <<MODULE_ITEM_CODE>> "
														           + "and   t1.MODULE_SUB_ITEM_CODE = <<MODULE_SUB_ITEM_CODE>> ";
														
	
	
}


