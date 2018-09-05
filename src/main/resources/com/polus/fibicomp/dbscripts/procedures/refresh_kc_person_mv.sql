CREATE  PROCEDURE refresh_kc_person_mv(
)
BEGIN
	TRUNCATE TABLE KC_PERSON_MV;
	
	INSERT INTO KC_PERSON_MV
(
	PRNCPL_ID, 
	PRNCPL_NM, 
	FIRST_NM, 
	MIDDLE_NM,
	LAST_NM,
	FULL_NAME, 
	EMAIL_ADDR,
	PRMRY_DEPT_CD,
	UNIT_NAME, 
	UNIT_NUMBER,
	PHONE_NBR, 
	ADDR_LINE_1	
)
	SELECT 
	  kpt.prncpl_id,
	  kpt.prncpl_nm, 
	  kent.first_nm,
	  kent.middle_nm,
	  kent.last_nm,
	 case 
		when MIDDLE_NM is null then concat(kent.last_nm,', ',kent.first_nm) 
	 else
		concat(kent.last_nm,', ',kent.first_nm,' ',kent.middle_nm)
	 end as full_name,
	  keet.email_addr,
	  keit.prmry_dept_cd,
	  un.unit_name,
	  un.unit_number,
	  ph.phone_nbr,
	  addr.addr_line_1
	  FROM krim_prncpl_t kpt 
      LEFT OUTER JOIN krim_entity_nm_t kent ON kpt.entity_id = kent.entity_id
											AND kent.dflt_ind = 'Y'
	  LEFT OUTER JOIN krim_entity_email_t keet ON kpt.entity_id = keet.entity_id
												AND keet.dflt_ind = 'Y'
	  LEFT OUTER JOIN krim_entity_emp_info_t keit ON kpt.entity_id = keit.entity_id
												  AND keit.PRMRY_IND = 'Y'
	  LEFT OUTER JOIN unit un ON keit.prmry_dept_cd = un.unit_number
	  LEFT OUTER JOIN krim_entity_phone_t ph ON ph.entity_id = kpt.entity_id and ph.dflt_ind = 'Y'
	  LEFT OUTER JOIN krim_entity_addr_t addr ON addr.entity_id = kpt.entity_id and addr.actv_ind = 'Y'
      WHERE kpt.ACTV_IND = 'Y';
      COMMIT;
END