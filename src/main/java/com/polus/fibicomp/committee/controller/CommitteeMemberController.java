package com.polus.fibicomp.committee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.committee.service.CommitteeMemberService;
import com.polus.fibicomp.committee.vo.CommitteeVo;

@RestController
public class CommitteeMemberController {

	protected static Logger logger = Logger.getLogger(CommitteeMemberController.class.getName());

	@Autowired
	@Qualifier(value = "committeeMemberService")
	private CommitteeMemberService committeeMemberService;

	@RequestMapping(value = "/addCommitteeMembership", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addCommitteeMembership(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addCommitteeMembership");
		String committeeDatas = committeeMemberService.addCommitteeMembership(vo);
		return committeeDatas;
	}

	@RequestMapping(value = "/saveCommitteeMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveCommitteeMembers(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommitteeMembers");
		return committeeMemberService.saveCommitteeMembers(vo);
	}

	@RequestMapping(value = "/deleteCommitteeMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteCommitteeMembers(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteCommitteeMembers");
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		logger.info("CommitteeId : " + vo.getCommitteeId());
		return committeeMemberService.deleteCommitteeMembers(vo);
	}

	@RequestMapping(value = "/saveCommitteeMembersRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveCommitteeMembersRole(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommitteeMembersRole");
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("MembershipRoleCode : " + vo.getCommitteeMemberRole().getMembershipRoleCode());
		logger.info("MembershipRoleDescription : " + vo.getCommitteeMemberRole().getMembershipRoleDescription());
		logger.info("StartDate : " + vo.getCommitteeMemberRole().getStartDate());
		logger.info("EndDate : " + vo.getCommitteeMemberRole().getEndDate());
		return committeeMemberService.saveCommitteeMembersRole(vo);
	}

	@RequestMapping(value = "/updateMemberRoles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String updateMemberRoles(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateMemberRoles");
		logger.info("CommMemberRolesId : " + vo.getCommMemberRolesId());
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		return committeeMemberService.updateMemberRoles(vo);
	}

	@RequestMapping(value = "/deleteMemberRoles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteMemberRoles(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteMemberRoles");
		logger.info("CommMemberRolesId : " + vo.getCommMemberRolesId());
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		return committeeMemberService.deleteMemberRoles(vo);
	}

	@RequestMapping(value = "/saveCommitteeMembersExpertise", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveCommitteeMembersExpertise(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommitteeMembersExpertise");
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ResearchAreaCode : " + vo.getCommitteeMemberExpertise().getResearchAreaCode());
		logger.info("ResearchAreaDescription : " + vo.getCommitteeMemberExpertise().getResearchAreaDescription());
		return committeeMemberService.saveCommitteeMembersExpertise(vo);
	}

	@RequestMapping(value = "/deleteMemberExpertise", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteExpertise(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteMemberExpertise");
		logger.info("CommMemberExpertiseId : " + vo.getCommMemberExpertiseId());
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		return committeeMemberService.deleteExpertise(vo);
	}

}
