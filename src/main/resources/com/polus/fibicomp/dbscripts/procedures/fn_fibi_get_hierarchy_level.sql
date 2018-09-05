CREATE FUNCTION fn_fibi_get_hierarchy_level(av_award_number  varchar(12)) RETURNS int(11)
begin
 
 DECLARE lvl int;
 DECLARE ls_award_number varchar(12);
 
  select av_award_number into ls_award_number from dual;
 
 set lvl = 0;

   repeat
       set lvl = lvl + 1;
       
		select PARENT_AWARD_NUMBER into ls_award_number 
        from award_hierarchy 
        where award_number = ls_award_number;
     
      until  ls_award_number='000000-00000' or ls_award_number is NULL end repeat;
   return lvl;
end