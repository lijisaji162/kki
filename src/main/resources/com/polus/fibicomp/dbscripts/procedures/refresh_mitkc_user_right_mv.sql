CREATE  PROCEDURE refresh_mitkc_user_right_mv()
BEGIN


TRUNCATE TABLE MITKC_USER_RIGHT_MV;
	
INSERT INTO MITKC_USER_RIGHT_MV
(
PERSON_ID,
UNIT_NUMBER,
DESCEND_FLAG,
PERM_NM,
ACTV_TO_DT
)
select distinct t1.MBR_ID as PERSON_ID,t6.child_unit_number as UNIT_NUMBER,t3.ATTR_VAL as DESCEND_FLAG,t5.NM AS PERM_NM,t1.ACTV_TO_DT
FROM KRIM_ROLE_MBR_T t1
INNER JOIN krim_role_mbr_attr_data_t t2 on t1.ROLE_MBR_ID = t2.ROLE_MBR_ID
INNER JOIN krim_role_mbr_attr_data_t t3 on t1.ROLE_MBR_ID = t3.ROLE_MBR_ID       
INNER JOIN KRIM_ROLE_PERM_T t4 on t4.ROLE_ID = t1.ROLE_ID
INNER JOIN KRIM_PERM_T t5 on t5.PERM_ID = t4.PERM_ID   
INNER JOIN MITKC_UNIT_WITH_CHILDREN t6 on t6.UNIT_NUMBER = t2.ATTR_VAL
WHERE t3.ATTR_VAL IN ('Y', 'Yes')
AND t2.ATTR_VAL not in ( 'Y', 'N', 'Yes', 'No' )
AND ( t1.ACTV_TO_DT is null OR t1.ACTV_TO_DT >= now() )
AND t5.NM in (
'View Proposal',
'View Protocol',
'View IACUC Protocol',
'Create ProtocolDocument',
'Create IACUC Protocol',
'Create ProposalDevelopmentDocument',
'CREATE SUBAWARD',
'Maintain Coi Disclosure',
'CREATE NEGOTIATION',
'View Coi Disclosure',
'VIEW SUBAWARD',
'OSPGO_Admin',
'VIEW NEGOTIATION',
'VIEW NEGOTIATION - UNRESTRICTED',
'OSP Administrator',
'OST Administrator',
'OST Department Administrator',
'Cost_Share_Maintenance',
'IRB Administrator',
'Negotiation Administrator',
'Approve AwardBudget',
'Modify Award',
'Create Award',
'View Award',
'View AwardBudget',
'Create AwardBudget',
'Modify AwardBudget',
'Submit AwardBudget',
'OST Reject'
)
union 
select distinct t1.MBR_ID as PERSON_ID,t2.ATTR_VAL as UNIT_NUMBER,t3.ATTR_VAL as DESCEND_FLAG,t5.NM AS PERM_NM,t1.ACTV_TO_DT
FROM KRIM_ROLE_MBR_T t1
INNER JOIN krim_role_mbr_attr_data_t t2 on t1.ROLE_MBR_ID = t2.ROLE_MBR_ID
INNER JOIN krim_role_mbr_attr_data_t t3 on t1.ROLE_MBR_ID = t3.ROLE_MBR_ID        
INNER JOIN KRIM_ROLE_PERM_T t4 on t4.ROLE_ID = t1.ROLE_ID
INNER JOIN KRIM_PERM_T t5 on t5.PERM_ID = t4.PERM_ID           
WHERE t3.ATTR_VAL  IN ('N', 'No') 
AND t2.ATTR_VAL not in ( 'Y', 'N', 'Yes', 'No' )
AND ( t1.ACTV_TO_DT is null OR t1.ACTV_TO_DT >= now() )
AND t5.NM in (
'View Proposal',
'View Protocol',
'View IACUC Protocol',
'Create ProtocolDocument',
'Create IACUC Protocol',
'Create ProposalDevelopmentDocument',
'CREATE SUBAWARD',
'Maintain Coi Disclosure',
'CREATE NEGOTIATION',
'View Coi Disclosure',
'VIEW SUBAWARD',
'OSPGO_Admin',
'VIEW NEGOTIATION',
'VIEW NEGOTIATION - UNRESTRICTED',
'OSP Administrator',
'OST Administrator',
'OST Department Administrator',
'Cost_Share_Maintenance',
'IRB Administrator',
'Negotiation Administrator',
'Approve AwardBudget',
'Modify Award',
'Create Award',
'View Award',
'View AwardBudget',
'Create AwardBudget',
'Modify AwardBudget',
'Submit AwardBudget',
'OST Reject'
)
union
select distinct t1.MBR_ID as PERSON_ID,'000001' as UNIT_NUMBER,'Y' as DESCEND_FLAG,t5.NM AS PERM_NM,t1.ACTV_TO_DT
FROM KRIM_ROLE_MBR_T t1
LEFT OUTER JOIN krim_role_mbr_attr_data_t t2 on t1.ROLE_MBR_ID = t2.ROLE_MBR_ID    
INNER JOIN KRIM_ROLE_PERM_T t4 on t4.ROLE_ID = t1.ROLE_ID
INNER JOIN KRIM_PERM_T t5 on t5.PERM_ID = t4.PERM_ID  
WHERE t2.ROLE_MBR_ID is null
AND ( t1.ACTV_TO_DT is null OR t1.ACTV_TO_DT >= now() )
AND t5.NM in (
'View Proposal',
'View Protocol',
'View IACUC Protocol',
'Create ProtocolDocument',
'Create IACUC Protocol',
'Create ProposalDevelopmentDocument',
'CREATE SUBAWARD',
'Maintain Coi Disclosure',
'CREATE NEGOTIATION',
'View Coi Disclosure',
'VIEW SUBAWARD',
'OSPGO_Admin',
'VIEW NEGOTIATION',
'VIEW NEGOTIATION - UNRESTRICTED',
'OSP Administrator',
'OST Administrator',
'OST Department Administrator',
'Cost_Share_Maintenance',
'IRB Administrator',
'Negotiation Administrator',
'Approve AwardBudget',
'Modify Award',
'Create Award',
'View Award',
'View AwardBudget',
'Create AwardBudget',
'Modify AwardBudget',
'Submit AwardBudget',
'OST Reject'
);
commit;
END