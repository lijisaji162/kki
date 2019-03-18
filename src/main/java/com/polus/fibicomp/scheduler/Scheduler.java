package com.polus.fibicomp.scheduler;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.email.service.FibiEmailService;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.service.ProposalService;
import com.polus.fibicomp.workflow.dao.WorkflowDao;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;

@Component
@Transactional
public class Scheduler {

	protected static Logger logger = Logger.getLogger(Scheduler.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private FibiEmailService fibiEmailService;

	@Autowired
	private ProposalService proposalService;

	@Value("${application.context.name}")
	private String context;

	@Scheduled(cron = "0 0 */8 * * *")
	public void sendScheduledEmailNotification() {
		logger.info("--------- sendScheduledEmailNotification ---------");
		Date date = new Date();
		Timestamp currentTime = new Timestamp(date.getTime());
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Proposal.class);
		criteria.createAlias("proposalStatus", "proposalStatus");
		criteria.add(Restrictions.eq("proposalStatus.statusCode", Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
		criteria.add(Restrictions.eq("isInactive", false));
		@SuppressWarnings("unchecked")
		List<Proposal> proposals = criteria.list();
		if (proposals != null && !proposals.isEmpty()) {
			for (Proposal proposalObject : proposals) {
				if (proposalObject.getSubmissionDate() != null) {
					long milliseconds = currentTime.getTime() - proposalObject.getSubmissionDate().getTime();
					int seconds = (int) milliseconds / 1000;
					int hours = seconds / 3600;
					Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposalObject.getProposalId());
					if (hours > 24) {
						if (workflow != null) {
							List<WorkflowDetail> workflowDetails = workflow.getWorkflowDetails();
							for (WorkflowDetail workflowDetail : workflowDetails) {
								if (workflowDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
									if (workflowDetail.getFirstCronEmailFlag() != null) {
										if (!workflowDetail.getFirstCronEmailFlag()) {
											String piName = proposalService.getPrincipalInvestigator(proposalObject.getProposalPersons());
											String message = "The following proposal is need to be reviewed:<br/><br/>Proposal Number: "
													+ proposalObject.getProposalId() + "<br/>" + "Proposal Title: "
													+ proposalObject.getTitle() + "<br/>Principal Investigator: "
													+ piName + "<br/>" + "Lead Unit: "
													+ proposalObject.getHomeUnitNumber() + " - "
													+ proposalObject.getHomeUnitName() + "<br/>" + "Deadline Date: "
													+ proposalObject.getSponsorDeadlineDate()
													+ "<br/><br/>Please go to "
													+ "<a title=\"\" target=\"_self\" href=\"" + context
													+ "/proposal/proposalHome?proposalId="
													+ proposalObject.getProposalId() + "\">this link</a> "
													+ "to review the proposal and provide your response by clicking on the Approve or Reject buttons.";
											String subject = "Reminder:Action Required: Approval for "
													+ proposalObject.getTitle();
											Set<String> toAddresses = new HashSet<String>();
											toAddresses.add(workflowDetail.getEmailAddress());
											fibiEmailService.sendEmail(toAddresses, subject, null, null, message, true);
											workflowDetail.setFirstCronEmailFlag(true);
										}
									}
								}
							}
							workflow.setWorkflowDetails(workflowDetails);
							workflow = workflowDao.saveWorkflow(workflow);
						}
					}
				}
			}
		}
	}

	@Scheduled(cron = "0 3 */8 * * *")
	public void sendRemainderEmailNotification() {
		logger.info("--------- sendRemainderEmailNotification ---------");
		Date date = new Date();
		Timestamp currentTime = new Timestamp(date.getTime());

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Proposal.class);
		criteria.createAlias("proposalStatus", "proposalStatus");
		criteria.add(Restrictions.eq("proposalStatus.statusCode", Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
		criteria.add(Restrictions.eq("isInactive", false));
		@SuppressWarnings("unchecked")
		List<Proposal> proposals = criteria.list();

		for (Proposal proposalObject : proposals) {
			if (proposalObject.getSubmissionDate() != null) {
				long milliseconds = currentTime.getTime() - proposalObject.getSubmissionDate().getTime();
				int seconds = (int) milliseconds / 1000;
				int hours = seconds / 3600;
				Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposalObject.getProposalId());
				if (hours > 48) {
					if (workflow != null) {
						List<WorkflowDetail> workflowDetails = workflow.getWorkflowDetails();
						for (WorkflowDetail workflowDetail : workflowDetails) {
							if (workflowDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
								if (workflowDetail.getSecondCronEmailFlag() != null) {
									if (!workflowDetail.getSecondCronEmailFlag()) {
										String piName = proposalService.getPrincipalInvestigator(proposalObject.getProposalPersons());
										String message = "The following proposal is need to be reviewed:<br/><br/>Proposal Number: "
												+ proposalObject.getProposalId() + "<br/>" + "Proposal Title: "
												+ proposalObject.getTitle() + "<br/>Principal Investigator: "
												+ piName + "<br/>" + "Lead Unit: "
												+ proposalObject.getHomeUnitNumber() + " - "
												+ proposalObject.getHomeUnitName() + "<br/>" + "Deadline Date: "
												+ proposalObject.getSponsorDeadlineDate()
												+ "<br/><br/>Please go to "
												+ "<a title=\"\" target=\"_self\" href=\"" + context
												+ "/proposal/proposalHome?proposalId="
												+ proposalObject.getProposalId() + "\">this link</a> "
												+ "to review the proposal and provide your response by clicking on the Approve or Reject buttons.";
										String subject = "Reminder:Action Required: Approval for "
												+ proposalObject.getTitle();
										Set<String> toAddresses = new HashSet<String>();
										toAddresses.add(workflowDetail.getEmailAddress());
										fibiEmailService.sendEmail(toAddresses, subject, null, null, message, true);
										workflowDetail.setSecondCronEmailFlag(true);
									}
								}
							}
						}
						workflow.setWorkflowDetails(workflowDetails);
						workflow = workflowDao.saveWorkflow(workflow);
					}
				}
			}
		}
	}

}
