package com.polus.fibicomp.budget.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.budget.common.pojo.InstituteRate;
import com.polus.fibicomp.budget.dao.BudgetDao;
import com.polus.fibicomp.budget.pojo.FibiProposalRate;
import com.polus.fibicomp.budget.vo.BudgetVO;
import com.polus.fibicomp.committee.dao.CommitteeDao;

@Transactional
@Service(value = "budgetService")
public class BudgetServiceImpl implements BudgetService {

	protected static Logger logger = Logger.getLogger(BudgetServiceImpl.class.getName());

	@Autowired
	private BudgetDao budgetDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Override
	public List<FibiProposalRate> fetchFilteredProposalRates(BudgetVO budgetVO) {
		List<InstituteRate> instituteRates = budgetDao.filterInstituteRateByDateRange(null, null);
		if (instituteRates != null && !instituteRates.isEmpty()) {
			List<FibiProposalRate> proposalRates = new ArrayList<FibiProposalRate>();
			for (InstituteRate instituteRate : instituteRates) {
				FibiProposalRate proposalRate = new FibiProposalRate();
				proposalRate.setApplicableRate(instituteRate.getInstituteRate());
				proposalRate.setFiscalYear(instituteRate.getFiscalYear());
				proposalRate.setInstituteRate(instituteRate.getInstituteRate());
				proposalRate.setOnOffCampusFlag(instituteRate.getOnOffCampusFlag());
				//proposalRate.setProposalBudget(null);
				proposalRate.setRateClassCode(instituteRate.getRateClassCode());
				proposalRate.setRateTypeCode(instituteRate.getRateTypeCode());
				proposalRate.setStartDate(instituteRate.getStartDate());
				proposalRate.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
				proposalRate.setUpdateUser(null);
				proposalRates.add(proposalRate);
			}
		}
		return null;
	}

}
