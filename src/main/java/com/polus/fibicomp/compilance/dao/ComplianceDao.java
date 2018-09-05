package com.polus.fibicomp.compilance.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.compilance.pojo.SpecialReviewApprovalType;
import com.polus.fibicomp.compilance.pojo.SpecialReviewType;
import com.polus.fibicomp.compilance.pojo.SpecialReviewUsage;

@Service
public interface ComplianceDao {

	public List<SpecialReviewType> fetchAllSpecialReviewType();

	public List<SpecialReviewUsage> fetchSpecialReviewUsageByModuleCode(String moduleCode);

	public List<SpecialReviewApprovalType> fetchSpecialReviewApprovalTypeNotInCodes(List<String> approvalTypeCodes);

}
