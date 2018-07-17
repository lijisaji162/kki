package com.polus.fibicomp.report.vo;

import java.util.List;
import java.util.Map;

import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.view.AwardView;
import com.polus.fibicomp.view.ExpenditureByAwardView;
import com.polus.fibicomp.view.ProtocolView;

public class ReportVO {

	private Integer grantCallId;

	private Integer proposalCount;

	private String reportName;

	private List<GrantCall> grantIds;

	private List<Proposal> proposals;

	private Map<String, List<Proposal>> applicationsByGrantCallType;

	private Map<String, List<ProtocolView>> protocolsByType;

	private Map<String, List<AwardView>> awardByGrantType;

	private Integer awardCount;

	private List<AwardView> awards;

	private String personId;

	private List<AwardView> awardNumbers;

	private String awardNumber;

	private List<ExpenditureByAwardView> expenditureList;

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public List<GrantCall> getGrantIds() {
		return grantIds;
	}

	public void setGrantIds(List<GrantCall> grantIds) {
		this.grantIds = grantIds;
	}

	public List<Proposal> getProposals() {
		return proposals;
	}

	public void setProposals(List<Proposal> proposals) {
		this.proposals = proposals;
	}

	public Integer getProposalCount() {
		return proposalCount;
	}

	public void setProposalCount(Integer proposalCount) {
		this.proposalCount = proposalCount;
	}

	public Integer getAwardCount() {
		return awardCount;
	}

	public void setAwardCount(Integer awardCount) {
		this.awardCount = awardCount;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public List<AwardView> getAwardNumbers() {
		return awardNumbers;
	}

	public void setAwardNumbers(List<AwardView> awardNumbers) {
		this.awardNumbers = awardNumbers;
	}

	public String getAwardNumber() {
		return awardNumber;
	}

	public void setAwardNumber(String awardNumber) {
		this.awardNumber = awardNumber;
	}

	public List<AwardView> getAwards() {
		return awards;
	}

	public void setAwards(List<AwardView> awards) {
		this.awards = awards;
	}

	public Map<String, List<Proposal>> getApplicationsByGrantCallType() {
		return applicationsByGrantCallType;
	}

	public void setApplicationsByGrantCallType(Map<String, List<Proposal>> applicationsByGrantCallType) {
		this.applicationsByGrantCallType = applicationsByGrantCallType;
	}

	public List<ExpenditureByAwardView> getExpenditureList() {
		return expenditureList;
	}

	public void setExpenditureList(List<ExpenditureByAwardView> expenditureList) {
		this.expenditureList = expenditureList;
	}

	public Map<String, List<ProtocolView>> getProtocolsByType() {
		return protocolsByType;
	}

	public void setProtocolsByType(Map<String, List<ProtocolView>> protocolsByType) {
		this.protocolsByType = protocolsByType;
	}

	public Integer getGrantCallId() {
		return grantCallId;
	}

	public void setGrantCallId(Integer grantCallId) {
		this.grantCallId = grantCallId;
	}

	public Map<String, List<AwardView>> getAwardByGrantType() {
		return awardByGrantType;
	}

	public void setAwardByGrantType(Map<String, List<AwardView>> awardByGrantType) {
		this.awardByGrantType = awardByGrantType;
	}

}
