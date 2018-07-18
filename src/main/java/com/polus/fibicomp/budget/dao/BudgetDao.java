package com.polus.fibicomp.budget.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.budget.common.pojo.InstituteRate;

@Service
public interface BudgetDao {

	public List<InstituteRate> filterInstituteRateByDateRange(Date startDate, Date endDate);

}
