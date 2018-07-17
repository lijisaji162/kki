package com.polus.fibicomp.schedule.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.schedule.service.ScheduleService;
import com.polus.fibicomp.schedule.vo.ScheduleVo;

@RestController
public class ScheduleController {

	protected static Logger logger = Logger.getLogger(ScheduleController.class.getName());

	@Autowired
	@Qualifier(value = "scheduleService")
	private ScheduleService scheduleService;
	
	@RequestMapping(value = "/loadScheduleById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loadScheduleById(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadScheduleById");
		logger.info("scheduleId : " + vo.getScheduleId());
		String committeeDatas = scheduleService.loadScheduleById(vo.getScheduleId());
		return committeeDatas;
	}

	@RequestMapping(value = "/updateSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String updateSchedule(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateSchedule");
		String scheduleDatas = scheduleService.updateSchedule(vo);
		return scheduleDatas;
	}

	@RequestMapping(value = "/addOtherActions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addOtherActions(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addOtherActions");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("scheduleId : " + vo.getScheduleId());
		logger.info("scheduleActItemTypecode : " + vo.getCommitteeScheduleActItems().getScheduleActItemTypecode());
		logger.info("scheduleActItemTypeDescription : " + vo.getCommitteeScheduleActItems().getScheduleActItemTypeDescription());
		logger.info("itemDescription : " + vo.getCommitteeScheduleActItems().getItemDescription());
		String scheduleDatas = scheduleService.addOtherActions(vo);
		return scheduleDatas;
	}

	@RequestMapping(value = "/deleteOtherActions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteOtherActions(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteOtherActions");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("actionItemId : " + vo.getCommScheduleActItemsId());
		return scheduleService.deleteOtherActions(vo);
	}

	@RequestMapping(value = "/addCommitteeScheduleMinute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addCommitteeScheduleMinute(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addCommitteeScheduleMinute");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		return scheduleService.addCommitteeScheduleMinute(vo);
	}

	@RequestMapping(value = "/updateScheduleAttendance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String updateCommitteeSchedule(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateScheduleAttendance");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ScheduleId : " + vo.getScheduleId());
		logger.info("AttendanceId : " + vo.getUpdatedAttendance().getCommitteeScheduleAttendanceId());
		logger.info("MemberPresent : " + vo.getUpdatedAttendance().getMemberPresent());
		return scheduleService.updateScheduleAttendance(vo);
	}

	@RequestMapping(value = "/addOthersPresent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addOthersPresent(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addOthersPresent");
		logger.info("ScheduleId : " + vo.getScheduleId());
		return scheduleService.addOthersPresent(vo);
	}

	@RequestMapping(value = "/deleteScheduleMinute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteScheduleMinute(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteScheduleMinute");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("CommitteeScheduleMinuteId : " + vo.getCommScheduleMinuteId());
		return scheduleService.deleteScheduleMinute(vo);
	}

	@RequestMapping(value = "/deleteScheduleAttachment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteScheduleAttachment(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteScheduleAttachment");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("CommitteeScheduleAttachId : " + vo.getCommScheduleAttachId());
		return scheduleService.deleteScheduleAttachment(vo);
	}

	@RequestMapping(value = "/addScheduleAttachment", method = RequestMethod.POST)
	public String addScheduleAttachment(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestParam("formDataJson") String formDataJson) {
		logger.info("Requesting for addScheduleAttachment");
		return scheduleService.addScheduleAttachment(files, formDataJson);
	}

	@RequestMapping(value = "/updateScheduleAttachment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String updateScheduleAttachment(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateScheduleAttendance");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ScheduleId : " + vo.getScheduleId());
		logger.info("AttachmentId : " + vo.getNewCommitteeScheduleAttachment().getCommScheduleAttachId());
		return scheduleService.updateScheduleAttachment(vo);
	}

	@RequestMapping(value = "/deleteScheduleAttendance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteScheduleAttendance(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteScheduleAttachment");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("CommitteeScheduleAttendanceId : " + vo.getCommScheduleAttendanceId());
		return scheduleService.deleteScheduleAttendance(vo);
	}

	@RequestMapping(value = "/downloadScheduleAttachment", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadScheduleAttachment(HttpServletResponse response, @RequestHeader("commScheduleAttachId") String commScheduleAttachId) {
		logger.info("Requesting for downloadScheduleAttachment");
		logger.info("commScheduleAttachId : " + commScheduleAttachId);
		Integer attachmentid = Integer.parseInt(commScheduleAttachId);
		return scheduleService.downloadScheduleAttachment(attachmentid);
	}

	@RequestMapping(value = "/updateCommitteeScheduleMinute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String updateCommitteeScheduleMinute(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateCommitteeScheduleMinute");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		return scheduleService.updateCommitteeScheduleMinute(vo);
	}

}
