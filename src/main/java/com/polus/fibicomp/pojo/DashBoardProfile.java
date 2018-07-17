package com.polus.fibicomp.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.view.AwardView;
import com.polus.fibicomp.view.DisclosureView;
import com.polus.fibicomp.view.ExpenditureVolume;
import com.polus.fibicomp.view.IacucView;
import com.polus.fibicomp.view.ProposalView;
import com.polus.fibicomp.view.ProtocolView;
import com.polus.fibicomp.view.ResearchSummaryPieChart;
import com.polus.fibicomp.view.ResearchSummaryView;

public class DashBoardProfile {

	private ArrayList<HashMap<String, Object>> dashBoardDetailMap;

	private ArrayList<HashMap<String, Object>> dashBoardResearchSummaryMap;

	private Integer totalServiceRequest;

	private Integer serviceRequestCount;

	private List<Integer> pageNumbers;

	private PersonDTO personDTO;

	private String encryptedUserName;

	private List<AwardView> awardViews;

	private List<ProposalView> proposalViews;

	private List<ProtocolView> protocolViews;

	private List<IacucView> iacucViews;

	private List<DisclosureView> disclosureViews;

	private List<ExpenditureVolume> expenditureVolumes;

	private List<ResearchSummaryView> summaryViews;

	private List<ResearchSummaryPieChart> summaryAwardPieChart;

	private List<ResearchSummaryPieChart> summaryProposalPieChart;

	private List<ResearchSummaryPieChart> summaryProposalDonutChart;

	private List<ResearchSummaryPieChart> summaryAwardDonutChart;

	private List<Committee> committees;

	private List<CommitteeSchedule> committeeSchedules;

	private List<GrantCall> grantCalls;

	private List<Proposal> proposal;

	public ArrayList<HashMap<String, Object>> getDashBoardDetailMap() {
		return dashBoardDetailMap;
	}

	public void setDashBoardDetailMap(ArrayList<HashMap<String, Object>> dashBoardDetailMap) {
		this.dashBoardDetailMap = dashBoardDetailMap;
	}

	public Integer getTotalServiceRequest() {
		return totalServiceRequest;
	}

	public void setTotalServiceRequest(Integer totalServiceRequest) {
		this.totalServiceRequest = totalServiceRequest;
	}

	public Integer getServiceRequestCount() {
		return serviceRequestCount;
	}

	public void setServiceRequestCount(Integer serviceRequestCount) {
		this.serviceRequestCount = serviceRequestCount;
	}

	public List<Integer> getPageNumbers() {
		return pageNumbers;
	}

	public void setPageNumbers(List<Integer> pageNumbers) {
		this.pageNumbers = pageNumbers;
	}

	public PersonDTO getPersonDTO() {
		return personDTO;
	}

	public void setPersonDTO(PersonDTO personDTO) {
		this.personDTO = personDTO;
	}

	public ArrayList<HashMap<String, Object>> getDashBoardResearchSummaryMap() {
		return dashBoardResearchSummaryMap;
	}

	public void setDashBoardResearchSummaryMap(ArrayList<HashMap<String, Object>> dashBoardResearchSummaryMap) {
		this.dashBoardResearchSummaryMap = dashBoardResearchSummaryMap;
	}

	public String getEncryptedUserName() {
		return encryptedUserName;
	}

	public void setEncryptedUserName(String encryptedUserName) {
		this.encryptedUserName = encryptedUserName;
	}

	public List<AwardView> getAwardViews() {
		return awardViews;
	}

	public void setAwardViews(List<AwardView> awardViews) {
		this.awardViews = awardViews;
	}

	public List<ProposalView> getProposalViews() {
		return proposalViews;
	}

	public void setProposalViews(List<ProposalView> proposalViews) {
		this.proposalViews = proposalViews;
	}

	public List<ProtocolView> getProtocolViews() {
		return protocolViews;
	}

	public void setProtocolViews(List<ProtocolView> protocolViews) {
		this.protocolViews = protocolViews;
	}

	public List<IacucView> getIacucViews() {
		return iacucViews;
	}

	public void setIacucViews(List<IacucView> iacucViews) {
		this.iacucViews = iacucViews;
	}

	public List<DisclosureView> getDisclosureViews() {
		return disclosureViews;
	}

	public void setDisclosureViews(List<DisclosureView> disclosureViews) {
		this.disclosureViews = disclosureViews;
	}

	public List<ExpenditureVolume> getExpenditureVolumes() {
		return expenditureVolumes;
	}

	public void setExpenditureVolumes(List<ExpenditureVolume> expenditureVolumes) {
		this.expenditureVolumes = expenditureVolumes;
	}

	public List<ResearchSummaryView> getSummaryViews() {
		return summaryViews;
	}

	public void setSummaryViews(List<ResearchSummaryView> summaryViews) {
		this.summaryViews = summaryViews;
	}

	public List<ResearchSummaryPieChart> getSummaryAwardPieChart() {
		return summaryAwardPieChart;
	}

	public void setSummaryAwardPieChart(List<ResearchSummaryPieChart> summaryAwardPieChart) {
		this.summaryAwardPieChart = summaryAwardPieChart;
	}

	public List<ResearchSummaryPieChart> getSummaryProposalPieChart() {
		return summaryProposalPieChart;
	}

	public void setSummaryProposalPieChart(List<ResearchSummaryPieChart> summaryProposalPieChart) {
		this.summaryProposalPieChart = summaryProposalPieChart;
	}

	public List<ResearchSummaryPieChart> getSummaryProposalDonutChart() {
		return summaryProposalDonutChart;
	}

	public void setSummaryProposalDonutChart(List<ResearchSummaryPieChart> summaryProposalDonutChart) {
		this.summaryProposalDonutChart = summaryProposalDonutChart;
	}

	public List<ResearchSummaryPieChart> getSummaryAwardDonutChart() {
		return summaryAwardDonutChart;
	}

	public void setSummaryAwardDonutChart(List<ResearchSummaryPieChart> summaryAwardDonutChart) {
		this.summaryAwardDonutChart = summaryAwardDonutChart;
	}

	public List<Committee> getCommittees() {
		return committees;
	}

	public void setCommittees(List<Committee> committees) {
		this.committees = committees;
	}

	public List<CommitteeSchedule> getCommitteeSchedules() {
		return committeeSchedules;
	}

	public void setCommitteeSchedules(List<CommitteeSchedule> committeeSchedules) {
		this.committeeSchedules = committeeSchedules;
	}

	public List<GrantCall> getGrantCalls() {
		return grantCalls;
	}

	public void setGrantCalls(List<GrantCall> grantCalls) {
		this.grantCalls = grantCalls;
	}

	public List<Proposal> getProposal() {
		return proposal;
	}

	public void setProposal(List<Proposal> proposal) {
		this.proposal = proposal;
	}
}
