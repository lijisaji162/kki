CREATE  PROCEDURE refresh_award_mv(
)
BEGIN

TRUNCATE TABLE AWARD_MV;
	

INSERT INTO AWARD_MV
(
DOCUMENT_NUMBER,
AWARD_NUMBER,
AWARD_ID,
TITLE,
ACCOUNT_NUMBER,
SEQUENCE_NUMBER,
STATUS,
SPONSOR,
SPONSOR_CODE,
UNIT_NUMBER,
UNIT_NAME,
full_name,
PERSON_ID,
UPDATE_TIMESTAMP,
UPDATE_USER
)
select 
award.document_number as DOCUMENT_NUMBER,
award.AWARD_NUMBER as AWARD_NUMBER,
award.AWARD_ID as AWARD_ID,
award.title as TITLE,
award.account_number AS ACCOUNT_NUMBER,
award.sequence_number AS SEQUENCE_NUMBER,
award_status.description AS STATUS,
sponsor.sponsor_name AS SPONSOR,
award.sponsor_code AS SPONSOR_CODE,
unit.UNIT_NUMBER AS UNIT_NUMBER,
unit.UNIT_NAME AS UNIT_NAME,
award_persons.full_name,
award_persons.PERSON_ID,
award.update_timestamp AS UPDATE_TIMESTAMP,
award.update_user AS UPDATE_USER 		
FROM award  
left outer join award_persons on award.award_id = award_persons.award_id 
inner join award_status on  award.status_code = award_status.status_code
left outer join sponsor on sponsor.sponsor_code = award.sponsor_code
left outer join unit on award.LEAD_UNIT_NUMBER = unit.UNIT_NUMBER
where award.AWARD_SEQUENCE_STATUS = 'ACTIVE';
COMMIT;
END