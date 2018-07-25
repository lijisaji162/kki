package com.polus.fibicomp.budget.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.budget.common.pojo.InstituteRate;
import com.polus.fibicomp.budget.common.pojo.RateType;
import com.polus.fibicomp.budget.pojo.CostElement;

@Service
public interface BudgetDao {

	public List<InstituteRate> filterInstituteRateByDateRange(Date startDate, Date endDate);

	public List<CostElement> getAllCostElements();

	public RateType getOHRateTypeByParams(String rateClassCode, String rateTypeCode);

	public BigDecimal fetchApplicableRateByStartDate(Date budgetStartDate);

}
