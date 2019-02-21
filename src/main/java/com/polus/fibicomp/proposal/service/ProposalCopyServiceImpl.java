package com.polus.fibicomp.proposal.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.budget.common.pojo.ValidCeRateType;
import com.polus.fibicomp.budget.dao.BudgetDao;
import com.polus.fibicomp.budget.pojo.BudgetDetail;
import com.polus.fibicomp.budget.pojo.BudgetDetailCalcAmount;
import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.budget.pojo.FibiProposalRate;
import com.polus.fibicomp.budget.service.BudgetService;
import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.compilance.pojo.ProposalSpecialReview;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachment;
import com.polus.fibicomp.proposal.pojo.ProposalIrbProtocol;
import com.polus.fibicomp.proposal.pojo.ProposalKeyword;
import com.polus.fibicomp.proposal.pojo.ProposalPerson;
import com.polus.fibicomp.proposal.pojo.ProposalPersonUnit;
import com.polus.fibicomp.proposal.pojo.ProposalResearchArea;
import com.polus.fibicomp.proposal.pojo.ProposalSponsor;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@Transactional
@Service(value = "proposalCopyService")
public class ProposalCopyServiceImpl implements ProposalCopyService {

	protected static Logger logger = Logger.getLogger(ProposalCopyServiceImpl.class.getName());

	@Autowired
	@Qualifier(value = "proposalDao")
	private ProposalDao proposalDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private BudgetService budgetService;

	@Autowired
	private BudgetDao budgetDao;

	@Override
	public String copyProposal(ProposalVO vo) {
		Proposal originalProposal = null;
		if (vo.getProposal() != null) {
			originalProposal = vo.getProposal();
		} else {
			originalProposal = proposalDao.fetchProposalById(vo.getProposalId());
		}
		originalProposal = proposalDao.saveOrUpdateProposal(originalProposal);
		Proposal copyProposal = new Proposal();
		copyProposalMandatoryFields(copyProposal, originalProposal, vo.getUserFullName());
		copyProposalNonMandatoryFields(copyProposal, originalProposal, vo.getUserFullName());
		if (originalProposal.getBudgetHeader() != null) {
			BudgetHeader budgetHeader = copyProposalBudgetHeader(copyProposal, originalProposal, vo);
			copyProposal.setBudgetHeader(budgetHeader);
			copyProposal = proposalDao.saveOrUpdateProposal(copyProposal);
			if (copyProposal.getBudgetHeader().getIsAutoCalc() != null && !copyProposal.getBudgetHeader().getIsAutoCalc()) {
				copyProposal = budgetService.calculateCost(copyProposal);
			}
			vo.setProposal(copyProposal);
			copyProposal = budgetService.saveOrUpdateProposalBudget(vo);
		}
		copyProposal = proposalDao.saveOrUpdateProposal(copyProposal);
		vo.setStatus(true);
		vo.setMessage("Proposal copied successfully");
		vo.setProposal(copyProposal);
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	private void copyProposalMandatoryFields(Proposal copyProposal, Proposal originalProposal, String updateUser) {
		copyProposal.setTitle(originalProposal.getTitle());
		copyProposal.setActivityTypeCode(originalProposal.getActivityTypeCode());
		copyProposal.setActivityType(originalProposal.getActivityType());
		copyProposal.setTypeCode(originalProposal.getTypeCode());
		copyProposal.setProposalType(originalProposal.getProposalType());
		copyProposal.setHomeUnitNumber(originalProposal.getHomeUnitNumber());
		copyProposal.setHomeUnitName(originalProposal.getHomeUnitName());
		copyProposal.setSponsorCode(originalProposal.getSponsorCode());
		copyProposal.setSponsorName(originalProposal.getSponsorName());
		copyProposal.setStartDate(originalProposal.getStartDate());
		copyProposal.setEndDate(originalProposal.getEndDate());
		copyProposal.setSubmissionDate(originalProposal.getSubmissionDate());
		copyProposal.setSponsorDeadlineDate(originalProposal.getSponsorDeadlineDate());
		copyProposal.setInternalDeadLineDate(originalProposal.getInternalDeadLineDate());
		copyProposal = proposalDao.saveOrUpdateProposal(copyProposal);
		copyProposal.setProposalPersons(copyProposalPersons(copyProposal, originalProposal, updateUser));
	}

	private List<ProposalPerson> copyProposalPersons(Proposal copyProposal, Proposal proposal, String updateUser) {
		List<ProposalPerson> proposalPersons = proposal.getProposalPersons();
		List<ProposalPerson> copiedProposalPersons = new ArrayList<>(proposalPersons);
		Collections.copy(copiedProposalPersons, proposalPersons);
		List<ProposalPerson> newProposalPersons = new ArrayList<>();
		for (ProposalPerson copiedPersonDetail : copiedProposalPersons) {
			ProposalPerson personDetail = new ProposalPerson();
			personDetail.setProposal(copyProposal);
			personDetail.setPercentageOfEffort(copiedPersonDetail.getPercentageOfEffort());
			personDetail.setPersonId(copiedPersonDetail.getPersonId());
			personDetail.setRolodexId(copiedPersonDetail.getRolodexId());
			personDetail.setFullName(copiedPersonDetail.getFullName());
			personDetail.setPersonRoleId(copiedPersonDetail.getPersonRoleId());
			personDetail.setProposalPersonRole(copiedPersonDetail.getProposalPersonRole());
			personDetail.setUpdateUser(updateUser);
			personDetail.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
			personDetail.setEmailAddress(copiedPersonDetail.getEmailAddress());
			personDetail.setOrganisation(copiedPersonDetail.getOrganisation());
			List<ProposalPersonUnit> units = copiedPersonDetail.getUnits();
			if (units != null && !units.isEmpty()) {
				personDetail.getUnits().addAll(copyProposalPersonUnits(copiedPersonDetail, personDetail, updateUser));
			}
			newProposalPersons.add(personDetail);
		}
		return newProposalPersons;
	}

	private List<ProposalPersonUnit> copyProposalPersonUnits(ProposalPerson copiedPersonDetail,
			ProposalPerson personDetail, String updateUser) {
		List<ProposalPersonUnit> proposalPersonUnits = copiedPersonDetail.getUnits();
		List<ProposalPersonUnit> copiedProposalPersonUnits = new ArrayList<>(proposalPersonUnits);
		Collections.copy(copiedProposalPersonUnits, proposalPersonUnits);
		List<ProposalPersonUnit> newProposalPersonUnits = new ArrayList<>();
		for (ProposalPersonUnit copiedPersonPersonUnit : copiedProposalPersonUnits) {
			ProposalPersonUnit personUnit = new ProposalPersonUnit();
			personUnit.setProposalPerson(personDetail);
			personUnit.setUnitNumber(copiedPersonPersonUnit.getUnitNumber());
			personUnit.setLeadUnit(copiedPersonPersonUnit.isLeadUnit());
			personUnit.setUnit(copiedPersonPersonUnit.getUnit());
			personUnit.setUpdateUser(updateUser);
			personUnit.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
			newProposalPersonUnits.add(personUnit);
		}
		return newProposalPersonUnits;
	}

	private void copyProposalNonMandatoryFields(Proposal copyProposal, Proposal originalProposal, String updateUser) {
		copyProposal.setGrantCallId(originalProposal.getGrantCallId());
		copyProposal.setGrantCall(originalProposal.getGrantCall());
		copyProposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS);
		copyProposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS));
		copyProposal.setAbstractDescription(originalProposal.getAbstractDescription());
		copyProposal.setResearchDescription(originalProposal.getResearchDescription());
		copyProposal.setCreateUser(updateUser);
		copyProposal.setUpdateUser(updateUser);
		copyProposal.setGrantTypeCode(originalProposal.getGrantTypeCode());
		copyProposal.setGrantCallType(originalProposal.getGrantCallType());
		copyProposal.setSponsorProposalNumber(originalProposal.getSponsorProposalNumber());
		copyProposal.setPrincipalInvestigator(originalProposal.getPrincipalInvestigator());
		copyProposal.setApplicationActivityType(originalProposal.getApplicationActivityType());
		copyProposal.setApplicationType(originalProposal.getApplicationType());
		copyProposal.setApplicationStatus(originalProposal.getApplicationStatus());
		copyProposal.setCreateTimeStamp(committeeDao.getCurrentTimestamp());
		copyProposal.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
		copyProposal.setIsSubcontract(originalProposal.getIsSubcontract());
		copyProposal.setIsDomesticSite(originalProposal.getIsDomesticSite());
		copyProposal.setIsMultisiteStudy(originalProposal.getIsMultisiteStudy());
		if (originalProposal.getProposalAttachments() != null && !originalProposal.getProposalAttachments().isEmpty()) {
			copyProposal.setProposalAttachments(copyProposalAttachments(copyProposal, originalProposal, updateUser));
		}
		if (originalProposal.getPropSpecialReviews() != null && !originalProposal.getPropSpecialReviews().isEmpty()) {
			copyProposal.setPropSpecialReviews(copyProposalSpecialReview(copyProposal, originalProposal, updateUser));
		}
		if (originalProposal.getProposalKeywords() != null && !originalProposal.getProposalKeywords().isEmpty()) {
			copyProposal.setProposalKeywords(copyProposalKeywords(copyProposal, originalProposal, updateUser));
		}
		if (originalProposal.getProposalIrbProtocols() != null
				&& !originalProposal.getProposalIrbProtocols().isEmpty()) {
			copyProposal.setProposalIrbProtocols(copyProposalIrbProtocols(copyProposal, originalProposal, updateUser));
		}
		if (originalProposal.getProposalResearchAreas() != null && !originalProposal.getProposalResearchAreas().isEmpty()) {
			copyProposal.setProposalResearchAreas(copyProposalResearchAreas(copyProposal, originalProposal, updateUser));
		}
		if (originalProposal.getProposalSponsors() != null && !originalProposal.getProposalSponsors().isEmpty()) {
			copyProposal.setProposalSponsors(copyProposalSponsors(copyProposal, originalProposal, updateUser));
		}
	}

	public List<ProposalAttachment> copyProposalAttachments(Proposal copyProposal, Proposal proposal, String updateUser) {
		List<ProposalAttachment> proposalAttachments = proposal.getProposalAttachments();
		List<ProposalAttachment> copiedProposalAttachments = new ArrayList<>(proposalAttachments);
		Collections.copy(copiedProposalAttachments, proposalAttachments);
		List<ProposalAttachment> newAttachments = new ArrayList<>();
		for (ProposalAttachment copiedAttachmentDetail : copiedProposalAttachments) {
			ProposalAttachment attachmentDetail = new ProposalAttachment();
			attachmentDetail.setProposal(copyProposal);
			attachmentDetail.setAttachment(copiedAttachmentDetail.getAttachment());
			attachmentDetail.setAttachmentTypeCode(copiedAttachmentDetail.getAttachmentTypeCode());
			attachmentDetail.setAttachmentType(copiedAttachmentDetail.getAttachmentType());
			attachmentDetail.setDescription(copiedAttachmentDetail.getDescription());
			attachmentDetail.setFileName(copiedAttachmentDetail.getFileName());
			attachmentDetail.setMimeType(copiedAttachmentDetail.getMimeType());
			attachmentDetail.setUpdateUser(updateUser);
			attachmentDetail.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
			attachmentDetail.setNarrativeStatusCode(copiedAttachmentDetail.getNarrativeStatusCode());
			attachmentDetail.setNarrativeStatus(copiedAttachmentDetail.getNarrativeStatus());
			newAttachments.add(attachmentDetail);
		}
		return newAttachments;
	}

	private List<ProposalSpecialReview> copyProposalSpecialReview(Proposal copyProposal, Proposal proposal, String updateUser) {
		List<ProposalSpecialReview> proposalSpecialReviews = proposal.getPropSpecialReviews();
		List<ProposalSpecialReview> copiedProposalSpecialReviews = new ArrayList<>(proposalSpecialReviews);
		Collections.copy(copiedProposalSpecialReviews, proposalSpecialReviews);
		List<ProposalSpecialReview> newSpecialReviews = new ArrayList<>();
		for (ProposalSpecialReview copiedSpecialReviewDetail : copiedProposalSpecialReviews) {
			ProposalSpecialReview specialReviewDetail = new ProposalSpecialReview();
			specialReviewDetail.setProposal(copyProposal);
			specialReviewDetail.setSpecialReviewTypeCode(copiedSpecialReviewDetail.getSpecialReviewTypeCode());
			specialReviewDetail.setSpecialReviewType(copiedSpecialReviewDetail.getSpecialReviewType());
			specialReviewDetail.setApprovalTypeCode(copiedSpecialReviewDetail.getApprovalTypeCode());
			specialReviewDetail.setApprovalType(copiedSpecialReviewDetail.getApprovalType());
			specialReviewDetail.setProtocolNumber(copiedSpecialReviewDetail.getProtocolNumber());
			specialReviewDetail.setProtocolStatus(copiedSpecialReviewDetail.getProtocolStatus());
			specialReviewDetail.setApplicationDate(copiedSpecialReviewDetail.getApplicationDate());
			specialReviewDetail.setApprovalDate(copiedSpecialReviewDetail.getApprovalDate());
			specialReviewDetail.setExpirationDate(copiedSpecialReviewDetail.getExpirationDate());
			specialReviewDetail.setComments(copiedSpecialReviewDetail.getComments());
			specialReviewDetail.setUpdateUser(updateUser);
			specialReviewDetail.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
			newSpecialReviews.add(specialReviewDetail);
		}
		return newSpecialReviews;
	}

	private List<ProposalKeyword> copyProposalKeywords(Proposal copyProposal, Proposal proposal, String updateUser) {
		List<ProposalKeyword> proposalKeywords = proposal.getProposalKeywords();
		List<ProposalKeyword> copiedProposalKeywords = new ArrayList<>(proposalKeywords);
		Collections.copy(copiedProposalKeywords, proposalKeywords);
		List<ProposalKeyword> newKeywords = new ArrayList<>();
		for (ProposalKeyword copiedKeywordDetail : copiedProposalKeywords) {
			ProposalKeyword keywordtDetail = new ProposalKeyword();
			keywordtDetail.setProposal(copyProposal);
			keywordtDetail.setScienceKeywordCode(copiedKeywordDetail.getScienceKeywordCode());
			keywordtDetail.setScienceKeyword(copiedKeywordDetail.getScienceKeyword());
			keywordtDetail.setUpdateUser(updateUser);
			keywordtDetail.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
			newKeywords.add(keywordtDetail);
		}
		return newKeywords;
	}

	private List<ProposalIrbProtocol> copyProposalIrbProtocols(Proposal copyProposal, Proposal proposal, String updateUser) {
		List<ProposalIrbProtocol> proposalIrbProtocols = proposal.getProposalIrbProtocols();
		List<ProposalIrbProtocol> copiedProposalIrbProtocols = new ArrayList<>(proposalIrbProtocols);
		Collections.copy(copiedProposalIrbProtocols, proposalIrbProtocols);
		List<ProposalIrbProtocol> newIrbProtocols = new ArrayList<>();
		for (ProposalIrbProtocol copiedIrbProtocolDetail : copiedProposalIrbProtocols) {
			ProposalIrbProtocol irbProtocolDetail = new ProposalIrbProtocol();
			irbProtocolDetail.setProposal(copyProposal);
			irbProtocolDetail.setProtocolId(copiedIrbProtocolDetail.getProtocolId());
			irbProtocolDetail.setProtocol(copiedIrbProtocolDetail.getProtocol());
			irbProtocolDetail.setUpdateUser(updateUser);
			irbProtocolDetail.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
			newIrbProtocols.add(irbProtocolDetail);
		}
		return newIrbProtocols;
	}

	private List<ProposalResearchArea> copyProposalResearchAreas(Proposal copyProposal, Proposal proposal, String updateUser) {
		List<ProposalResearchArea> proposalResearchAreas = proposal.getProposalResearchAreas();
		List<ProposalResearchArea> copiedProposalResearchAreas = new ArrayList<>(proposalResearchAreas);
		Collections.copy(copiedProposalResearchAreas, proposalResearchAreas);
		List<ProposalResearchArea> newproposalResearchAreas = new ArrayList<>();
		for (ProposalResearchArea copiedResearchAreaDetail : copiedProposalResearchAreas) {
			ProposalResearchArea researchAreaDetail = new ProposalResearchArea();
			researchAreaDetail.setProposal(copyProposal);
			researchAreaDetail.setResearchAreaCode(copiedResearchAreaDetail.getResearchAreaCode());
			researchAreaDetail.setResearchArea(copiedResearchAreaDetail.getResearchArea());
			researchAreaDetail.setResearchTypeCode(copiedResearchAreaDetail.getResearchTypeCode());
			researchAreaDetail.setProposalResearchType(copiedResearchAreaDetail.getProposalResearchType());
			researchAreaDetail.setExcellenceAreaCode(copiedResearchAreaDetail.getExcellenceAreaCode());
			researchAreaDetail.setProposalExcellenceArea(copiedResearchAreaDetail.getProposalExcellenceArea());
			researchAreaDetail.setUpdateUser(updateUser);
			researchAreaDetail.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
			newproposalResearchAreas.add(researchAreaDetail);
		}
		return newproposalResearchAreas;
	}

	private List<ProposalSponsor> copyProposalSponsors(Proposal copyProposal, Proposal proposal, String updateUser) {
		List<ProposalSponsor> proposalSponsors = proposal.getProposalSponsors();
		List<ProposalSponsor> copiedProposalSponsors = new ArrayList<>(proposalSponsors);
		Collections.copy(copiedProposalSponsors, proposalSponsors);
		List<ProposalSponsor> newProposalSponsors = new ArrayList<>();
		for (ProposalSponsor copiedProposalSponsorsDetail : copiedProposalSponsors) {
			ProposalSponsor sponsorsDetail = new ProposalSponsor();
			sponsorsDetail.setProposal(copyProposal);
			sponsorsDetail.setSponsorCode(copiedProposalSponsorsDetail.getSponsorCode());
			sponsorsDetail.setSponsor(copiedProposalSponsorsDetail.getSponsor());
			sponsorsDetail.setStartDate(copiedProposalSponsorsDetail.getStartDate());
			sponsorsDetail.setEndDate(copiedProposalSponsorsDetail.getEndDate());
			sponsorsDetail.setAmount(copiedProposalSponsorsDetail.getAmount());
			sponsorsDetail.setUpdateUser(updateUser);
			sponsorsDetail.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
			newProposalSponsors.add(sponsorsDetail);
		}
		return newProposalSponsors;
	}

	private BudgetHeader copyProposalBudgetHeader(Proposal copyProposal, Proposal proposal, ProposalVO vo) {
		BudgetHeader originalBudget = proposal.getBudgetHeader();
		BudgetHeader copyBudget = new BudgetHeader();
		createBudgetHeader(copyBudget, originalBudget, vo.getUserFullName());
		copyProposal.setBudgetHeader(copyBudget);
		copyProposal = proposalDao.saveOrUpdateProposal(copyProposal);
		Set<String> rateClassTypes = new HashSet<>();
		copyBudget.setProposalRates(budgetService.fetchFilteredProposalRates(copyProposal, rateClassTypes));
		vo.setRateClassTypes(rateClassTypes);
		if (originalBudget.getBudgetPeriods() != null && !originalBudget.getBudgetPeriods().isEmpty()) {
			copyBudget.getBudgetPeriods().addAll(copyBudgetPeriods(copyBudget, originalBudget, proposal.getActivityTypeCode()));
		}
		return copyBudget;
	}

	private void createBudgetHeader(BudgetHeader copyBudget, BudgetHeader originalBudget, String userName) {
		copyBudget.setStartDate(originalBudget.getStartDate());
		copyBudget.setEndDate(originalBudget.getEndDate());
		copyBudget.setCreateTimeStamp(committeeDao.getCurrentTimestamp());
		copyBudget.setCreateUser(userName);
		copyBudget.setCreateUserName(userName);
		copyBudget.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
		copyBudget.setUpdateUser(userName);
		copyBudget.setUpdateUserName(userName);
		copyBudget.setRateType(originalBudget.getRateType());
		copyBudget.setRateClassCode(originalBudget.getRateClassCode());
		copyBudget.setRateTypeCode(originalBudget.getRateTypeCode());
		copyBudget.setIsAutoCalc(originalBudget.getIsAutoCalc());
		copyBudget = budgetDao.saveBudgetHeader(copyBudget);
	}

	private List<BudgetPeriod> copyBudgetPeriods(BudgetHeader copyBudget, BudgetHeader originalBudget, String activityTypeCode) {
		List<BudgetPeriod> budgetPeriods = originalBudget.getBudgetPeriods();
		List<BudgetPeriod> copiedBudgetPeriods = new ArrayList<>(budgetPeriods);
		Collections.copy(copiedBudgetPeriods, budgetPeriods);
		List<BudgetPeriod> newPeriods = new ArrayList<>();
		for (BudgetPeriod originalPeriod : copiedBudgetPeriods) {
			BudgetPeriod copyPeriod = new BudgetPeriod();
			copyPeriod.setModuleItemCode(originalPeriod.getModuleItemCode());
			copyPeriod.setModuleItemKey(originalPeriod.getModuleItemKey());
			copyPeriod.setVersionNumber(originalPeriod.getVersionNumber());
			copyPeriod.setBudgetPeriod(originalPeriod.getBudgetPeriod());
			copyPeriod.setStartDate(originalPeriod.getStartDate());
			copyPeriod.setEndDate(originalPeriod.getEndDate());
			copyPeriod.setTotalCost(originalPeriod.getTotalCost());
			copyPeriod.setTotalDirectCost(originalPeriod.getTotalDirectCost());
			copyPeriod.setTotalIndirectCost(originalPeriod.getTotalIndirectCost());
			copyPeriod.setPeriodLabel(originalPeriod.getPeriodLabel());
			copyPeriod.setIsObligatedPeriod(originalPeriod.getIsObligatedPeriod());
			copyPeriod.setBudget(copyBudget);
			// periodDetail = budgetDao.saveBudgetPeriod(periodDetail);
			if (originalPeriod.getBudgetDetails() != null && !originalPeriod.getBudgetDetails().isEmpty()) {
				copyBudgetDetails(copyPeriod, originalPeriod, activityTypeCode);
			}
			copyPeriod.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
			copyPeriod.setUpdateUser(copyBudget.getUpdateUser());
			copyPeriod.setSubcontractCost(originalPeriod.getSubcontractCost());
			copyPeriod = budgetDao.saveBudgetPeriod(copyPeriod);
			newPeriods.add(copyPeriod);
		}
		return newPeriods;
	}

	private void copyBudgetDetails(BudgetPeriod copyPeriod, BudgetPeriod period, String activityTypeCode) {
		List<BudgetDetail> budgetDetails = period.getBudgetDetails();
		if (budgetDetails != null && !budgetDetails.isEmpty()) {
			List<BudgetDetail> copiedBudgetDetails = new ArrayList<>(budgetDetails);
			Collections.copy(copiedBudgetDetails, budgetDetails);
			List<BudgetDetail> newLineItems = new ArrayList<>();
			for (BudgetDetail budgetDetail : copiedBudgetDetails) {
				BudgetDetail copyBudgetDetail = new BudgetDetail();
				copyBudgetDetail.setBudgetCategory(budgetDetail.getBudgetCategory());
				copyBudgetDetail.setBudgetCategoryCode(budgetDetail.getBudgetCategoryCode());
				copyBudgetDetail.setBudgetJustification(budgetDetail.getBudgetJustification());
				copyBudgetDetail.setBudgetPeriod(budgetDetail.getBudgetPeriod());
				copyBudgetDetail.setEndDate(budgetDetail.getEndDate());
				copyBudgetDetail.setIsSystemGeneratedCostElement(budgetDetail.getIsSystemGeneratedCostElement());
				copyBudgetDetail.setSystemGeneratedCEType(budgetDetail.getSystemGeneratedCEType());
				copyBudgetDetail.setIsApplyInflationRate(budgetDetail.getIsApplyInflationRate());
				// apply inflation here
				CostElement costElement = budgetDetail.getCostElement();
				costElement = budgetDao.fetchCostElementsById(costElement.getCostElement());
				copyBudgetDetail.setCostElement(costElement);
				copyBudgetDetail.setCostElementCode(budgetDetail.getCostElementCode());
				BigDecimal lineItemCost = budgetDetail.getLineItemCost();
				BigDecimal updatedLineItemCost = BigDecimal.ZERO;
				List<ValidCeRateType> ceRateTypes = costElement.getValidCeRateTypes();
				BudgetDetailCalcAmount budgetCalculatedAmount = null;
				if (ceRateTypes != null && !ceRateTypes.isEmpty()) {
					for (ValidCeRateType ceRateType : ceRateTypes) {
						FibiProposalRate applicableRate = budgetDao.fetchApplicableProposalRate(
								copyPeriod.getBudget().getBudgetId(), copyPeriod.getStartDate(),
								ceRateType.getRateClassCode(), ceRateType.getRateTypeCode(), activityTypeCode);
						if (applicableRate != null && (applicableRate.getRateClass().getRateClassTypeCode().equals("I")
								&& "7".equals(applicableRate.getRateClassCode()))) {
							BigDecimal validRate = BigDecimal.ZERO;
							validRate = validRate.add(applicableRate.getApplicableRate());
							if (validRate.compareTo(BigDecimal.ZERO) > 0) {
								BigDecimal hundred = new BigDecimal(100);
								BigDecimal percentageFactor = validRate.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
								BigDecimal calculatedCost = ((lineItemCost.multiply(percentageFactor)));
								updatedLineItemCost = updatedLineItemCost.add(calculatedCost);
								budgetCalculatedAmount = budgetService.getNewBudgetCalculatedAmount(copyPeriod, budgetDetail, applicableRate);
								budgetCalculatedAmount.setCalculatedCost(calculatedCost);
								copyBudgetDetail.getBudgetDetailCalcAmounts().add(budgetCalculatedAmount);
							}
						}
					}
				}
				/*if (updatedLineItemCost.compareTo(BigDecimal.ZERO) > 0) {
					if (budgetDetail.getIsApplyInflationRate().equals(true)) {
						lineItemCost = lineItemCost.add(updatedLineItemCost);
						copyBudgetDetail.setLineItemCost(lineItemCost.setScale(2, BigDecimal.ROUND_HALF_UP));
					} else {
						// lineItemCost = lineItemCost.subtract(updatedLineItemCost);
						copyBudgetDetail.setLineItemCost(lineItemCost.setScale(2, BigDecimal.ROUND_HALF_UP));
					}
				} else {*/
					copyBudgetDetail.setLineItemCost(lineItemCost.setScale(2, BigDecimal.ROUND_HALF_UP));
				/*}*/
				copyBudgetDetail.setLineItemDescription(budgetDetail.getLineItemDescription());
				copyBudgetDetail.setLineItemNumber(budgetDetail.getLineItemNumber());
				copyBudgetDetail.setOnOffCampusFlag(budgetDetail.getOnOffCampusFlag());
				copyBudgetDetail.setPeriod(copyPeriod);
				copyBudgetDetail.setPrevLineItemCost(budgetDetail.getPrevLineItemCost());
				copyBudgetDetail.setStartDate(budgetDetail.getStartDate());
				copyBudgetDetail.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
				copyBudgetDetail.setUpdateUser(copyPeriod.getUpdateUser());
				copyBudgetDetail.setFullName(budgetDetail.getFullName());
				copyBudgetDetail.setRolodexId(budgetDetail.getRolodexId());
				copyBudgetDetail.setPersonId(budgetDetail.getPersonId());
				copyBudgetDetail.setTbnId(budgetDetail.getTbnId());
				copyBudgetDetail.setTbnPerson(budgetDetail.getTbnPerson());
				copyBudgetDetail.setPersonType(budgetDetail.getPersonType());
				// copyBudgetDetail = budgetDao.saveBudgetDetail(copyBudgetDetail);
				newLineItems.add(copyBudgetDetail);
			}
			copyPeriod.getBudgetDetails().addAll(newLineItems);
		}
	}

}
