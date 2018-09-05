----------------DEVPROPOSAL----------------------------------
INSERT INTO MITKC_ELASTIC_INDEX(
MODULE_CODE,
MODULE_ITEM_KEY,
DOCUMENT_NUMBER,
TITLE,
LEAD_UNIT_NAME,
LEAD_UNIT_NUMBER,
STATUS,
STATUS_CODE,
SPONSOR_CODE,
SPONSOR_NAME,
PERSON_ID,
PI_NAME, 
LOAD_TIMESTAMP 
)
SELECT     
3,
EPS_PROPOSAL.PROPOSAL_NUMBER,
EPS_PROPOSAL.DOCUMENT_NUMBER,
EPS_PROPOSAL.TITLE,
UNIT.UNIT_NAME AS LEAD_UNIT,
EPS_PROPOSAL.OWNED_BY_UNIT AS LEAD_UNIT_NUMBER, 
PROPOSAL_STATE.DESCRIPTION AS STATUS,
EPS_PROPOSAL.STATUS_CODE,
EPS_PROPOSAL.SPONSOR_CODE,
SPONSOR.SPONSOR_NAME AS SPONSOR,
T1.PERSON_ID,
T1.FULL_NAME, 
SYSDATE
FROM EPS_PROPOSAL 
INNER JOIN UNIT ON UNIT.UNIT_NUMBER = EPS_PROPOSAL.OWNED_BY_UNIT
LEFT OUTER JOIN SPONSOR ON SPONSOR.SPONSOR_CODE = EPS_PROPOSAL.SPONSOR_CODE
INNER JOIN PROPOSAL_STATE ON  PROPOSAL_STATE.STATE_TYPE_CODE = EPS_PROPOSAL.STATUS_CODE
LEFT OUTER JOIN EPS_PROP_PERSON T1 ON EPS_PROPOSAL.PROPOSAL_NUMBER = T1.PROPOSAL_NUMBER
/
COMMIT
/
-------------------------IRB---------------------------
INSERT INTO MITKC_ELASTIC_INDEX(
MODULE_CODE,
MODULE_ITEM_KEY,
DOCUMENT_NUMBER,
PROTOCOL_ID,
TITLE,
LEAD_UNIT_NAME,
LEAD_UNIT_NUMBER,
STATUS,
STATUS_CODE,
PROTOCOL_TYPE,
PERSON_ID,
PI_NAME, 
LOAD_TIMESTAMP 
)

SELECT 
7,
T1.PROTOCOL_NUMBER,
T1.DOCUMENT_NUMBER,
T1.PROTOCOL_ID,     
T1.TITLE,
T5.UNIT_NAME AS LEAD_UNIT,
T4.UNIT_NUMBER,  
T3.DESCRIPTION AS STATUS,
T1.PROTOCOL_STATUS_CODE AS STATUS_CODE,
T2.DESCRIPTION AS PROTOCOL_TYPE,    
T6.PERSON_ID,
T6.FULL_NAME AS PERSON_NAME,
SYSDATE
FROM PROTOCOL T1
INNER JOIN PROTOCOL_TYPE T2 ON T1.PROTOCOL_TYPE_CODE = T2.PROTOCOL_TYPE_CODE
INNER JOIN PROTOCOL_STATUS T3 ON T1.PROTOCOL_STATUS_CODE = T3.PROTOCOL_STATUS_CODE
LEFT OUTER JOIN PROTOCOL_UNITS T4 ON T1.PROTOCOL_NUMBER = T4.PROTOCOL_NUMBER 
						AND T1.SEQUENCE_NUMBER = T4.SEQUENCE_NUMBER
						AND T4.LEAD_UNIT_FLAG = 'Y'
LEFT OUTER JOIN UNIT T5 ON T4.UNIT_NUMBER = T5.UNIT_NUMBER
LEFT OUTER JOIN PROTOCOL_PERSONS T6 ON T1.PROTOCOL_ID=T6.PROTOCOL_ID
/
COMMIT
/
---------------------------------IACUC--------------------------------------------
INSERT INTO MITKC_ELASTIC_INDEX(
MODULE_CODE,
MODULE_ITEM_KEY,
DOCUMENT_NUMBER,
PROTOCOL_ID,
TITLE,
LEAD_UNIT_NAME,
LEAD_UNIT_NUMBER,
STATUS,
STATUS_CODE,
PROTOCOL_TYPE,
PERSON_ID,
PI_NAME, 
LOAD_TIMESTAMP 
)
SELECT  
9,
T1.PROTOCOL_NUMBER,
T1.DOCUMENT_NUMBER, 
T1.PROTOCOL_ID,
T1.TITLE,
T5.UNIT_NAME AS LEAD_UNIT,
T4.UNIT_NUMBER AS LEAD_UNIT_NUMBER,
T3.DESCRIPTION AS STATUS,
T1.PROTOCOL_STATUS_CODE AS STATUS_CODE,
T2.DESCRIPTION AS PROTOCOL_TYPE, 
T6.PERSON_ID,
T6.FULL_NAME AS PERSON_NAME,
SYSDATE
FROM IACUC_PROTOCOL T1
INNER JOIN IACUC_PROTOCOL_TYPE T2 ON T1.PROTOCOL_TYPE_CODE = T2.PROTOCOL_TYPE_CODE
INNER JOIN IACUC_PROTOCOL_STATUS T3 ON T1.PROTOCOL_STATUS_CODE = T3.PROTOCOL_STATUS_CODE
LEFT OUTER JOIN IACUC_PROTOCOL_UNITS T4 ON T1.PROTOCOL_NUMBER = T4.PROTOCOL_NUMBER 
							AND T1.SEQUENCE_NUMBER = T4.SEQUENCE_NUMBER
							AND T4.LEAD_UNIT_FLAG = 'Y'
LEFT OUTER JOIN UNIT T5 ON T4.UNIT_NUMBER = T5.UNIT_NUMBER
LEFT OUTER JOIN IACUC_PROTOCOL_PERSONS T6 ON T1.PROTOCOL_ID=T6.PROTOCOL_ID
/
commit
/
-------------------------------------------DISCLOSURE----------------------------------
INSERT INTO MITKC_ELASTIC_INDEX(
MODULE_CODE,
MODULE_ITEM_KEY,
DOCUMENT_NUMBER,
COI_DISCLOSURE_ID,
PERSON_ID,
PI_NAME, 
DISCLOSURE_DISPOSITION,
STATUS,
STATUS_CODE,
COI_DISCLOSURE_NUMBER,
LOAD_TIMESTAMP 
)
SELECT    
8,
T1.MODULE_ITEM_KEY,
T1.DOCUMENT_NUMBER,
T1.COI_DISCLOSURE_ID,
T1.PERSON_ID,
(T5.LAST_NM||', '||T5.FIRST_NM||' '||T5.MIDDLE_NM) AS FULL_NAME,
T3.DESCRIPTION AS DISCLOSURE_DISPOSITION,
T2.DESCRIPTION AS DISCLOSURE_STATUS,
T1.DISCLOSURE_STATUS_CODE,  
T1.COI_DISCLOSURE_NUMBER,
SYSDATE
FROM COI_DISCLOSURE T1 
INNER JOIN COI_DISCLOSURE_STATUS T2 ON T2.COI_DISCLOSURE_STATUS_CODE = T1.DISCLOSURE_STATUS_CODE 
INNER JOIN COI_DISPOSITION_STATUS T3 ON T3.COI_DISPOSITION_CODE = T1.DISCLOSURE_DISPOSITION_CODE
LEFT OUTER JOIN KRIM_PRNCPL_T T4 ON T4.PRNCPL_ID = T1.PERSON_ID 
LEFT OUTER JOIN KRIM_ENTITY_NM_T T5 ON T4.ENTITY_ID = T5.ENTITY_ID;
/
COMMIT
/

-------------------------------------SUBAWARD----------------------------------------------------
delete from MITKC_ELASTIC_INDEX where MODULE_CODE = 4
/
INSERT INTO MITKC_ELASTIC_INDEX(
MODULE_CODE,
MODULE_ITEM_KEY,
ACCOUNT_NUMBER,
DOCUMENT_NUMBER,
SUBAWARD_PO,
SUBRECIPIENT,
SUB_PRIME,
SPONSOR_AWARD_ID,
STATUS_CODE,
STATUS,
PERSON_ID,
PI_NAMe 
)

SELECT 
4,
T1.SUBAWARD_CODE,
T1.ACCOUNT_NUMBER,
T1.DOCUMENT_NUMBER,
T1.PURCHASE_ORDER_NUM,
T2.ORGANIZATION_NAME AS SUBRECIPIENT,
T7.SPONSOR_NAME AS SUB_PRIME,
T6.SPONSOR_AWARD_NUMBER AS SPONSOR_AWARD_ID,
T1.STATUS_CODE,
T3.DESCRIPTION AS STATUS,
T8.PERSON_ID AS PI_PERSON_ID,
T9.FULL_NAME AS PI_NAME
FROM SUBAWARD T1
LEFT OUTER JOIN ORGANIZATION T2 ON T1.ORGANIZATION_ID = T2.ORGANIZATION_ID
LEFT OUTER JOIN SUBAWARD_STATUS T3 ON T1.STATUS_CODE = T3.SUBAWARD_STATUS_CODE
LEFT OUTER JOIN SUBAWARD_FUNDING_SOURCE T4 ON T1.SUBAWARD_CODE = T4.SUBAWARD_CODE
LEFT OUTER JOIN AWARD T6 ON T4.AWARD_ID = T6.AWARD_ID
LEFT OUTER JOIN SPONSOR T7 ON T6.SPONSOR_CODE = T7.SPONSOR_CODE
LEFT OUTER JOIN AWARD_PERSONS T8 ON T6.AWARD_ID = T8.AWARD_ID 
                                 AND T8.CONTACT_ROLE_CODE = 'PI'
LEFT OUTER JOIN KC_PERSON_MV T9 ON T8.PERSON_ID = T9.PRNCPL_ID
WHERE T1.SUBAWARD_SEQUENCE_STATUS = 'ACTIVE'
AND T4.SUBAWARD_FUNDING_SOURCE_ID IN (SELECT MAX(SUBAWARD_FUNDING_SOURCE_ID)
                                              FROM SUBAWARD_FUNDING_SOURCE
                                              WHERE SUBAWARD_CODE = T1.SUBAWARD_CODE)
/
commit
/