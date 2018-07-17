package com.polus.fibicomp.committee.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeMemberExpertise;
import com.polus.fibicomp.committee.pojo.CommitteeMemberRoles;
import com.polus.fibicomp.committee.pojo.CommitteeMembershipType;
import com.polus.fibicomp.committee.pojo.CommitteeResearchAreas;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.CommitteeType;
import com.polus.fibicomp.committee.pojo.MembershipRole;
import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.committee.pojo.ScheduleStatus;
import com.polus.fibicomp.pojo.Rolodex;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.view.PersonDetailsView;

@Service
public interface CommitteeDao {

	/**
	 * This method is used fetch committeeType.
	 * @param committeeTypeCode - code of committee type.
	 * @return committeeType.
	 */
	public CommitteeType fetchCommitteeType(Integer committeeTypeCode);

	/**
	 * This method is used to fetch all ProtocolReviewTypes.
	 * @return list of ProtocolReviewTypes.
	 */
	public List<ProtocolReviewType> fetchAllReviewType();

	/**
	 * This method is used to fetch list of Lead Units.
	 * @return list of leadUnits.
	 */
	public List<Unit> fetchAllHomeUnits();

	/**
	 * This method is used to fetch all ResearchAreas.
	 * @return list of ResearchAreas.
	 */
	public List<ResearchArea> fetchAllResearchAreas();

	/**
	 * This method is used to retrieve current time.
	 * @return currentTime.
	 */
	public Date getCurrentDate();

	/**
	 * This method is used to retrieve current Timestamp.
	 * @return current Timestamp.
	 */
	public Timestamp getCurrentTimestamp();

	/**
	 * This method is used to convert Object into JSON format.
	 * @param object - request object.
	 * @return response - JSON data.
	 */
	public String convertObjectToJSON(Object object);

	/**
	 * This method is used to store committee data.
	 * @param committee - committee object to save.
	 * @return committee - saved committee object.
	 */
	public Committee saveCommittee(Committee committee);

	/**
	 * This method is used to fetch committee by Id.
	 * @param committeeId - Id of the committee.
	 * @return committee - committee object
	 */
	public Committee fetchCommitteeById(String committeeId);

	/**
	 * This method is used to fetch ScheduleStatus by status.
	 * @param scheduleStatus - Status to be fetched.
	 * @return status - scheduleStatus.
	 */
	public ScheduleStatus fetchScheduleStatusByStatus(String scheduleStatus);

	/**
	 * This method is used to fetch all committee.
	 * @return committees - list of committees.
	 */
	public List<Committee> loadAllCommittee();

	/**
	 * This method is used to load all employees.
	 * @return employeesList - list of employees.
	 */
	public List<PersonDetailsView> getAllEmployees();

	/**
	 * This method is used to load all non-employees.
	 * @return nonEmployeesList - list of non-employees.
	 */
	public List<Rolodex> getAllNonEmployees();

	/**
	 * This method is used to load all committee membership types.
	 * @return membershipTypeList - list of CommitteeMembershipTypes.
	 */
	public List<CommitteeMembershipType> getMembershipTypes();

	/**
	 * This method is used to load all MembershipRoles.
	 * @return membershipRoleList - list of MembershipRoles.
	 */
	public List<MembershipRole> getMembershipRoles();

	/**
	 * This method is used to get person's(employee) detail.
	 * @param personId - Id of the person.
	 * @return person - Person Object.
	 */
	public PersonDetailsView getPersonDetailsById(String personId);

	/**
	 * This method is used to delete membershipRole.
	 * @param roleId - Id of the membershipRole.
	 */
	public void deleteMemberRoles(Integer roleId);

	/**
	 * This method is used to delete expertise.
	 * @param expertiseId - Id of the expertise.
	 */
	public void deleteExpertise(Integer expertiseId);

	/**
	 * This method is used to get person's(non-employee) detail. 
	 * @param rolodexId - Id of the person.
	 * @return rolodex - Rolodex Object.
	 */
	public Rolodex getRolodexById(Integer rolodexId);

	/**
	 * This method is used to updateCommitteSchedule.
	 * @param committeeSchedule - committeeSchedule object for updation.
	 */
	public void updateCommitteSchedule(CommitteeSchedule committeeSchedule);

	/**
	 * This method is used to get CommitteeSchedule by Id.
	 * @param scheduleId - Id of the CommitteeSchedule.
	 * @return committeeSchedule - committeeSchedule object.
	 */
	public CommitteeSchedule getCommitteeScheduleById(Integer scheduleId);

	/**
	 * This method is used to fetch CommitteeMembershipType by Id.
	 * @param membershipTypeCode - code for CommitteeMembershipType.
	 * @return committeeMembershipType - CommitteeMembershipType object
	 */
	public CommitteeMembershipType getCommitteeMembershipTypeById(String membershipTypeCode);

	/**
	 * This method is used to fetch all ScheduleStatus.
	 * @return status - list of ScheduleStatus.
	 */
	public List<ScheduleStatus> fetchAllScheduleStatus();

	/**
	 * This method is used to save CommitteeMemberRole.
	 * @param memberRole - role of a member.
	 * @return memberRole.
	 */
	public CommitteeMemberRoles saveCommitteeMemberRole(CommitteeMemberRoles memberRole);

	/**
	 * This method is used to save CommitteeMemberExpertise.
	 * @param expertise - expertise of a member.
	 * @return expertise.
	 */
	public CommitteeMemberExpertise saveCommitteeMemberExpertise(CommitteeMemberExpertise expertise);

	/**
	 * This method is used to save CommitteeResearchAreas.
	 * @param researchAreas - ResearchArea of a committee.
	 * @return researchAreas.
	 */
	public CommitteeResearchAreas saveCommitteeResearchAreas(CommitteeResearchAreas researchAreas);

}
