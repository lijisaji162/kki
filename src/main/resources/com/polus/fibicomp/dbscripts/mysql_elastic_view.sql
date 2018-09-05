
  CREATE OR REPLACE VIEW ELASTIC_AWARD_V (DOCUMENT_NUMBER, AWARD_NUMBER, AWARD_ID, TITLE, ACCOUNT_NUMBER, STATUS_CODE, STATUS, SPONSOR, SPONSOR_CODE, LEAD_UNIT_NUMBER, LEAD_UNIT_NAME, PI_NAME, PERSON_ID, OBLIGATION_EXPIRATION_DATE) AS 
  select 	
		award.document_number,
		award.AWARD_NUMBER as AWARD_NUMBER,
		award.AWARD_ID as AWARD_ID,
		award.title as title,
		award.account_number AS ACCOUNT_NUMBER,
        award.status_code as status_code,
		award_status.description AS STATUS,
		sponsor.sponsor_name AS SPONSOR,
		award.sponsor_code AS SPONSOR_CODE,
		unit.UNIT_NUMBER AS lead_unit_number,
		unit.UNIT_NAME AS lead_unit_name,
		award_persons.full_name as pi_name,
        award_persons.PERSON_ID,
        awi.obligation_expiration_date
		FROM award  
        left outer join award_persons on award.award_id = award_persons.award_id and award_persons.contact_role_code = 'PI'
        inner join award_status on  award.status_code = award_status.status_code
        left outer join sponsor on sponsor.sponsor_code = award.sponsor_code
        left outer join unit on award.LEAD_UNIT_NUMBER = unit.UNIT_NUMBER 
        left outer join award_amount_info awi ON award.award_id = awi.award_id 
		where award.award_sequence_status = 'ACTIVE'
        and awi.award_amount_info_id in ( select max(award_amount_info_id) 
                                          from award_amount_info 
                                          where award_id = award.award_id );
										
CREATE OR REPLACE VIEW ELASTIC_PROPOSAL_V (DOCUMENT_NUMBER, PROPOSAL_NUMBER, VER_NBR, TITLE, LEAD_UNIT_NUMBER, LEAD_UNIT, SPONSOR_CODE, SPONSOR, DEADLINE_DATE, STATUS_CODE, STATUS, UPDATE_TIMESTAMP, UPDATE_USER, PERSON_ID, FULL_NAME) AS 
  select 
	eps_proposal.DOCUMENT_NUMBER,
	eps_proposal.PROPOSAL_NUMBER,
	eps_proposal.VER_NBR,
	eps_proposal.TITLE,
	eps_proposal.OWNED_BY_UNIT as LEAD_UNIT_NUMBER, 
	unit.UNIT_NAME as LEAD_UNIT, 
	eps_proposal.SPONSOR_CODE,
	SPONSOR.SPONSOR_NAME AS SPONSOR,
	eps_proposal.DEADLINE_DATE,
	eps_proposal.STATUS_CODE,
	EPS_PROPOSAL_STATUS.DESCRIPTION AS STATUS,
	eps_proposal.UPDATE_TIMESTAMP,
	eps_proposal.UPDATE_USER,
	t1.person_id,
	t1.full_name 
FROM eps_proposal 
inner join unit on unit.UNIT_NUMBER = eps_proposal.OWNED_BY_UNIT
left outer join SPONSOR on sponsor.sponsor_code = eps_proposal.sponsor_code
inner join EPS_PROPOSAL_STATUS on  EPS_PROPOSAL_STATUS.STATUS_CODE = eps_proposal.STATUS_CODE
left outer join eps_prop_person t1 on eps_proposal.PROPOSAL_NUMBER=t1.PROPOSAL_NUMBER
and t1.PROP_PERSON_ROLE_ID = 'PI';

CREATE OR REPLACE VIEW ELASTIC_IRB_DATA_V (DOCUMENT_NUMBER, PROTOCOL_ID, PROTOCOL_NUMBER, TITLE, UNIT_NUMBER, LEAD_UNIT, PROTOCOL_TYPE_CODE, PROTOCOL_TYPE, STATUS_CODE, STATUS, PERSON_NAME, PERSON_ID) AS 
  select 
                       t1.DOCUMENT_NUMBER,
					   t1.PROTOCOL_ID,
					   t1.PROTOCOL_NUMBER,
					   t1.TITLE,
                       t4.unit_number,
					   t5.unit_name AS LEAD_UNIT,
                       t1.PROTOCOL_TYPE_CODE,
					   t2.DESCRIPTION AS PROTOCOL_TYPE,
                       t1.PROTOCOL_STATUS_CODE AS STATUS_CODE,
					   t3.DESCRIPTION as STATUS,
                       T6.full_name as Person_Name,
                       t6.PERSON_ID
					   from protocol t1
					   inner join protocol_type t2 on t1.PROTOCOL_TYPE_CODE = t2.PROTOCOL_TYPE_CODE
					   inner join protocol_status t3 on t1.PROTOCOL_STATUS_CODE = t3.PROTOCOL_STATUS_CODE
					   left outer join protocol_units t4 on t1.PROTOCOL_NUMBER = t4.PROTOCOL_NUMBER 
														and t1.sequence_number = t4.sequence_number
														and t4.lead_unit_flag = 'Y'
					   left outer join unit t5 on t4.unit_number = t5.unit_number
                       left outer join Protocol_Persons t6 on T1.Protocol_Id = T6.Protocol_Id 
                       and t6.PROTOCOL_PERSON_ROLE_ID = 'PI';
					   
					   
CREATE OR REPLACE VIEW ELASTIC_IACUC_DATA_V (DOCUMENT_NUMBER, PROTOCOL_ID, PROTOCOL_NUMBER, TITLE, LEAD_UNIT_NUMBER, LEAD_UNIT, PROTOCOL_TYPE_CODE, PROTOCOL_TYPE, STATUS_CODE, STATUS, PERSON_ID, PERSON_NAME) AS 
  select            
                   t1.DOCUMENT_NUMBER, 
                   t1.protocol_id,
				   t1.PROTOCOL_NUMBER,
				   t1.TITLE,
                   t4.unit_number AS LEAD_UNIT_NUMBER,
				   t5.unit_name AS LEAD_UNIT,
                   t1.PROTOCOL_TYPE_CODE,
				   t2.DESCRIPTION AS PROTOCOL_TYPE,
                   t1.PROTOCOL_STATUS_CODE AS STATUS_CODE,
				   t3.DESCRIPTION as STATUS,
                   t6.PERSON_ID,
                   T6.full_name as Person_Name
				   from iacuc_protocol t1
				   inner join iacuc_protocol_type t2 on t1.PROTOCOL_TYPE_CODE = t2.PROTOCOL_TYPE_CODE
				   inner join iacuc_protocol_status t3 on t1.PROTOCOL_STATUS_CODE = t3.PROTOCOL_STATUS_CODE
				   left outer join IACUC_PROTOCOL_UNITS t4 on t1.PROTOCOL_NUMBER = t4.PROTOCOL_NUMBER 
													and t1.sequence_number = t4.sequence_number
													and t4.lead_unit_flag = 'Y'
				   left outer join unit t5 on t4.unit_number = t5.unit_number
                   left outer join IACUC_PROTOCOL_PERSONS t6 on T1.Protocol_Id=T6.Protocol_Id;
				   
  CREATE OR REPLACE VIEW ELASTIC_COI_DISCLOSURE_DATA_V (DOCUMENT_NUMBER, COI_DISCLOSURE_ID, COI_DISCLOSURE_NUMBER, PERSON_ID, FULL_NAME, DISCLOSURE_DISPOSITION_CODE, DISCLOSURE_DISPOSITION, DISCLOSURE_STATUS_CODE, DISCLOSURE_STATUS, MODULE_ITEM_KEY, EXPIRATION_DATE) AS 
  select     
           t1.DOCUMENT_NUMBER,
           t1.COI_DISCLOSURE_ID,
           t1.COI_DISCLOSURE_NUMBER,
           t1.PERSON_ID,
           concat(t5.last_nm,', ',t5.first_nm,' ',t5.middle_nm) AS FULL_NAME,
           t1.DISCLOSURE_DISPOSITION_CODE,
           t3.DESCRIPTION AS DISCLOSURE_DISPOSITION,
           t1.DISCLOSURE_STATUS_CODE,
           t2.DESCRIPTION AS DISCLOSURE_STATUS,
           t1.MODULE_ITEM_KEY,
           t1.EXPIRATION_DATE
           from COI_DISCLOSURE t1 
           inner join COI_DISCLOSURE_STATUS t2 on t2.COI_DISCLOSURE_STATUS_CODE = t1.DISCLOSURE_STATUS_CODE 
           inner join COI_DISPOSITION_STATUS t3 on t3.COI_DISPOSITION_CODE = t1.DISCLOSURE_DISPOSITION_CODE
           left outer join krim_prncpl_t t4 on t4.prncpl_id = t1.PERSON_ID 
           left outer join krim_entity_nm_t t5 on t4.entity_id = t5.entity_id;