CREATE  PROCEDURE refresh_unit_hierarchy_tmp_tab()
BEGIN
 truncate table unit_tmp;
	
	SET @row_number = 0;
     
	insert into unit_tmp 
	(id,UNIT_NUMBER,UNIT_NAME,PARENT_UNIT_NUMBER,ACTIVE_FLAG )
	SELECT 
		(@row_number:=@row_number + 1) AS num, UNIT_NUMBER,
		UNIT_NAME,PARENT_UNIT_NUMBER,ACTIVE_FLAG 
		from unit;
	commit;
END