package com.polus.fibicomp.ip.service;

import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.common.service.CommonService;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.ip.dao.InstitutionalProposalDao;

@Transactional
@Service(value = "institutionalProposalService")
public class InstitutionalProposalServiceImpl implements InstitutionalProposalService {

	protected static Logger logger = Logger.getLogger(InstitutionalProposalServiceImpl.class.getName());

	@Autowired
	@Qualifier(value = "institutionalProposalDao")
	private InstitutionalProposalDao institutionalProposalDao;

	@Autowired
	private CommonService commonService;

	@Override
	public boolean createInstitutionalProposal(Integer proposalId, String ipNumber, String userName) {
		return institutionalProposalDao.createInstitutionalProposal(proposalId, ipNumber, userName);
	}

	@Override
	public String generateInstitutionalProposalNumber() {
		boolean fyBasedIp = commonService.getParameterValueAsBoolean(Constants.MODULE_NAMESPACE_PROPOSAL_DEVELOPMENT, Constants.DOCUMENT_COMPONENT, Constants.FISCAL_YEAR_BASED_IP);
		return fyBasedIp ? getNextIPFiscalYearBased() :
             new DecimalFormat(Constants.DECIMAL_FORMAT).format(commonService.getNextSequenceNumber(Constants.INSTITUTIONAL_PROPSAL_PROPSAL_NUMBER_SEQUENCE));
	}

	private String getNextIPFiscalYearBased() {
        String fiscalYear =  StringUtils.substring(commonService.getCurrentFiscalYear().toString(),2,4);
        String fiscalMonth = StringUtils.leftPad(commonService.getCurrentFiscalMonthForDisplay().toString(), 2, "0");
        Long nextProposalNumberSeq = commonService.getNextSequenceNumber(Constants.INSTITUTIONAL_PROPSAL_PROPSAL_NUMBER_SEQUENCE);
        return fiscalYear + fiscalMonth + new DecimalFormat(Constants.DECIMAL_FORMAT_FOR_NEW_IP).format(nextProposalNumberSeq);
    }

}
