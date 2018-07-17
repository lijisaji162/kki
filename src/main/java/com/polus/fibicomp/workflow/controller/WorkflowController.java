package com.polus.fibicomp.workflow.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.workflow.service.WorkflowService;

@RestController
public class WorkflowController {

	protected static Logger logger = Logger.getLogger(WorkflowController.class.getName());
	
	@Autowired
	@Qualifier(value = "workflowService")
	private WorkflowService workflowService;

	@RequestMapping(value = "/downloadWorkflowAttachment", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadWorkflowAttachment(HttpServletResponse response, @RequestHeader("attachmentId") String attachmentId) {
		logger.info("Requesting for downloadWorkflowAttachment");
		logger.info("attachmentId : " + attachmentId);
		Integer attachmentid = Integer.parseInt(attachmentId);
		return workflowService.downloadWorkflowAttachment(attachmentid);
	}
}
