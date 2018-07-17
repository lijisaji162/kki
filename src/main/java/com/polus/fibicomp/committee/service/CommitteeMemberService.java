package com.polus.fibicomp.committee.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.vo.CommitteeVo;

@Service
public interface CommitteeMemberService {

	/**
	 * This method is used to add committee members.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details of committee with CommitteeMembership.
	 */
	public String addCommitteeMembership(CommitteeVo committeeVo);

	/**
	 * This method is used to save committee member.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details of committee with CommitteeMembership.
	 */
	public String saveCommitteeMembers(CommitteeVo committeeVo);

	/**
	 * This method is used to delete committee members.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details of committee with updated list of CommitteeMembership.
	 */
	public String deleteCommitteeMembers(CommitteeVo committeeVo);

	/**
	 * This method is used to save committee member roles. 
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details of committee with list of CommitteeMembershipRoles.
	 */
	public String saveCommitteeMembersRole(CommitteeVo committeeVo);

	/**
	 * This method is used to delete member roles.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details of committee with updated list of CommitteeMembershipRoles.
	 */
	public String deleteMemberRoles(CommitteeVo committeeVo);

	/**
	 * This method is used to update member roles.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details of committee with updated list of CommitteeMembershipRoles.
	 */
	public String updateMemberRoles(CommitteeVo committeeVo);

	/**
	 * This method is used to delete expertise.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details of committee with updated list of CommitteeMemberExpertise.
	 */
	public String deleteExpertise(CommitteeVo committeeVo);

	/**
	 * This method is used to save committee members expertise.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return  a String of details of committee with list of CommitteeMemberExpertise.
	 */
	public String saveCommitteeMembersExpertise(CommitteeVo committeeVo);

}
