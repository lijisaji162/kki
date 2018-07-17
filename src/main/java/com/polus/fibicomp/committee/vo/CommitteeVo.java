package com.polus.fibicomp.committee.vo;

import java.util.List;

import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeMemberExpertise;
import com.polus.fibicomp.committee.pojo.CommitteeMemberRoles;
import com.polus.fibicomp.committee.pojo.CommitteeMembershipType;
import com.polus.fibicomp.committee.pojo.CommitteeResearchAreas;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.MembershipRole;
import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.committee.pojo.ScheduleStatus;
import com.polus.fibicomp.committee.schedule.ScheduleData;
import com.polus.fibicomp.pojo.Rolodex;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.view.PersonDetailsView;

public class CommitteeVo {

	private Integer committeeTypeCode;

	private Committee committee;

	private List<ProtocolReviewType> reviewTypes;

	private List<Unit> homeUnits;

	private List<ResearchArea> researchAreas;

	private String updateType;

	private String currentUser;

	private String committeeId;
	
	private ScheduleData scheduleData;

	private List<PersonDetailsView> employees;

	private List<Rolodex> nonEmployees;

	private List<CommitteeMembershipType> committeeMembershipTypes;

	private List<MembershipRole> membershipRoles;

	private String personId;

	private boolean nonEmployeeFlag;

	private Integer rolodexId;

	private CommitteeSchedule committeeSchedule;

	private Boolean status;

	private String message;

	private Integer scheduleId;

	private Integer commMemberRolesId;

	private Integer commMemberExpertiseId;

	private Integer commMembershipId;

	private Integer commResearchAreasId;

	private CommitteeResearchAreas committeeResearchArea;

	private CommitteeMemberRoles committeeMemberRole;

	private CommitteeMemberExpertise committeeMemberExpertise;

	private List<ScheduleStatus> scheduleStatus;

	public List<ProtocolReviewType> getReviewTypes() {
		return reviewTypes;
	}

	public void setReviewTypes(List<ProtocolReviewType> reviewTypes) {
		this.reviewTypes = reviewTypes;
	}

	public List<Unit> getHomeUnits() {
		return homeUnits;
	}

	public void setHomeUnits(List<Unit> homeUnits) {
		this.homeUnits = homeUnits;
	}

	public List<ResearchArea> getResearchAreas() {
		return researchAreas;
	}

	public void setResearchAreas(List<ResearchArea> researchAreas) {
		this.researchAreas = researchAreas;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public Integer getCommitteeTypeCode() {
		return committeeTypeCode;
	}

	public void setCommitteeTypeCode(Integer committeeTypeCode) {
		this.committeeTypeCode = committeeTypeCode;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}

	public ScheduleData getScheduleData() {
		return scheduleData;
	}

	public void setScheduleData(ScheduleData scheduleData) {
		this.scheduleData = scheduleData;
	}

	public List<PersonDetailsView> getEmployees() {
		return employees;
	}

	public void setEmployees(List<PersonDetailsView> employees) {
		this.employees = employees;
	}

	public List<Rolodex> getNonEmployees() {
		return nonEmployees;
	}

	public void setNonEmployees(List<Rolodex> nonEmployees) {
		this.nonEmployees = nonEmployees;
	}

	public List<CommitteeMembershipType> getCommitteeMembershipTypes() {
		return committeeMembershipTypes;
	}

	public void setCommitteeMembershipTypes(List<CommitteeMembershipType> committeeMembershipTypes) {
		this.committeeMembershipTypes = committeeMembershipTypes;
	}

	public List<MembershipRole> getMembershipRoles() {
		return membershipRoles;
	}

	public void setMembershipRoles(List<MembershipRole> membershipRoles) {
		this.membershipRoles = membershipRoles;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public boolean isNonEmployeeFlag() {
		return nonEmployeeFlag;
	}

	public void setNonEmployeeFlag(boolean nonEmployeeFlag) {
		this.nonEmployeeFlag = nonEmployeeFlag;
	}

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getCommMemberRolesId() {
		return commMemberRolesId;
	}

	public void setCommMemberRolesId(Integer commMemberRolesId) {
		this.commMemberRolesId = commMemberRolesId;
	}

	public Integer getCommMemberExpertiseId() {
		return commMemberExpertiseId;
	}

	public void setCommMemberExpertiseId(Integer commMemberExpertiseId) {
		this.commMemberExpertiseId = commMemberExpertiseId;
	}

	public Integer getCommMembershipId() {
		return commMembershipId;
	}

	public void setCommMembershipId(Integer commMembershipId) {
		this.commMembershipId = commMembershipId;
	}

	public Integer getCommResearchAreasId() {
		return commResearchAreasId;
	}

	public void setCommResearchAreasId(Integer commResearchAreasId) {
		this.commResearchAreasId = commResearchAreasId;
	}

	public CommitteeResearchAreas getCommitteeResearchArea() {
		return committeeResearchArea;
	}

	public void setCommitteeResearchArea(CommitteeResearchAreas committeeResearchArea) {
		this.committeeResearchArea = committeeResearchArea;
	}

	public CommitteeMemberRoles getCommitteeMemberRole() {
		return committeeMemberRole;
	}

	public void setCommitteeMemberRole(CommitteeMemberRoles committeeMemberRole) {
		this.committeeMemberRole = committeeMemberRole;
	}

	public CommitteeMemberExpertise getCommitteeMemberExpertise() {
		return committeeMemberExpertise;
	}

	public void setCommitteeMemberExpertise(CommitteeMemberExpertise committeeMemberExpertise) {
		this.committeeMemberExpertise = committeeMemberExpertise;
	}

	public List<ScheduleStatus> getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(List<ScheduleStatus> scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

}
